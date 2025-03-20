package com.nhnacademy.taskapi.address.service;

import com.nhnacademy.taskapi.address.domain.dto.req.AddMemberAddressRequest;
import com.nhnacademy.taskapi.address.domain.dto.req.DeleteMemberAddressRequest;
import com.nhnacademy.taskapi.address.domain.dto.req.UpdateMemberAddressRequest;
import com.nhnacademy.taskapi.address.domain.entity.MemberAddress;
import com.nhnacademy.taskapi.address.exception.InvalidMemberAddressException;
import com.nhnacademy.taskapi.address.exception.MemberAddressNotFoundException;
import com.nhnacademy.taskapi.address.repository.AddressRepository;
import com.nhnacademy.taskapi.grade.domain.Grade;
import com.nhnacademy.taskapi.member.domain.Member;
import com.nhnacademy.taskapi.member.repository.MemberRepository;
import com.nhnacademy.taskapi.member.service.MemberService;
import com.nhnacademy.taskapi.roles.domain.Role;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @Mock
    private MemberService memberService;

    /**
     * 수정일: 2024/12/31
     * 수정자: 김주혁
     * 수정 내용: memberRepository 추가.
     */
    @Mock
    private MemberRepository memberRepository;

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressService addressService;

    Member member;
    MemberAddress memberAddress;
    AddMemberAddressRequest addMemberAddressRequest;
    UpdateMemberAddressRequest updateMemberAddressRequest;
    DeleteMemberAddressRequest deleteMemberAddressRequest;

    @BeforeEach
    void setUp(){
        // 테스트용 멤버
       member = Member.createNewMember(
                Grade.create("테스트 등급",1,"테스트 등급"),
                "테스트",
                "testId",
                "testPassword",
                LocalDate.now(),
                Member.Gender.M,
                "test123@nhn.com",
                "010-2222-2222",
                Role.createRole("테스트 권한","테스트 권한")
        );

        try {
            member.getClass().getDeclaredField("id").setAccessible(true);
            ReflectionUtils.setField(member.getClass().getDeclaredField("id"),member,1L );
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        // 테스트용 AddMemberAddressRequest
        addMemberAddressRequest = new AddMemberAddressRequest();

        for(Field field : addMemberAddressRequest.getClass().getDeclaredFields()){
            field.setAccessible(true);
        }

        try {
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("name"),
                    addMemberAddressRequest,"테스트");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("phoneNumber"),
                    addMemberAddressRequest,"010-9999-9999");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("alias"),
                    addMemberAddressRequest,"테스트");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("requestedTerm"),
                    addMemberAddressRequest,"테스트용입니다");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("zipCode"),
                    addMemberAddressRequest,"99999");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("roadNameAddress"),
                    addMemberAddressRequest,"테스트");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("numberAddress"),
                    addMemberAddressRequest,"테스트");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("notes"),
                    addMemberAddressRequest,"(테스트)");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("detailAddress"),
                    addMemberAddressRequest,"테스트");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("defaultLocation"),
                    addMemberAddressRequest,false);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        };

        // 테스트용 UpdateMemberAddressRequest
        updateMemberAddressRequest = new UpdateMemberAddressRequest();

        for(Field field : updateMemberAddressRequest.getClass().getDeclaredFields()){
            field.setAccessible(true);
        }

        try {
            ReflectionUtils.setField(updateMemberAddressRequest.getClass().getDeclaredField("id"),
                    updateMemberAddressRequest,1L );
            ReflectionUtils.setField(updateMemberAddressRequest.getClass().getDeclaredField("name"),
                    updateMemberAddressRequest,"수정 테스트" );
            ReflectionUtils.setField(updateMemberAddressRequest.getClass().getDeclaredField("phoneNumber"),
                    updateMemberAddressRequest,"010-3333-3333" );
            ReflectionUtils.setField(updateMemberAddressRequest.getClass().getDeclaredField("alias"),
                    updateMemberAddressRequest,"수정 테스트" );
            ReflectionUtils.setField(updateMemberAddressRequest.getClass().getDeclaredField("requestedTerm"),
                    updateMemberAddressRequest,"수정 테스트" );
            ReflectionUtils.setField(updateMemberAddressRequest.getClass().getDeclaredField("zipCode"),
                    updateMemberAddressRequest,"21121" );
            ReflectionUtils.setField(updateMemberAddressRequest.getClass().getDeclaredField("roadNameAddress"),
                    updateMemberAddressRequest,"수정 테스트" );
            ReflectionUtils.setField(updateMemberAddressRequest.getClass().getDeclaredField("numberAddress"),
                    updateMemberAddressRequest,"수정 테스트" );
            ReflectionUtils.setField(updateMemberAddressRequest.getClass().getDeclaredField("notes"),
                    updateMemberAddressRequest,"수정 테스트" );
            ReflectionUtils.setField(updateMemberAddressRequest.getClass().getDeclaredField("detailAddress"),
                    updateMemberAddressRequest,"수정 테스트" );
            ReflectionUtils.setField(updateMemberAddressRequest.getClass().getDeclaredField("defaultLocation"),
                    updateMemberAddressRequest,false );
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        // 테스트용 DeleteMemberAddressRequest
        deleteMemberAddressRequest = new DeleteMemberAddressRequest();
        try {
            deleteMemberAddressRequest.getClass().getDeclaredField("id").setAccessible(true);
            ReflectionUtils.setField(deleteMemberAddressRequest.getClass().getDeclaredField("id"),
                    deleteMemberAddressRequest, 1L);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        // 테스트용 memberAddress
        memberAddress = MemberAddress.createMemberAddress(
                member,
                addMemberAddressRequest
        );

        try {
            memberAddress.getClass().getDeclaredField("id").setAccessible(true);
            ReflectionUtils.setField(memberAddress.getClass().getDeclaredField("id"),memberAddress,1L);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

    }


    @Test
    @DisplayName("addMemberAddress 메서드 동작 테스트")
    @Order(1)
    void addMemberAddressTest(){
        /**
         * 수정일: 2024/12/31
         * 수정자: 김주혁
         * 수정 내용
         *  1. 기존 내용 주석처리
         *  2. memberRepository로 member return.
         *  3. Mockito.verify를 memberService -> memberRepository로 변경.
         */
        Mockito.when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
//        Mockito.when(memberService.getMemberById(member.getId())).thenReturn(member);
        addressService.addMemberAddress(1L, addMemberAddressRequest);
//        Mockito.verify(memberService, Mockito.times(1)).getMemberById(member.getId());
        Mockito.verify(memberRepository, Mockito.times(3)).findById(member.getId());

    }

    @Test
    @DisplayName("getMemberAddress 메서드 동작 테스트")
    @Order(2)
    void getMemberAddressTest() {

        Mockito.when(addressRepository.findById(memberAddress.getId())).thenReturn(Optional.of(memberAddress));

        addressService.getMemberAddress(memberAddress.getId(), memberAddress.getId());

        Mockito.verify(addressRepository,Mockito.times(1)).findById(memberAddress.getId());

    }

    @Test
    @DisplayName("getMemberAddress 메서드 예외 테스트 - 해당하는 ID의 배송지가 존재하지 않을때")
    @Order(3)
    void getMemberAddressMemberAddressNotFoundExceptionTest() {

        Mockito.when(addressRepository.findById(999L)).thenReturn(Optional.empty());

        Assertions.assertThrows(MemberAddressNotFoundException.class,()->{
            addressService.getMemberAddress(memberAddress.getId(), 999L);
        });

    }

    @Test
    @DisplayName("getMemberAddress 메서드 예외 테스트 - 해당하는 ID의 member ID와 요청한 member ID가 일치하지 않을때")
    @Order(4)
    void getMemberAddressMemberInvalidMemberAddressExceptionTest() {

        Mockito.when(addressRepository.findById(memberAddress.getId())).thenReturn(Optional.of(memberAddress));

        Assertions.assertThrows(InvalidMemberAddressException.class,()->{
            addressService.getMemberAddress(999L,memberAddress.getId());
        });

    }

    @Test
    @DisplayName("getMemberAddresses 메서드 동작 테스트")
    @Order(5)
    void getMemberAddressesTest() {

        /**
         * 수정일: 2024/12/31
         * 수정자: 김주혁
         * 수정 내용: 기존 내용 주석처리, memberRepository로 member return.
         */
        Mockito.when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
//        Mockito.when(memberService.getMemberById(member.getId())).thenReturn(member);
        Mockito.when(addressRepository.findMemberAddressByMember(member)).thenReturn(List.of(memberAddress));

        addressService.getMemberAddresses(member.getId());

        Mockito.verify(addressRepository,Mockito.times(1)).findMemberAddressByMember(member);

    }

//    @Test
//    @DisplayName("updateMemberAddress 메서드 동작 테스트")
//    @Order(6)
//    void updateMemberAddressTest() {
//
//        Mockito.when(addressRepository.findById(memberAddress.getId())).thenReturn(Optional.of(memberAddress));
//
//        addressService.updateMemberAddress(member.getId(), updateMemberAddressRequest);
//
//        Mockito.verify(addressRepository,Mockito.times(1)).findById(memberAddress.getId());
//
//    }

    @Test
    @DisplayName("updateMemberAddress 메서드 예외 테스트 - 해당하는 ID의 배송지가 존재하지 않을때")
    @Order(7)
    void updateMemberAddressMemberAddressNotFoundExceptionTest() {

        Mockito.when(addressRepository.findById(999L)).thenReturn(Optional.empty());

        try {
            updateMemberAddressRequest.getClass().getDeclaredField("id").setAccessible(true);
            ReflectionUtils.setField(updateMemberAddressRequest.getClass().getDeclaredField("id"),
                    updateMemberAddressRequest,999L);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        Assertions.assertThrows(MemberAddressNotFoundException.class,()->{
            addressService.updateMemberAddress(member.getId(), updateMemberAddressRequest);
        });

    }

    @Test
    @DisplayName("updateMemberAddress 메서드 예외 테스트 - 해당하는 ID의 member ID와 요청한 member ID가 일치하지 않을때")
    @Order(8)
    void updateMemberAddressInvalidMemberAddressExceptionTest() {

        Mockito.when(addressRepository.findById(memberAddress.getId())).thenReturn(Optional.of(memberAddress));

        try {
            updateMemberAddressRequest.getClass().getDeclaredField("id").setAccessible(true);
            ReflectionUtils.setField(updateMemberAddressRequest.getClass().getDeclaredField("id"),
                    updateMemberAddressRequest,memberAddress.getId());
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        Assertions.assertThrows(InvalidMemberAddressException.class,()->{
            addressService.updateMemberAddress(999L, updateMemberAddressRequest);
        });

    }

    @Test
    @DisplayName("deleteMemberAddress 메서드 동작 테스트")
    @Order(9)
    void deleteMemberAddressTest() {

        Mockito.when(addressRepository.findById(memberAddress.getId())).thenReturn(Optional.of(memberAddress));
        Mockito.doNothing().when(addressRepository).delete(memberAddress);

        addressService.deleteMemberAddress(member.getId(),deleteMemberAddressRequest);

        Mockito.verify(addressRepository,Mockito.times(1)).findById(memberAddress.getId());
        Mockito.verify(addressRepository,Mockito.times(1)).delete(memberAddress);

    }

    @Test
    @DisplayName("deleteMemberAddress 메서드 예외 테스트 - 해당하는 ID의 배송지가 존재하지 않을때")
    @Order(10)
    void deleteMemberAddressMemberAddressNotFoundExceptionTest() {

        Mockito.when(addressRepository.findById(999L)).thenReturn(Optional.empty());

        try {
            deleteMemberAddressRequest.getClass().getDeclaredField("id").setAccessible(true);
            ReflectionUtils.setField(deleteMemberAddressRequest.getClass().getDeclaredField("id"),
                    deleteMemberAddressRequest,999L );
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        Assertions.assertThrows(MemberAddressNotFoundException.class,()->{
            addressService.deleteMemberAddress(member.getId(),deleteMemberAddressRequest);
        });
    }

    @Test
    @DisplayName("deleteMemberAddress 메서드 예외 테스트 - 해당하는 ID의 member ID와 요청한 member ID가 일치하지 않을때")
    @Order(11)
    void deleteMemberAddressInvalidMemberAddressExceptionTest() {

        Mockito.when(addressRepository.findById(memberAddress.getId())).thenReturn(Optional.of(memberAddress));

        Assertions.assertThrows(InvalidMemberAddressException.class,()->{
            addressService.deleteMemberAddress(999L, deleteMemberAddressRequest);
        });
    }



}
