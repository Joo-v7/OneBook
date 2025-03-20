package com.onebook.frontapi.service.address;

import com.onebook.frontapi.dto.address.request.AddMemberAddressRequest;
import com.onebook.frontapi.dto.address.request.DeleteMemberAddressRequest;
import com.onebook.frontapi.dto.address.request.UpdateMemberAddressRequest;
import com.onebook.frontapi.dto.address.response.MemberAddressResponse;
import com.onebook.frontapi.exception.address.MemberAddressLimitExceededException;
import com.onebook.frontapi.feign.address.AddressClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressClient addressClient;

    public MemberAddressResponse addMemberAddress(AddMemberAddressRequest addMemberAddressRequest) {

        return addressClient.addMemberAddress(addMemberAddressRequest).getBody();
    }

    public MemberAddressResponse getMemberAddressById(Long id){
        return addressClient.getMemberAddressById(id).getBody();
    }

    public List<MemberAddressResponse> getAllMemberAddressByMemberId() {
        return addressClient.getAllMemberAddressByMemberId().getBody();
    }

    public MemberAddressResponse updateMemberAddress(UpdateMemberAddressRequest updateMemberAddressRequest){
        return addressClient.updateMemberAddressById(updateMemberAddressRequest).getBody();
    }

    public MemberAddressResponse deleteMemberAddress(DeleteMemberAddressRequest deleteMemberAddressRequest){
        return addressClient.deleteMemberAddress(deleteMemberAddressRequest).getBody();
    }

    public void checkAddressLimit() {
        Long addressesCount = addressClient.getAddressesCount().getBody();
        if(addressesCount >= 10L){
            throw new MemberAddressLimitExceededException("배송지는 최대 10개까지 등록 가능합니다.");
        }
    }

    // TODO 기본배송지 설정
    public MemberAddressResponse setDefaultAddress(Long memberAddressId){

        return addressClient.setDefaultAddress(memberAddressId).getBody();
    }

    // TODO 기본배송지 불러오기
}
