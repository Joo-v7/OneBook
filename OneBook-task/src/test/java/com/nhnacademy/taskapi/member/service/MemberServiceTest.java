package com.nhnacademy.taskapi.member.service;

import com.nhnacademy.taskapi.grade.domain.Grade;
import com.nhnacademy.taskapi.grade.dto.GradeResponseDto;
import com.nhnacademy.taskapi.grade.service.impl.GradeServiceImpl;
import com.nhnacademy.taskapi.member.domain.Member;
import com.nhnacademy.taskapi.member.dto.MemberModifyRequestDto;
import com.nhnacademy.taskapi.member.dto.MemberRegisterRequestDto;
import com.nhnacademy.taskapi.member.dto.MemberResponse;
import com.nhnacademy.taskapi.member.dto.MemberResponseDto;
import com.nhnacademy.taskapi.member.exception.MemberIllegalArgumentException;
import com.nhnacademy.taskapi.member.exception.MemberNotFoundException;
import com.nhnacademy.taskapi.member.repository.MemberRepository;
import com.nhnacademy.taskapi.member.service.impl.MemberServiceImpl;
import com.nhnacademy.taskapi.member.repository.MemberQueryDslRepository;
import com.nhnacademy.taskapi.point.service.impl.PointServiceImpl;
import com.nhnacademy.taskapi.roles.domain.Role;
import com.nhnacademy.taskapi.roles.dto.RoleResponseDto;
import com.nhnacademy.taskapi.roles.service.impl.RoleServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    private RoleServiceImpl roleService;

    @Mock
    private GradeServiceImpl gradeService;

    @Mock
    private PointServiceImpl pointService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberQueryDslRepository memberQueryDslRepository;

    @InjectMocks
    private MemberServiceImpl memberService;

    @Test
    @DisplayName("Get All Members Successfully")
    void getAllMembersTest() {
        Grade grade = Grade.create("ROYAL", 10, "일반 등급");
        Role role = Role.createRole("MEMBER", "일반 회원");
        Member member = Member.createNewMember(grade, "김주혁", "joo", "jjjjjjjjjj", LocalDate.now(), Member.Gender.M, "helloworld@gmail.com", "01011111111", role);
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("createdAt")));

        List<Member> memberList = List.of(member);
        Page<Member> memberPage = new PageImpl<>(memberList, pageable, memberList.size());
        Mockito.when(memberRepository.findAll(pageable)).thenReturn(memberPage);

        Page<MemberResponse> result = memberService.getAllMembers(pageable);

        Mockito.verify(memberRepository, Mockito.times(1)).findAll(pageable);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
        assertThat(result.getContent().getFirst().name()).isEqualTo(member.getName());
        assertThat(result.getContent().getFirst().gender()).isEqualTo("M");
        assertThat(result.getContent().getFirst().email()).isEqualTo("helloworld@gmail.com");
        assertThat(result.getContent().getFirst().phoneNumber()).isEqualTo("01011111111");
    }

//    @Test
//    @DisplayName("Get All Members Failed - Negative page number")
//    void getAllMembersFailedTest() {
//        Pageable pageable = PageRequest.of(0, -1, Sort.by(Sort.Order.desc("createdAt")));
//        assertThrows(MemberIllegalArgumentException.class, () -> memberService.getAllMembers(pageable));
//    }

    @Test
    @DisplayName("Get Member Successfully")
    void getMemberByIdTest() {
        Grade grade = Grade.create("ROYAL", 10, "일반 등급");
        Role role = Role.createRole("MEMBER", "일반 회원");
        Member member = Member.createNewMember(grade, "김주혁", "joo", "jjjjjjjjjj", LocalDate.now(), Member.Gender.M, "helloworld@gmail.com", "01011111111", role);

        Mockito.when(memberRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(member));

        MemberResponseDto result = memberService.getMemberById(1L);

        Mockito.verify(memberRepository, Mockito.times(1)).findById(1L);
        assertThat(result.name()).isEqualTo(member.getName());
        assertThat(result.gender()).isEqualTo("M");
        assertThat(result.email()).isEqualTo("helloworld@gmail.com");
        assertThat(result.phoneNumber()).isEqualTo("01011111111");
    }

    @Test
    @DisplayName("Get Member Failed 1 - memberId doesn't exist")
    void getMemberByIdFailedTest1() {
        assertThrows(MemberNotFoundException.class, () -> memberService.getMemberById(-1L));
    }

