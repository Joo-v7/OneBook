package com.nhnacademy.taskapi.member.repository;

import com.nhnacademy.taskapi.config.QuerydslConfig;
import com.nhnacademy.taskapi.grade.domain.Grade;
import com.nhnacademy.taskapi.member.domain.Member;
import com.nhnacademy.taskapi.roles.domain.Role;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
@Import(QuerydslConfig.class)

public class MemberRepositoryTest {

    /**
     * 회원 삭제는 지원하지 않는다.
     */

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Find All Members with Pagination")
    void findAllTest() {
        Grade grade = Grade.create("ROYAL", 10, "일반 등급");
        Role role = Role.createRole("MEMBER", "일반 회원");

        Grade savedGrade = entityManager.persist(grade);
        Role savedRole = entityManager.persist(role);
        Member member = Member.createNewMember(savedGrade, "김주혁", "joo", "jjjjjjjjjj", LocalDate.now(), Member.Gender.M, "helloworld@gmail.com", "01011111111", savedRole);

        Member savedMember = memberRepository.save(member);

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("createdAt")));
        Page<Member> memberPage = memberRepository.findAll(pageable);

        // page 정보 검증
        assertThat(memberPage.getNumber()).isEqualTo(0);
        assertThat(memberPage.getSize()).isEqualTo(10);
        assertThat(memberPage.getSort().getOrderFor("createdAt").getDirection()).isEqualTo(Sort.Direction.DESC);

        // 첫 번째 멤버 가져오기
        Member target = memberPage.getContent().getFirst();

        // 멤버 검증
        assertThat(target.getGrade()).isEqualTo(savedGrade);
        assertThat(target.getName()).isEqualTo(savedMember.getName());
        assertThat(target.getLoginId()).isEqualTo(savedMember.getLoginId());
        assertThat(target.getPassword()).isEqualTo(savedMember.getPassword());
        assertThat(target.getDateOfBirth()).isEqualTo(savedMember.getDateOfBirth());
        assertThat(target.getGender()).isEqualTo(savedMember.getGender());
        assertThat(target.getEmail()).isEqualTo(savedMember.getEmail());
        assertThat(target.getPhoneNumber()).isEqualTo(savedMember.getPhoneNumber());
        assertThat(target.getRole()).isEqualTo(savedMember.getRole());
    }

    @Test
    @DisplayName("Find Member by Id")
    void findByIdTest() {
        Grade grade = Grade.create("ROYAL", 10, "일반 등급");
        Role role = Role.createRole("MEMBER", "일반 회원");

        Grade savedGrade = entityManager.persist(grade);
        Role savedRole = entityManager.persist(role);
        Member member = Member.createNewMember(savedGrade, "김주혁", "joo", "jjjjjjjjjj", LocalDate.now(), Member.Gender.M, "helloworld@gmail.com", "01011111111", savedRole);

        Member savedMember = memberRepository.save(member);

        Member target = memberRepository.findById(savedMember.getId()).get();

        // 멤버 검증
        assertThat(target.getGrade()).isEqualTo(savedGrade);
        assertThat(target.getName()).isEqualTo(savedMember.getName());
        assertThat(target.getLoginId()).isEqualTo(savedMember.getLoginId());
        assertThat(target.getPassword()).isEqualTo(savedMember.getPassword());
        assertThat(target.getDateOfBirth()).isEqualTo(savedMember.getDateOfBirth());
        assertThat(target.getGender()).isEqualTo(savedMember.getGender());
        assertThat(target.getEmail()).isEqualTo(savedMember.getEmail());
        assertThat(target.getPhoneNumber()).isEqualTo(savedMember.getPhoneNumber());
        assertThat(target.getRole()).isEqualTo(savedMember.getRole());
    }

    @Test
    @DisplayName("Find Member by loginId")
    void findByLoginIdTest() {
        Grade grade = Grade.create("ROYAL", 10, "일반 등급");
        Role role = Role.createRole("MEMBER", "일반 회원");

        Grade savedGrade = entityManager.persist(grade);
        Role savedRole = entityManager.persist(role);
        Member member = Member.createNewMember(savedGrade, "김주혁", "joo", "jjjjjjjjjj", LocalDate.now(), Member.Gender.M, "helloworld@gmail.com", "01011111111", savedRole);

        Member savedMember = memberRepository.save(member);

        Member target = memberRepository.findByLoginId(savedMember.getLoginId()).get();

        // 멤버 검증
        assertThat(target.getGrade()).isEqualTo(savedGrade);
        assertThat(target.getName()).isEqualTo(savedMember.getName());
        assertThat(target.getLoginId()).isEqualTo(savedMember.getLoginId());
        assertThat(target.getPassword()).isEqualTo(savedMember.getPassword());
        assertThat(target.getDateOfBirth()).isEqualTo(savedMember.getDateOfBirth());
        assertThat(target.getGender()).isEqualTo(savedMember.getGender());
        assertThat(target.getEmail()).isEqualTo(savedMember.getEmail());
        assertThat(target.getPhoneNumber()).isEqualTo(savedMember.getPhoneNumber());
        assertThat(target.getRole()).isEqualTo(savedMember.getRole());
    }

    @Test
    @DisplayName("Find loginId by memberId")
    void getLoginIdByIdTest() {
        Grade grade = Grade.create("ROYAL", 10, "일반 등급");
        Role role = Role.createRole("MEMBER", "일반 회원");

        Grade savedGrade = entityManager.persist(grade);
        Role savedRole = entityManager.persist(role);
        Member member = Member.createNewMember(savedGrade, "김주혁", "joo", "jjjjjjjjjj", LocalDate.now(), Member.Gender.M, "helloworld@gmail.com", "01011111111", savedRole);

        Member savedMember = memberRepository.save(member);

        String target = memberRepository.getLoginIdById(savedMember.getId()).get();

        assertThat(target).isEqualTo(savedMember.getLoginId());

    }

    @Test
    @DisplayName("Exists memberId")
    void existsByIdTest() {
        Grade grade = Grade.create("ROYAL", 10, "일반 등급");
        Role role = Role.createRole("MEMBER", "일반 회원");

        Grade savedGrade = entityManager.persist(grade);
        Role savedRole = entityManager.persist(role);
        Member member = Member.createNewMember(savedGrade, "김주혁", "joo", "jjjjjjjjjj", LocalDate.now(), Member.Gender.M, "helloworld@gmail.com", "01011111111", savedRole);

        Member savedMember = memberRepository.save(member);

        boolean target = memberRepository.existsById(savedMember.getId());

        assertThat(target).isEqualTo(true);
    }

    @Test
    @DisplayName("Exists loginId")
    void existsByLoginIdTest() {
        Grade grade = Grade.create("ROYAL", 10, "일반 등급");
        Role role = Role.createRole("MEMBER", "일반 회원");

        Grade savedGrade = entityManager.persist(grade);
        Role savedRole = entityManager.persist(role);
        Member member = Member.createNewMember(savedGrade, "김주혁", "joo", "jjjjjjjjjj", LocalDate.now(), Member.Gender.M, "helloworld@gmail.com", "01011111111", savedRole);

        Member savedMember = memberRepository.save(member);

        boolean target = memberRepository.existsByLoginId(savedMember.getLoginId());

        assertThat(target).isEqualTo(true);
    }

    @Test
    @DisplayName("Get MemberNetPaymentAmount")
    void memberNetPaymentAmountTest() {
        Grade grade = Grade.create("ROYAL", 10, "일반 등급");
        Role role = Role.createRole("MEMBER", "일반 회원");

        Grade savedGrade = entityManager.persist(grade);
        Role savedRole = entityManager.persist(role);
        Member member = Member.createNewMember(savedGrade, "김주혁", "joo", "jjjjjjjjjj", LocalDate.now(), Member.Gender.M, "helloworld@gmail.com", "01011111111", savedRole);

    }


}
