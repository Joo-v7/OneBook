package com.onebook.frontapi.controller.address;

import com.onebook.frontapi.dto.address.request.AddMemberAddressRequest;
import com.onebook.frontapi.dto.address.request.DeleteMemberAddressRequest;
import com.onebook.frontapi.dto.address.request.UpdateMemberAddressRequest;
import com.onebook.frontapi.dto.address.response.MemberAddressResponse;
import com.onebook.frontapi.service.address.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    // 배송지 등록 기능
    // 배송지 등록 페이지 불러오기
    @GetMapping("/addresses/register")
    public String getAddressRegisterForm(){

        addressService.checkAddressLimit();

        return "address/address-register-form";
    }

    // 배송지 등록하기
    @PostMapping("/addresses/register")
    public String registerAddress(@ModelAttribute AddMemberAddressRequest addMemberAddressRequest){

        addressService.addMemberAddress(addMemberAddressRequest);
        return "redirect:/addresses";
    }

    // 배송지 조회 기능
    @GetMapping("/addresses")
    public String getAddressAll(Model model){

        List<MemberAddressResponse> memberAddresses = addressService.getAllMemberAddressByMemberId();
        model.addAttribute("memberAddresses",memberAddresses);
        return "address/address-list";
    }

    // 배송지 수정 기능
    // 배송지 수정 폼 불러오기
    @GetMapping("/addresses/modify")
    public String getAddressModifyForm(Model model, @RequestParam Long id){

        MemberAddressResponse memberAddressResponse =
                addressService.getMemberAddressById(id);

        model.addAttribute("memberAddress",memberAddressResponse);
        return "address/address-modify-form";
    }

    // 배송지 수정 기능
    @PutMapping("/addresses/modify")
    public String updateAddress(@ModelAttribute UpdateMemberAddressRequest updateMemberAddressRequest){

        addressService.updateMemberAddress(updateMemberAddressRequest);
        return "redirect:/addresses";
    }

    // 배송지 삭제 기능
    @DeleteMapping("/addresses/delete")
    public String deleteAddress(@ModelAttribute DeleteMemberAddressRequest deleteMemberAddressRequest){

        addressService.deleteMemberAddress(deleteMemberAddressRequest);
        return "redirect:/addresses";
    }

    // 기본 배송지 설정 기능
    @PostMapping("/addresses/{address-id}/default")
    public String setDefaultAddress(@PathVariable(name = "address-id") Long memberAddressId){

        addressService.setDefaultAddress(memberAddressId);
        return "redirect:/addresses";
    }
}