//    @Test
//    @DisplayName("Get Member Failed 2 - DB error")
//    void getMemberByIdFailedTest2() {
//        Mockito.when(memberRepository.existsById(Mockito.anyLong())).thenReturn(true);
//        Mockito.when(memberRepository.findById(Mockito.anyLong())).thenThrow(MemberNotFoundException.class);
//
//        assertThrows(MemberNotFoundException.class, () -> memberService.getMemberById(1l));
//    }

    @Test
    @DisplayName("Get Member by LoginId Successfully")
    void getMemberByLoginIdTest() {
        Grade grade = Grade.create("ROYAL", 10, "일반 등급");
        Role role = Role.createRole("MEMBER", "일반 회원");
        Member member = Member.createNewMember(grade, "김주혁", "joo", "jjjjjjjjjj", LocalDate.now(), Member.Gender.M, "helloworld@gmail.com", "01011111111", role);

        Mockito.when(memberRepository.findByLoginId(Mockito.anyString())).thenReturn(Optional.of(member));

        Member result = memberService.getMemberByLoginId("joo");

        Mockito.verify(memberRepository, Mockito.times(1)).findByLoginId(Mockito.anyString());
        assertThat(result.getName()).isEqualTo(member.getName());
        assertThat(result.getLoginId()).isEqualTo(member.getLoginId());
        assertThat(result.getPassword()).isEqualTo(member.getPassword());
        assertThat(result.getGender()).isEqualTo(member.getGender());
        assertThat(result.getEmail()).isEqualTo(member.getEmail());
        assertThat(result.getPhoneNumber()).isEqualTo(member.getPhoneNumber());
    }

