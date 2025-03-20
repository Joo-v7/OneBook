package com.onebook.frontapi.feign.address;

import com.onebook.frontapi.dto.address.request.AddMemberAddressRequest;
import com.onebook.frontapi.dto.address.request.DeleteMemberAddressRequest;
import com.onebook.frontapi.dto.address.request.UpdateMemberAddressRequest;
import com.onebook.frontapi.dto.address.response.MemberAddressResponse;
import com.onebook.frontapi.dto.category.CategoryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "AddressClient",  url = "${onebook.gatewayUrl}")
public interface AddressClient {

    @PostMapping("/task/addresses")
    ResponseEntity<MemberAddressResponse> addMemberAddress
            (@RequestBody AddMemberAddressRequest addMemberAddressRequest);

    @GetMapping("/task/addresses")
    ResponseEntity<List<MemberAddressResponse>> getAllMemberAddressByMemberId();

    @GetMapping("/task/addresses/{addressId}")
    ResponseEntity<MemberAddressResponse> getMemberAddressById(@PathVariable Long addressId);

    @PutMapping("/task/addresses")
    ResponseEntity<MemberAddressResponse> updateMemberAddressById
            (@RequestBody UpdateMemberAddressRequest updateMemberAddressRequest);

    @DeleteMapping("/task/addresses")
    ResponseEntity<MemberAddressResponse> deleteMemberAddress
            (@RequestBody DeleteMemberAddressRequest deleteMemberAddressRequest);

    @GetMapping("/task/addresses/count")
    ResponseEntity<Long> getAddressesCount();

    // TODO 기본배송지 설정
    @PostMapping("/task/addresses/{address-id}/default")
    ResponseEntity<MemberAddressResponse> setDefaultAddress(@PathVariable(name = "address-id") Long memberAddressId);


}
