package com.nhnacademy.taskapi.address.domain.entity;

import com.nhnacademy.taskapi.address.domain.dto.req.AddMemberAddressRequest;
import com.nhnacademy.taskapi.address.domain.dto.req.UpdateMemberAddressRequest;
import com.nhnacademy.taskapi.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
public class MemberAddress {

    private MemberAddress(Member member, String name, String phoneNumber, String alias, String requestedTerm,
                         String zipCode, String roadNameAddress, String numberAddress, String notes, String detailAddress,
                         boolean defaultLocation) {

        this.member = member;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.alias = alias;
        this.requestedTerm = requestedTerm;
        this.zipCode = zipCode;
        this.roadNameAddress = roadNameAddress;
        this.numberAddress = numberAddress;
        this.notes = notes;
        this.detailAddress = detailAddress;
        this.defaultLocation = defaultLocation;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_address_id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false ,length = 10)
    private String name;

    @Column(nullable = false, length = 30)
    private String phoneNumber;

    @Column(length = 20)
    private String alias;

    @Column(length = 100)
    private String requestedTerm;

    @Column(nullable = false,length = 10)
    private String zipCode;

    @Column(nullable = false,length = 100)
    private String roadNameAddress;

    @Column(nullable = false,length = 100)
    private String numberAddress;

    @Column(length = 100)
    private String notes;

    @Column(length = 100)
    private String detailAddress;

    @Column(nullable = false)
    private boolean defaultLocation;

    public static MemberAddress createMemberAddress(Member member, AddMemberAddressRequest addMemberAddressRequest){
        return new MemberAddress(
                member,
                addMemberAddressRequest.getName(),
                addMemberAddressRequest.getPhoneNumber(),
                addMemberAddressRequest.getAlias(),
                addMemberAddressRequest.getRequestedTerm(),
                addMemberAddressRequest.getZipCode(),
                addMemberAddressRequest.getRoadNameAddress(),
                addMemberAddressRequest.getNumberAddress(),
                addMemberAddressRequest.getNotes(),
                addMemberAddressRequest.getDetailAddress(),
                addMemberAddressRequest.isDefaultLocation()
        );
    }

    public void updateMemberAddress(UpdateMemberAddressRequest updateMemberAddressRequest) {

        this.name = updateMemberAddressRequest.getName();
        this.phoneNumber = updateMemberAddressRequest.getPhoneNumber();
        this.alias = updateMemberAddressRequest.getAlias();
        this.requestedTerm = updateMemberAddressRequest.getRequestedTerm();
        this.zipCode = updateMemberAddressRequest.getZipCode();
        this.roadNameAddress = updateMemberAddressRequest.getRoadNameAddress();
        this.numberAddress = updateMemberAddressRequest.getNumberAddress();
        this.notes = updateMemberAddressRequest.getNotes();
        this.detailAddress = updateMemberAddressRequest.getDetailAddress();
        this.defaultLocation = updateMemberAddressRequest.isDefaultLocation();
    }

    public void setDefaultLocation(){
        this.defaultLocation = true;
    }

    public void unsetDefaultLocation(){
        this.defaultLocation =false;
    }
}
