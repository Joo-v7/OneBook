package com.onebook.frontapi.controller.order;

import com.onebook.frontapi.dto.address.response.MemberAddressResponse;
import com.onebook.frontapi.dto.order.BookListRequest;
import com.onebook.frontapi.dto.book.BookDTO;
import com.onebook.frontapi.dto.book.BookOrderRequest;
import com.onebook.frontapi.dto.order.*;
import com.onebook.frontapi.service.address.AddressService;
import com.onebook.frontapi.service.book.BookService;
import com.onebook.frontapi.service.member.MemberService;
import com.onebook.frontapi.service.order.OrderService;
import com.onebook.frontapi.service.order.OrderStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Controller
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final OrderStatusService orderStatusService;
    private final BookService bookService;
    private final AddressService addressService;

    @PostMapping("/order/register-form")
    public String orderRegistesr(@ModelAttribute BookListRequest bookListRequest, Model model) {
        // 책 리스트
        Map<BookDTO, Integer> bookMap = new HashMap<>();
        List<BookOrderRequest> bookOrderRequests = bookListRequest.getBookOrderRequests();
        for (BookOrderRequest bookOrderRequest : bookOrderRequests) {
            bookMap.put(bookService.getBook(bookOrderRequest.getBookId()), bookOrderRequest.getQuantity());
        }
        model.addAttribute("bookMap", bookMap);

        // 사용자의 전화번호
        String orderderPhoneNumber = memberService.getMember().phoneNumber();
        model.addAttribute("ordererPhoneNumber", orderderPhoneNumber);

        // TODO 사용자의 기본 배송지
        List<MemberAddressResponse> address = addressService.getAllMemberAddressByMemberId();
        for (MemberAddressResponse memberAddressResponse : address) {
            if (memberAddressResponse.isDefaultLocation()) {
                model.addAttribute("defaultAddress", memberAddressResponse);
            }
        }

        // 배송 선택 날짜
        // TODO utils에 넘기고싶음
        List<Map<String, String>> reservationDates = new ArrayList<>(4);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM월 dd일");
        for (int i = 1; i < 6; i++) {
            LocalDate day = LocalDate.now().plusDays(i + 1);

            reservationDates.add(Map.of(
                    "orderNum", String.valueOf(i),
                    "completedDate", String.valueOf(day),
                    "description", day.format(formatter)
            ));
        }
        model.addAttribute("reservationDates", reservationDates);


        return "order/order";
    }

    @PostMapping("/order/register")
    public String submitOrder(@ModelAttribute OrderFormRequest orderFormRequest) {
        Long createOrderId = orderService.createOrder(orderFormRequest);

        return "redirect:/front/payments/checkout-page?orderId=" + createOrderId;
    }

    @GetMapping("/admin/orders")
    public String orderStatus(@RequestParam String status, Model model) {
        model.addAttribute("status", status);

        List<String> orderStatusList = orderStatusService.getAllOrderStatuses();
        model.addAttribute("orderStatusList", orderStatusList);

        List<OrderByStatusResponseDto> ordersByStatus = orderService.getOrdersByStatus(status);
        model.addAttribute("ordersByStatus", ordersByStatus);

        return "admin/orderStatus";
    }

    @PostMapping("/admin/update-orderstatus")
    public String orderStatus(@RequestParam("orderIds") List<Long> orderIds, @RequestParam("preStatus") String preStatus, String postStatus) {
        orderService.updateOrderStatus(orderIds, postStatus);

        // 상태 값 URL 인코딩
        String encodedStatus = URLEncoder.encode(preStatus, StandardCharsets.UTF_8);

        return "redirect:/admin/orders?status=" + encodedStatus;
    }

}
