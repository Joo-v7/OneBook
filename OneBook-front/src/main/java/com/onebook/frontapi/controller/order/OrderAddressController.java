package com.onebook.frontapi.controller.order;

import com.onebook.frontapi.dto.address.response.MemberAddressResponse;
import com.onebook.frontapi.dto.order.OrderAddressResponseDto;
import com.onebook.frontapi.service.address.AddressService;
import com.onebook.frontapi.service.order.OrderAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class OrderAddressController {

    private final OrderAddressService orderAddressService;

    @GetMapping("/order/addresses")
    public String addressManagement(Model model) {

        List<OrderAddressResponseDto> allOrderAddress = orderAddressService.getAllOrderAddress();

        model.addAttribute("addressList", allOrderAddress);
        return "order/addressManagement";
    }
}