//    @Test
//    @DisplayName("Get Member by LoginId Failed 1 - memberId doens't exist")
//    void getLoginByIdFailedTest1() {
//        assertThrows(MemberNotFoundException.class, () -> memberService.getLoginIdById(1L));
//    }

    @Test
    @DisplayName("Get Member by LoginId Failed 2 - member not found")
    void getLoginByIdFailedTest2() {
        assertThrows(MemberNotFoundException.class, () -> memberService.getLoginIdById(1L));
    }


    @Test
    @DisplayName("Exists Id Successfully")
    void existsByIdTest() {
        memberService.existsById(1L);

        Mockito.verify(memberRepository, Mockito.times(1)).existsById(Mockito.anyLong());
    }

    @Test
    @DisplayName("Exists LoginId Successfully")
    void existsByLoginIdTest() {
        memberService.existsByLoginId("joo");

        Mockito.verify(memberRepository, Mockito.times(1)).existsByLoginId(Mockito.anyString());
    }

    @Test
    @DisplayName("Register Member Successfully")
    void registerMemberTest() {
        Grade grade = Grade.create("ROYAL", 10, "일반 등급");
        Role role = Role.createRole("MEMBER", "일반 회원");
        Member member = Member.createNewMember(grade, "김주혁", "joo", "jjjjjjjjjj", LocalDate.now(), Member.Gender.M, "helloworld@gmail.com", "01011111111", role);

        GradeResponseDto gradeResponseDto = new GradeResponseDto(1,"ROYAL", 10, "일반 등급");
        RoleResponseDto roleResponseDto = new RoleResponseDto(1, "MEMBER", "일반 회원");

        Mockito.when(memberRepository.existsByLoginId(Mockito.anyString())).thenReturn(false);
        Mockito.when(gradeService.getDefaultGrade()).thenReturn(gradeResponseDto);
        Mockito.when(roleService.getDefaultRole()).thenReturn(roleResponseDto);
        Mockito.when(memberRepository.save(Mockito.any())).thenReturn(member);
//        Mockito.doNothing().when(pointService).registerMemberPoints(Mockito.any());

       MemberResponseDto result =  memberService.registerMember(new MemberRegisterRequestDto(
                "김주혁",
                "joo",
                "jjjjjjjjjj",
                LocalDate.now(),
                Member.Gender.M,
                "helloworld@gmail.com",
                "01011111111"
        ));

        Mockito.verify(memberRepository, Mockito.times(1)).save(Mockito.any());
        assertThat(result.name()).isEqualTo(member.getName());
        assertThat(result.gender()).isEqualTo("M");
        assertThat(result.email()).isEqualTo("helloworld@gmail.com");
        assertThat(result.phoneNumber()).isEqualTo("01011111111");
    }

    @Test
    @DisplayName("Register Member Failed 1 - loginId already exists")
    void registerMemberFailedTest1() {
        Mockito.when(memberService.existsByLoginId(Mockito.anyString())).thenReturn(true);

        assertThrows(MemberIllegalArgumentException.class,
                () -> memberService.registerMember(new MemberRegisterRequestDto(
                "김주혁",
                "joo",
                "jjjjjjjjjj",
                LocalDate.now(),
                Member.Gender.M,
                "helloworld@gmail.com",
                "01011111111"
        )));

    }

    @Test
    @DisplayName("Register Member Failed 2 - DB error")
    void registerMemberFailedTest2() {
        GradeResponseDto gradeResponseDto = new GradeResponseDto(1,"ROYAL", 10, "일반 등급");
        RoleResponseDto roleResponseDto = new RoleResponseDto(1, "MEMBER", "일반 회원");

        Mockito.when(memberRepository.existsByLoginId(Mockito.anyString())).thenReturn(false);
        Mockito.when(gradeService.getDefaultGrade()).thenReturn(gradeResponseDto);
        Mockito.when(roleService.getDefaultRole()).thenReturn(roleResponseDto);
        Mockito.when(memberRepository.save(Mockito.any())).thenThrow(DataIntegrityViolationException.class);

        assertThrows(MemberIllegalArgumentException.class,

                () -> memberService.registerMember(new MemberRegisterRequestDto(
                "김주혁",
                "joo",
                "jjjjjjjjjj",
                LocalDate.now(),
                Member.Gender.M,
                "helloworld@gmail.com",
                "01011111111"
        )));
    }

    @Test
    @DisplayName("Modify Member Successfully 1 - Password changed")
    void modifyMemberTest1() {
        Grade grade = Grade.create("ROYAL", 10, "일반 등급");
        Role role = Role.createRole("MEMBER", "일반 회원");
        Member member = Member.createNewMember(grade, "김주혁", "joo", BCrypt.hashpw("jjjjjjjjjj", BCrypt.gensalt()), LocalDate.now(), Member.Gender.M, "helloworld@gmail.com", "01011111111", role);

        Mockito.when(memberRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(member));

        MemberResponseDto result = memberService.modifyMember(Mockito.anyLong() ,new MemberModifyRequestDto(
                "혁주김",
                "oooooooooo",
                "worldhello@gmail.com",
                "01022222222"
        ));

        Mockito.verify(memberRepository, Mockito.times(1)).findById(Mockito.any());
        assertThat(result.name()).isEqualTo(member.getName());
        assertThat(result.gender()).isEqualTo("M");
        assertThat(result.email()).isEqualTo("worldhello@gmail.com");
        assertThat(result.phoneNumber()).isEqualTo("01022222222");
    }

    @Test
    @DisplayName("Modify Member Successfully 2 - Password not changed")
    void modifyMemberTest2() {
        Grade grade = Grade.create("ROYAL", 10, "일반 등급");
        Role role = Role.createRole("MEMBER", "일반 회원");
        Member member = Member.createNewMember(grade, "김주혁", "joo", BCrypt.hashpw("jjjjjjjjjj", BCrypt.gensalt()), LocalDate.now(), Member.Gender.M, "helloworld@gmail.com", "01011111111", role);

        Mockito.when(memberRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(member));

        MemberResponseDto result = memberService.modifyMember(Mockito.anyLong() ,new MemberModifyRequestDto(
                "혁주김",
                "jjjjjjjjjj",
                "worldhello@gmail.com",
                "01022222222"
        ));

        Mockito.verify(memberRepository, Mockito.times(1)).findById(Mockito.any());
        assertThat(result.name()).isEqualTo(member.getName());
        assertThat(result.gender()).isEqualTo("M");
        assertThat(result.email()).isEqualTo("worldhello@gmail.com");
        assertThat(result.phoneNumber()).isEqualTo("01022222222");
    }

