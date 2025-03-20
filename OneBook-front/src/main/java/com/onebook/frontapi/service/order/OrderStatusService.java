package com.onebook.frontapi.service.order;

import com.onebook.frontapi.dto.order.OrderByStatusResponseDto;
import com.onebook.frontapi.feign.order.OrderStatusClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderStatusService {
    private final OrderStatusClient orderStatusClient;

    public List<String> getAllOrderStatuses() {
        return orderStatusClient.findAllOrderStatuses();
    }
}
