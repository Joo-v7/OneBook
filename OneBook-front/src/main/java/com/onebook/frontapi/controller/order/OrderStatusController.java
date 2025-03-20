package com.onebook.frontapi.controller.order;

import com.onebook.frontapi.dto.order.OrderByStatusResponseDto;
import com.onebook.frontapi.service.order.OrderService;
import com.onebook.frontapi.service.order.OrderStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class OrderStatusController {

    // TODO OrderStatus 관리 컨트롤러

}
