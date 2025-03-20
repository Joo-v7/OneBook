package com.nhnacademy.taskapi.address.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.taskapi.address.domain.dto.req.AddMemberAddressRequest;
import com.nhnacademy.taskapi.address.domain.dto.req.DeleteMemberAddressRequest;
import com.nhnacademy.taskapi.address.domain.dto.req.UpdateMemberAddressRequest;
import com.nhnacademy.taskapi.address.domain.dto.resp.MemberAddressResponse;
import com.nhnacademy.taskapi.address.domain.entity.MemberAddress;
import com.nhnacademy.taskapi.address.service.AddressService;
import com.nhnacademy.taskapi.grade.domain.Grade;
import com.nhnacademy.taskapi.member.domain.Member;
import com.nhnacademy.taskapi.roles.domain.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AddressController.class)
@MockBean(JpaMetamodelMappingContext.class)
class AddressControllerTest {

    @MockBean
    private AddressService addressService;

    private int port = 8080;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private Member member;
    private AddMemberAddressRequest addMemberAddressRequest;
    private UpdateMemberAddressRequest updateMemberAddressRequest;
    private DeleteMemberAddressRequest deleteMemberAddressRequest;
    private MemberAddress memberAddress;
    private MemberAddressResponse addMemberAddressResponse;
    private MemberAddressResponse getMemberAddressResponse;
    private List<MemberAddressResponse> getMemberAddressResponseList;
    private MemberAddressResponse updateMemberAddressResponse;
    private MemberAddressResponse deleteMemberAddressResponse;

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
                "01022222222",
                Role.createRole("테스트 권한","테스트 권한")
        );

        // 테스트용 AddMemberAddressRequest
        addMemberAddressRequest = new AddMemberAddressRequest();

        for(Field field : addMemberAddressRequest.getClass().getDeclaredFields()){
            field.setAccessible(true);
        }

        try {
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("name"),
                    addMemberAddressRequest,"테스트");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("phoneNumber"),
                    addMemberAddressRequest,"01099999999");
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
                    addMemberAddressRequest,true);

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
                    updateMemberAddressRequest,"01033333333" );
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
                    updateMemberAddressRequest,true );
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

        // 테스트용 AddMemberResponse
        addMemberAddressResponse = MemberAddressResponse.changeEntityToDto(memberAddress);

        // 테스트용 GetMemberResponse
        getMemberAddressResponse = MemberAddressResponse.changeEntityToDto(memberAddress);

        // 테스트용 GetMemberResponses
        getMemberAddressResponseList = List.of(getMemberAddressResponse);

        // 테스트용 UpdateMemberResponse
        memberAddress.updateMemberAddress(updateMemberAddressRequest);
        updateMemberAddressResponse = MemberAddressResponse.changeEntityToDto(memberAddress);

        // 테스트용 DeleteMemberResponse
        deleteMemberAddressResponse = MemberAddressResponse.changeEntityToDto(memberAddress);
    }

    @Test
    @Order(1)
    @DisplayName("AddressController의 addMemberAddress 동작 테스트")
    public void addMemberAddressTest() throws Exception {

       Mockito.when(addressService.addMemberAddress(Mockito.anyLong(),Mockito.any(AddMemberAddressRequest.class)))
            .thenReturn(addMemberAddressResponse);

        String req = objectMapper.writeValueAsString(addMemberAddressRequest);

        mockMvc.perform(post("http://localhost:"+ port +"/task/addresses")
                .header("X-MEMBER-ID",1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(req))
                .andExpect(jsonPath("$.name").value("테스트"))
                .andExpect(jsonPath("$.phoneNumber").value("01099999999"))
                .andExpect(jsonPath("$.alias").value("테스트"))
                .andExpect(jsonPath("$.requestedTerm").value("테스트용입니다"))
                .andExpect(jsonPath("$.zipCode").value("99999"))
                .andExpect(jsonPath("$.roadNameAddress").value("테스트"))
                .andExpect(jsonPath("$.numberAddress").value("테스트"))
                .andExpect(jsonPath("$.notes").value("(테스트)"))
                .andExpect(jsonPath("$.detailAddress").value("테스트"))
                .andExpect(jsonPath("$.defaultLocation").value(true));
    }

    @Test
    @Order(2)
    @DisplayName("AddressController의 getAllMemberAddressByMemberId 동작 테스트")
    public void getAllMemberAddressByMemberIdTest() throws Exception {

        Mockito.when(addressService.getMemberAddresses(Mockito.anyLong()))
                .thenReturn(getMemberAddressResponseList);

        mockMvc.perform(get("http://localhost:"+ port +"/task/addresses")
                        .header("X-MEMBER-ID",1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("테스트"))
                .andExpect(jsonPath("$[0].phoneNumber").value("01099999999"))
                .andExpect(jsonPath("$[0].alias").value("테스트"))
                .andExpect(jsonPath("$[0].requestedTerm").value("테스트용입니다"))
                .andExpect(jsonPath("$[0].zipCode").value("99999"))
                .andExpect(jsonPath("$[0].roadNameAddress").value("테스트"))
                .andExpect(jsonPath("$[0].numberAddress").value("테스트"))
                .andExpect(jsonPath("$[0].notes").value("(테스트)"))
                .andExpect(jsonPath("$[0].detailAddress").value("테스트"))
                .andExpect(jsonPath("$[0].defaultLocation").value(true));
    }

    @Test
    @Order(3)
    @DisplayName("AddressController의 getMemberAddressById 동작 테스트")
    public void getMemberAddressByIdTest() throws Exception {

        Mockito.when(addressService.getMemberAddress(Mockito.anyLong(),Mockito.anyLong()))
                .thenReturn(getMemberAddressResponse);
        String pathVariable = "1";

        mockMvc.perform(get("http://localhost:"+ port +"/task/addresses/"+pathVariable)
                        .header("X-MEMBER-ID",1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("테스트"))
                .andExpect(jsonPath("$.phoneNumber").value("01099999999"))
                .andExpect(jsonPath("$.alias").value("테스트"))
                .andExpect(jsonPath("$.requestedTerm").value("테스트용입니다"))
                .andExpect(jsonPath("$.zipCode").value("99999"))
                .andExpect(jsonPath("$.roadNameAddress").value("테스트"))
                .andExpect(jsonPath("$.numberAddress").value("테스트"))
                .andExpect(jsonPath("$.notes").value("(테스트)"))
                .andExpect(jsonPath("$.detailAddress").value("테스트"))
                .andExpect(jsonPath("$.defaultLocation").value(true));
    }

    @Test
    @Order(4)
    @DisplayName("AddressController의 updateMemberAddress 동작 테스트")
    public void updateMemberAddressTest() throws Exception {

        Mockito.when(addressService.updateMemberAddress(Mockito.anyLong(),Mockito.any(UpdateMemberAddressRequest.class)))
                .thenReturn(updateMemberAddressResponse);

        String req = objectMapper.writeValueAsString(updateMemberAddressRequest);

        mockMvc.perform(put("http://localhost:"+ port +"/task/addresses")
                        .header("X-MEMBER-ID",1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(req)
                )
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("수정 테스트"))
                .andExpect(jsonPath("$.phoneNumber").value("01033333333"))
                .andExpect(jsonPath("$.alias").value("수정 테스트"))
                .andExpect(jsonPath("$.requestedTerm").value("수정 테스트"))
                .andExpect(jsonPath("$.zipCode").value("21121"))
                .andExpect(jsonPath("$.roadNameAddress").value("수정 테스트"))
                .andExpect(jsonPath("$.numberAddress").value("수정 테스트"))
                .andExpect(jsonPath("$.notes").value("수정 테스트"))
                .andExpect(jsonPath("$.detailAddress").value("수정 테스트"))
                .andExpect(jsonPath("$.defaultLocation").value(true));
    }

    @Test
    @Order(5)
    @DisplayName("AddressController의 deleteMemberAddress 동작 테스트")
    public void deleteMemberAddressTest() throws Exception {

        Mockito.when(addressService.deleteMemberAddress(Mockito.anyLong(),Mockito.any())).
                thenReturn(deleteMemberAddressResponse);

        String req = objectMapper.writeValueAsString(deleteMemberAddressRequest);

        mockMvc.perform(delete("http://localhost:"+ port +"/task/addresses")
                        .header("X-MEMBER-ID",1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(req)
                )
                .andExpect(jsonPath("$.id").value(1L));
    }
}