//    @Test
//    @DisplayName("Modify Member Failed - Db error")
//    void modifyMemberFailedTest() {
//        Grade grade = Grade.create("ROYAL", 10, "일반 등급");
//        Role role = Role.createRole("MEMBER", "일반 회원");
//        Member member = Member.createNewMember(grade, "김주혁", "joo", BCrypt.hashpw("jjjjjjjjjj", BCrypt.gensalt()), LocalDate.now(), Member.Gender.M, "helloworld@gmail.com", "010-1111-1111", role);
//
//        Mockito.when(memberRepository.existsById(Mockito.anyLong())).thenReturn(true);
//        Mockito.when(memberRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(member));
//        Mockito.when(memberRepository.save(Mockito.any())).thenThrow(DataIntegrityViolationException.class);
//
//        assertThrows(MemberDataIntegrityViolationException.class,
//                () -> memberService.modifyMember(Mockito.anyLong() ,new MemberModifyRequestDto(
//                "혁주김",
//                "jjjjjjjjjj",
//                "worldhello@gmail.com",
//                "010-2222-2222"
//        )));
//    }

    @Test
    @DisplayName("Remove Member Successfully")
    void removeMemberTest() {
        Grade grade = Grade.create("ROYAL", 10, "일반 등급");
        Role role = Role.createRole("MEMBER", "일반 회원");
        Member member = Member.createNewMember(grade, "김주혁", "joo", BCrypt.hashpw("jjjjjjjjjj", BCrypt.gensalt()), LocalDate.now(), Member.Gender.M, "helloworld@gmail.com", "01011111111", role);

        Mockito.when(memberRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(member));

        memberService.removeMember(Mockito.anyLong());

        Mockito.verify(memberRepository, Mockito.times(1)).findById(Mockito.any());
    }

    @Test
    @DisplayName("Change Status1 - To ACTIVE")
    void changeStatusToActivation1() {
        Grade grade = Grade.create("ROYAL", 10, "일반 등급");
        Role role = Role.createRole("MEMBER", "일반 회원");
        Member member = Member.createNewMember(grade, "김주혁", "joo", BCrypt.hashpw("jjjjjjjjjj", BCrypt.gensalt()), LocalDate.now(), Member.Gender.M, "helloworld@gmail.com", "01011111111", role);

        Mockito.when(memberRepository.findByLoginId(Mockito.anyString())).thenReturn(Optional.of(member));

        memberService.changeStatusToActivation(Mockito.anyString());
        Mockito.verify(memberRepository, Mockito.times(1)).findByLoginId(Mockito.any());
    }

//    @Test
//    @DisplayName("Change Status2 - To SUSPENDED")
//    void changeStatusToActivation2() {
//        Grade grade = Grade.create("ROYAL", 10, "일반 등급");
//        Role role = Role.createRole("MEMBER", "일반 회원");
//        Member member = Member.createNewMember(grade, "김주혁", "joo", BCrypt.hashpw("jjjjjjjjjj", BCrypt.gensalt()), LocalDate.now(), Member.Gender.M, "helloworld@gmail.com", "01011111111", role);
//
//        Mockito.when(memberRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(member));
//
//        memberService.changeStatusToActivation(Mockito.anyLong(), "SUSPENDED");
//        Mockito.verify(memberRepository, Mockito.times(1)).findById(Mockito.any());
//    }

//    @Test
//    @DisplayName("Remove Member Failed - DB error")
//    void removeMemberFailedTest() {
//        Grade grade = Grade.create("ROYAL", 10, "일반 등급");
//        Role role = Role.createRole("MEMBER", "일반 회원");
//        Member member = Member.createNewMember(grade, "김주혁", "joo", BCrypt.hashpw("jjjjjjjjjj", BCrypt.gensalt()), LocalDate.now(), Member.Gender.M, "helloworld@gmail.com", "010-1111-1111", role);
//
//        Mockito.when(memberRepository.existsById(Mockito.anyLong())).thenReturn(true);
//        Mockito.when(memberRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(member));
//        Mockito.when(memberRepository.save(Mockito.any())).thenThrow(DataIntegrityViolationException.class);
//
//        assertThrows(MemberDataIntegrityViolationException.class,
//                () -> memberService.removeMember(Mockito.anyLong()));
//    }

    /**
     * 로그인, 로그아웃 테스트 보류.
     */



















}
