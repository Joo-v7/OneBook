package com.onebook.frontapi.service.order;

import com.onebook.frontapi.dto.order.OrderAddressResponseDto;
import com.onebook.frontapi.feign.address.AddressClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderAddressService {

    private final AddressClient addressClient;

    public List<OrderAddressResponseDto> getAllOrderAddress() {
        return addressClient.getAllMemberAddressByMemberId().getBody().stream().map(
                MemberAddressResponse -> new OrderAddressResponseDto(
                        MemberAddressResponse.getName(),
                        MemberAddressResponse.getPhoneNumber(),
                        MemberAddressResponse.getAlias(),
                        MemberAddressResponse.getRequestedTerm(),
                        MemberAddressResponse.getZipCode(),
                        MemberAddressResponse.getRoadNameAddress(),
                        MemberAddressResponse.getNumberAddress(),
                        MemberAddressResponse.getNotes(),
                        MemberAddressResponse.getDetailAddress(),
                        MemberAddressResponse.isDefaultLocation()
                )).toList();
    }
}
