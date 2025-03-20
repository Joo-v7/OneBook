package com.nhnacademy.taskapi.order.service.Impl;

import com.nhnacademy.taskapi.book.domain.Book;
import com.nhnacademy.taskapi.book.service.BookService;
import com.nhnacademy.taskapi.order.dto.BookOrderRequest;
import com.nhnacademy.taskapi.order.dto.OrderDetailBookResponse;
import com.nhnacademy.taskapi.order.dto.OrderDetailResponse;
import com.nhnacademy.taskapi.order.dto.OrderResponse;
import com.nhnacademy.taskapi.order.entity.Order;
import com.nhnacademy.taskapi.order.entity.OrderDetail;
import com.nhnacademy.taskapi.order.exception.OrderInvalidException;
import com.nhnacademy.taskapi.order.exception.OrderNotFoundException;
import com.nhnacademy.taskapi.order.repository.OrderDetailRepository;
import com.nhnacademy.taskapi.order.repository.OrderRepository;
import com.nhnacademy.taskapi.order.service.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderDetailServiceImpl implements OrderDetailService {
    private final BookService bookService;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;

    @Override
    public void saveOrderDetail(Order order, List<BookOrderRequest> items) {
        // TODO 책 가격 검증 필요

        for (BookOrderRequest item : items) {
            Book book = bookService.getBook(item.getBookId());
            OrderDetail orderDetail = new OrderDetail(
                    order,
                    book,
                    book.getSalePrice(),
                    item.getQuantity(),
                    item.getDiscountAmount(),
                    item.getDiscountedPrice()
            );
            orderDetailRepository.save(orderDetail);
        }
    }

    @Override
    public OrderDetailResponse getOrderDetail(Long memberId, Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId + "is not found"));
        List<OrderDetail> orderDetailList = order.getOrderDetailList();
        OrderDetailBookResponse.fromOrderDetail(orderDetailList.get(0));

        List<OrderDetailBookResponse> items = orderDetailList.stream().map(OrderDetailBookResponse::fromOrderDetail).toList();

        OrderResponse orderResponse = OrderResponse.fromOrder(orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order id " + orderId + " does not exist")
                ));

        // MemberId 검증
        if (orderResponse.getMemberId() != memberId) {
            throw new OrderInvalidException("회원지 가지고있지 않는 주문 번호입니다.");
        }

        // TODO 결제 정보 추가

        return new OrderDetailResponse(items, orderResponse);
    }
}
