package com.onebook.frontapi.dto.address.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddMemberAddressRequest {

    private String name;
    private String phoneNumber;
    private String alias;
    private String requestedTerm;
    private String zipCode;
    private String roadNameAddress;
    private String numberAddress;
    private String notes;
    private String detailAddress;
    private boolean defaultLocation;
}
