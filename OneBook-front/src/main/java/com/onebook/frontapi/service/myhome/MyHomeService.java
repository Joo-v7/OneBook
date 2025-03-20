package com.onebook.frontapi.service.myhome;

import com.onebook.frontapi.dto.myhome.MyOrderStatusResponse;
import com.onebook.frontapi.dto.order.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class MyHomeService {
    public MyOrderStatusResponse getMyOrderStatus(Page<OrderResponse> orderResponses) {
        // 상태별 카운트를 저장하는 맵.
        Map<String, Integer> statusCountMap = new HashMap<>();

        statusCountMap.put("결제대기", 0);
        statusCountMap.put("배송전", 0);
        statusCountMap.put("배송중", 0);
        statusCountMap.put("배송완료", 0);
        statusCountMap.put("반품", 0);
        statusCountMap.put("교환", 0);
        statusCountMap.put("주문취소", 0);
        statusCountMap.put("구매확정", 0);

        for(OrderResponse orderResponse : orderResponses) {
            String orderStatusName = orderResponse.getOrderStatusName();

            if(statusCountMap.containsKey(orderStatusName)) {
                statusCountMap.put(orderStatusName, statusCountMap.get(orderStatusName) + 1);
            }
        }

        return new MyOrderStatusResponse(
                statusCountMap.get("결제대기"),
                statusCountMap.get("배송전"),
                statusCountMap.get("배송중"),
                statusCountMap.get("배송완료"),
                statusCountMap.get("반품") + statusCountMap.get("교환") + statusCountMap.get("주문취소"),
                statusCountMap.get("구매확정")
        );
    }

}
