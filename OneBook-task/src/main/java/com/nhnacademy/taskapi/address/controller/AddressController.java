package com.nhnacademy.taskapi.address.controller;

import com.nhnacademy.taskapi.address.domain.dto.req.AddMemberAddressRequest;
import com.nhnacademy.taskapi.address.domain.dto.req.DeleteMemberAddressRequest;
import com.nhnacademy.taskapi.address.domain.dto.req.UpdateMemberAddressRequest;
import com.nhnacademy.taskapi.address.domain.dto.resp.MemberAddressResponse;
import com.nhnacademy.taskapi.address.domain.entity.MemberAddress;
import com.nhnacademy.taskapi.address.exception.AddressIllegalArgumentException;
import com.nhnacademy.taskapi.address.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/task")
@Tag(name = "Address", description = "사용자의 배송지를 생성,수정,조회,삭제")  // API 그룹 설명 추가
public class AddressController {

    private final AddressService addressService;

    @PostMapping("/addresses")
    public ResponseEntity<MemberAddressResponse> addMemberAddress(@RequestHeader("X-MEMBER-ID") Long memberId ,
                                                                  @Valid @RequestBody AddMemberAddressRequest request,
                                                                  BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            throw new AddressIllegalArgumentException(bindingResult.getAllErrors().getFirst().getDefaultMessage());
        }

        MemberAddressResponse resp = addressService.addMemberAddress(memberId, request);
        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    @GetMapping("/addresses")
    public ResponseEntity<List<MemberAddressResponse>> getAllMemberAddressByMemberId(@RequestHeader("X-MEMBER-ID") Long memberId){

        List<MemberAddressResponse> resp = addressService.getMemberAddresses(memberId);
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<MemberAddressResponse> getMemberAddressById(@RequestHeader("X-MEMBER-ID") Long memberId, @PathVariable Long addressId ){

        MemberAddressResponse resp = addressService.getMemberAddress(memberId,addressId);
        return new ResponseEntity<>(resp,HttpStatus.OK);
    }
    
    @PutMapping("/addresses")
    public ResponseEntity<MemberAddressResponse> updateMemberAddress(@RequestHeader("X-MEMBER-ID") Long memberId
            ,@Valid @RequestBody UpdateMemberAddressRequest request, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            throw new AddressIllegalArgumentException(bindingResult.getAllErrors().getFirst().getDefaultMessage());
        }
        MemberAddressResponse resp = addressService.updateMemberAddress(memberId, request);
        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    @DeleteMapping("/addresses")
    public ResponseEntity<MemberAddressResponse> deleteMemberAddress(@RequestHeader("X-MEMBER-ID") Long memberId
            ,@Valid @RequestBody DeleteMemberAddressRequest request, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            throw new AddressIllegalArgumentException(bindingResult.getAllErrors().getFirst().getDefaultMessage());
        }

        MemberAddressResponse resp = addressService.deleteMemberAddress(memberId,request);
        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    @GetMapping("/addresses/count")
    public ResponseEntity<Long> getAddressesCount(@RequestHeader("X-MEMBER-ID") Long memberId){

        Long addressCount = addressService.getMemberAddressesCount(memberId);
        return ResponseEntity.ok(addressCount);
    }

    @PostMapping("/addresses/{address-id}/default")
    public ResponseEntity<MemberAddressResponse> setDefaultAddress(@RequestHeader("X-MEMBER-ID") Long memberId
            ,@PathVariable(name = "address-id") Long memberAddressId){

        MemberAddressResponse memberAddressResponse = addressService.setDefaultAddress(memberId,memberAddressId);

        return ResponseEntity.ok(memberAddressResponse);
    }

}
