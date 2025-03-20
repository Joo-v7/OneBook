package com.onebook.frontapi.service.order;

import com.onebook.frontapi.dto.order.*;
import com.onebook.frontapi.feign.member.MemberClient;
import com.onebook.frontapi.feign.order.OrderClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderClient orderClient;
    private final MemberClient memberClient;

    public Long createOrder(OrderFormRequest orderFormRequest) {
        return orderClient.createOrder(orderFormRequest);
    }

    // TODO 존재가 이상함
    public OrderMemberResponse getOrder(Long orderId) {
        OrderMemberFeignResponse body = orderClient.findOrderByOrderId(orderId).getBody();
        // TODO 에러처리
        if (body == null) {
            throw new RuntimeException();
        }
        return OrderMemberResponse.fromFeign(body);
    }

    public OrderResponse getOrders(Long orderId) {
        OrderMemberFeignResponse body = orderClient.findOrderByOrderId(orderId).getBody();
        // TODO 에러처리
        if (body == null) {
            throw new RuntimeException();
        }
        return OrderResponse.fromFeign(body);
    }

    // 상태를 통해 주문목록 가져오기
    public Page<OrderResponse> getOrdersByStatus(String orderStatus, Pageable pageable) {
        Page<OrderFeignResponse> body = orderClient.findOrders(orderStatus, pageable).getBody();
        return body.map(OrderResponse::fromFeign);
    }

    // TODO 변경필수
    // admin이 사용하는 상태를 통해 주문목록 가져오기
    public List<OrderByStatusResponseDto> getOrdersByStatus(String status) {
        return orderClient.findOrderByStatus(status);
    }

    // 주문 상태 변경
    public void updateOrderStatus(List<Long> orderIds, String status) {
        orderClient.updateOrderStatus(orderIds, status);
        try {
            if (status.equals("구매확정")) {
                for (Long orderId : orderIds) {
                    OrderMemberResponse orderMemberResponse = getOrder(orderId);
                    memberClient.getMemberNetPaymentAmountForAdmin(orderMemberResponse.getMemberId());
                }
            }
        }catch(FeignException e) {
            log.info("순수 구매 금액에 따른 회원 등급 업데이트 실패");
        }
    }

    // 주문디테일 가져오기
    public OrderDetailResponse getOrderDetail(Long orderId) {
        OrderDetailFeignResponse orderDetail = orderClient.findOrderDetail(orderId);

        OrderDetailResponse orderDetailResponse = new OrderDetailResponse(
            orderDetail.getItems(),
            OrderResponse.fromFeign(orderDetail.getOrderResponse())
        );

        return orderDetailResponse;
    }

    // 교환, 반품&환불 진행
    public void createOrderSolution(Long orderId, OrderSolutionRequest orderSolutionRequest) {
        orderClient.saveOrderSolution(orderId, orderSolutionRequest);
    }
}
