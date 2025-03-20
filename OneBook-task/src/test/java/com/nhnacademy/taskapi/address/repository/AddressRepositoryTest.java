package com.nhnacademy.taskapi.address.repository;

import com.nhnacademy.taskapi.address.domain.dto.req.AddMemberAddressRequest;
import com.nhnacademy.taskapi.address.domain.dto.req.UpdateMemberAddressRequest;
import com.nhnacademy.taskapi.address.domain.entity.MemberAddress;
import com.nhnacademy.taskapi.config.QuerydslConfig;
import com.nhnacademy.taskapi.grade.domain.Grade;
import com.nhnacademy.taskapi.member.domain.Member;
import com.nhnacademy.taskapi.roles.domain.Role;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;

@DataJpaTest
@Import(QuerydslConfig.class)
class AddressRepositoryTest {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    @DisplayName("MemberAddress Repository save 테스트")
    public void saveMemberAddressTest(){

        Grade grade = Grade.create("실버",1,"실버등급");
        Role role = Role.createRole("사용자","사용자입니다");
        Member member = Member.createNewMember(
                grade,
                "김갑수",
                "gapsoo01",
                "1234",
                LocalDate.parse("1992-01-01"),
                Member.Gender.M,
                "gapsoozzang@nhn.com",
                "010-1234-5678",
                role
        );

        entityManager.persist(role);
        entityManager.persist(grade);
        entityManager.persist(member);

        AddMemberAddressRequest addMemberAddressRequest = new AddMemberAddressRequest();

        for(Field field : addMemberAddressRequest.getClass().getDeclaredFields()){
            field.setAccessible(true);
        }

        try {
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("name"),
                    addMemberAddressRequest,"이순신");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("phoneNumber"),
                    addMemberAddressRequest,"010-9999-9999");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("alias"),
                    addMemberAddressRequest,"거북선");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("requestedTerm"),
                    addMemberAddressRequest,"돌격 앞으로");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("zipCode"),
                    addMemberAddressRequest,"999-999");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("roadNameAddress"),
                    addMemberAddressRequest,"한산도");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("numberAddress"),
                    addMemberAddressRequest,"명량");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("notes"),
                    addMemberAddressRequest,"(전라남도 좌수사)");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("detailAddress"),
                    addMemberAddressRequest,"상세주소");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("defaultLocation"),
                    addMemberAddressRequest,true);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }


        MemberAddress memberAddress = MemberAddress.createMemberAddress(member,addMemberAddressRequest);

        addressRepository.save(memberAddress);

    }



    @Test
    @DisplayName("MemberAddress Repository readByAddressId 테스트")
    public void readMemberAddressByIdTest(){

        Grade grade = Grade.create("실버",1,"실버등급");
        Role role = Role.createRole("사용자","사용자입니다");
        Member member = Member.createNewMember(
                grade,
                "김갑수",
                "gapsoo01",
                "1234",
                LocalDate.parse("1992-01-01"),
                Member.Gender.M,
                "gapsoozzang@nhn.com",
                "010-1234-5678",
                role
        );

        entityManager.persist(role);
        entityManager.persist(grade);
        entityManager.persist(member);

        AddMemberAddressRequest addMemberAddressRequest = new AddMemberAddressRequest();

        for(Field field : addMemberAddressRequest.getClass().getDeclaredFields()){
            field.setAccessible(true);
        }

        try {
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("name"),
                    addMemberAddressRequest,"이순신");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("phoneNumber"),
                    addMemberAddressRequest,"010-9999-9999");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("alias"),
                    addMemberAddressRequest,"거북선");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("requestedTerm"),
                    addMemberAddressRequest,"돌격 앞으로");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("zipCode"),
                    addMemberAddressRequest,"999-999");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("roadNameAddress"),
                    addMemberAddressRequest,"한산도");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("numberAddress"),
                    addMemberAddressRequest,"명량");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("notes"),
                    addMemberAddressRequest,"(전라남도 좌수사)");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("detailAddress"),
                    addMemberAddressRequest,"상세주소");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("defaultLocation"),
                    addMemberAddressRequest,true);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }


        MemberAddress memberAddress = MemberAddress.createMemberAddress(member,addMemberAddressRequest);
        addressRepository.save(memberAddress);

        addressRepository.findById(memberAddress.getId());

    }

    @Test
    @DisplayName("MemberAddress Repository readByMemberId 테스트")
    public void readMemberAddressByMemberIdTest(){

        Grade grade = Grade.create("실버",1,"실버등급");
        Role role = Role.createRole("사용자","사용자입니다");
        Member member = Member.createNewMember(
                grade,
                "김갑수",
                "gapsoo01",
                "1234",
                LocalDate.parse("1992-01-01"),
                Member.Gender.M,
                "gapsoozzang@nhn.com",
                "010-1234-5678",
                role
        );

        entityManager.persist(role);
        entityManager.persist(grade);
        entityManager.persist(member);

        AddMemberAddressRequest addMemberAddressRequest = new AddMemberAddressRequest();

        for(Field field : addMemberAddressRequest.getClass().getDeclaredFields()){
            field.setAccessible(true);
        }

        try {
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("name"),
                    addMemberAddressRequest,"이순신");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("phoneNumber"),
                    addMemberAddressRequest,"010-9999-9999");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("alias"),
                    addMemberAddressRequest,"거북선");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("requestedTerm"),
                    addMemberAddressRequest,"돌격 앞으로");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("zipCode"),
                    addMemberAddressRequest,"999-999");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("roadNameAddress"),
                    addMemberAddressRequest,"한산도");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("numberAddress"),
                    addMemberAddressRequest,"명량");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("notes"),
                    addMemberAddressRequest,"(전라남도 좌수사)");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("detailAddress"),
                    addMemberAddressRequest,"상세주소");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("defaultLocation"),
                    addMemberAddressRequest,true);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }


        MemberAddress memberAddress = MemberAddress.createMemberAddress(member,addMemberAddressRequest);
        addressRepository.save(memberAddress);

        addressRepository.findMemberAddressByMember(member);

    }




    @Test
    @DisplayName("MemberAddress Repository update 테스트")
    public void updateMemberAddressTest(){

        Grade grade = Grade.create("실버",1,"실버등급");
        Role role = Role.createRole("사용자","사용자입니다");
        Member member = Member.createNewMember(
                grade,
                "김갑수",
                "gapsoo01",
                "1234",
                LocalDate.parse("1992-01-01"),
                Member.Gender.M,
                "gapsoozzang@nhn.com",
                "010-1234-5678",
                role
        );

        entityManager.persist(role);
        entityManager.persist(grade);
        entityManager.persist(member);

        AddMemberAddressRequest addMemberAddressRequest = new AddMemberAddressRequest();

        for(Field field : addMemberAddressRequest.getClass().getDeclaredFields()){
            field.setAccessible(true);
        }

        try {
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("name"),
                    addMemberAddressRequest,"이순신");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("phoneNumber"),
                    addMemberAddressRequest,"010-9999-9999");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("alias"),
                    addMemberAddressRequest,"거북선");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("requestedTerm"),
                    addMemberAddressRequest,"돌격 앞으로");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("zipCode"),
                    addMemberAddressRequest,"999-999");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("roadNameAddress"),
                    addMemberAddressRequest,"한산도");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("numberAddress"),
                    addMemberAddressRequest,"명량");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("notes"),
                    addMemberAddressRequest,"(전라남도 좌수사)");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("detailAddress"),
                    addMemberAddressRequest,"상세주소");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("defaultLocation"),
                    addMemberAddressRequest,true);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        MemberAddress memberAddress = MemberAddress.createMemberAddress(member,addMemberAddressRequest);
        addressRepository.save(memberAddress);

        MemberAddress memberAddressForUpdate = addressRepository.findById(memberAddress.getId()).get();
        UpdateMemberAddressRequest updateMemberAddressRequest = new UpdateMemberAddressRequest();

        for(Field field : updateMemberAddressRequest.getClass().getDeclaredFields()){
            field.setAccessible(true);
        }

        try {
            ReflectionUtils.setField(updateMemberAddressRequest.getClass().getDeclaredField("id"),
                    updateMemberAddressRequest,0L );
            ReflectionUtils.setField(updateMemberAddressRequest.getClass().getDeclaredField("name"),
                    updateMemberAddressRequest,"세종대왕" );
            ReflectionUtils.setField(updateMemberAddressRequest.getClass().getDeclaredField("phoneNumber"),
                    updateMemberAddressRequest,"010-7777-7777" );
            ReflectionUtils.setField(updateMemberAddressRequest.getClass().getDeclaredField("alias"),
                    updateMemberAddressRequest,"집현전" );
            ReflectionUtils.setField(updateMemberAddressRequest.getClass().getDeclaredField("requestedTerm"),
                    updateMemberAddressRequest,"나랏말싸미" );
            ReflectionUtils.setField(updateMemberAddressRequest.getClass().getDeclaredField("zipCode"),
                    updateMemberAddressRequest,"000-000" );
            ReflectionUtils.setField(updateMemberAddressRequest.getClass().getDeclaredField("roadNameAddress"),
                    updateMemberAddressRequest,"가나다라" );
            ReflectionUtils.setField(updateMemberAddressRequest.getClass().getDeclaredField("numberAddress"),
                    updateMemberAddressRequest,"마바사아" );
            ReflectionUtils.setField(updateMemberAddressRequest.getClass().getDeclaredField("notes"),
                    updateMemberAddressRequest,"(기타)" );
            ReflectionUtils.setField(updateMemberAddressRequest.getClass().getDeclaredField("detailAddress"),
                    updateMemberAddressRequest,"상세주소" );
            ReflectionUtils.setField(updateMemberAddressRequest.getClass().getDeclaredField("defaultLocation"),
                    updateMemberAddressRequest,true );
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        memberAddressForUpdate.updateMemberAddress(updateMemberAddressRequest);
    }

    @Test
    @DisplayName("MemberAddress Repository delete 테스트")
    public void deleteMemberAddressTest(){

        Grade grade = Grade.create("실버",1,"실버등급");
        Role role = Role.createRole("사용자","사용자입니다");
        Member member = Member.createNewMember(
                grade,
                "김갑수",
                "gapsoo01",
                "1234",
                LocalDate.parse("1992-01-01"),
                Member.Gender.M,
                "gapsoozzang@nhn.com",
                "010-1234-5678",
                role
        );

        entityManager.persist(role);
        entityManager.persist(grade);
        entityManager.persist(member);

        AddMemberAddressRequest addMemberAddressRequest = new AddMemberAddressRequest();

        for(Field field : addMemberAddressRequest.getClass().getDeclaredFields()){
            field.setAccessible(true);
        }

        try {
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("name"),
                    addMemberAddressRequest,"이순신");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("phoneNumber"),
                    addMemberAddressRequest,"010-9999-9999");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("alias"),
                    addMemberAddressRequest,"거북선");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("requestedTerm"),
                    addMemberAddressRequest,"돌격 앞으로");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("zipCode"),
                    addMemberAddressRequest,"999-999");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("roadNameAddress"),
                    addMemberAddressRequest,"한산도");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("numberAddress"),
                    addMemberAddressRequest,"명량");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("notes"),
                    addMemberAddressRequest,"(전라남도 좌수사)");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("detailAddress"),
                    addMemberAddressRequest,"상세주소");
            ReflectionUtils.setField(addMemberAddressRequest.getClass().getDeclaredField("defaultLocation"),
                    addMemberAddressRequest,true);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        MemberAddress memberAddress = MemberAddress.createMemberAddress(member,addMemberAddressRequest);
        memberAddress = addressRepository.save(memberAddress);

        MemberAddress memberAddressForDelete = addressRepository.findById(memberAddress.getId()).get();

        addressRepository.delete(memberAddressForDelete);

    }


}