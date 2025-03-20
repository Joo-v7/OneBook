package com.nhnacademy.taskapi.member.domain;

import com.nhnacademy.taskapi.grade.domain.Grade;
import com.nhnacademy.taskapi.roles.domain.Role;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
@Table(name="members")
public class Member {

    @Id
    @Tsid
    @Column(name="member_id")
    private Long id;

    @Setter
    @ManyToOne
    @JoinColumn(name="grade_id", nullable=false)
    private Grade grade;

    @Setter
    @ManyToOne
    @JoinColumn(name="role_id", nullable=false)
    private Role role;

    @NotBlank
    private String name;

    @NotBlank
    @Column(name="login_id", unique=true)
    private String loginId;

    @NotBlank
    private String password;

    @Column(name="date_of_birth", nullable=false)
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name="gender", nullable=false)
    private Gender gender;

    @Email
    @NotBlank
    @Column(name="email")
    private String email;

    @NotBlank
    @Column(name="phone_number")
    private String phoneNumber;

    @Setter
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status; // default: ACTIVE

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Setter
    private LocalDateTime lastLoginAt;

    // 멤버 상태 - 활성화, 비활성화(휴면), 삭제, 정지 / default: INACTIVE
    public enum Status {
        ACTIVE, INACTIVE, DELETED, SUSPENDED
    }

    // 멤버 성별 - 남,여
    public enum Gender {
        M, F
    }

    // 회원 가입 생성자: 로그인 기록이 없는 멤버
    public static Member createNewMember(Grade grade, String name, String loginId, String password, LocalDate dateOfBirth, Gender gender, String email, String phoneNumber, Role role) {
        return new Member(grade, name, loginId, password, dateOfBirth, gender, email, phoneNumber, role);
    }

    // 기존 회원 생성자: 로그인 기록이 있는 멤버
    public static Member reconstruct(Grade grade, String name, String loginId, String password, LocalDate dateOfBirth, Gender gender, String email, String phoneNumber, Role role, Status status, LocalDateTime createdAt,LocalDateTime lastLoginAt) {
        return new Member(grade, name, loginId, password, dateOfBirth, gender, email, phoneNumber, role, status, createdAt, lastLoginAt);
    }

    // 멤버 수정 - MemberModifyRequestDto 참고.
    public void modifyMember(String name, String password, String email, String phoneNumber) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    // 로그인 기록, 멤버상태가 없는 멤버: 신규 회원(회원가입)
    private Member(Grade grade, String name, String loginId, String password, LocalDate dateOfBirth, Gender gender, String email, String phoneNumber, Role role) {
        this.grade = grade;
        this.name = name;
        this.loginId = loginId;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.status = Status.ACTIVE;
        this.createdAt = LocalDateTime.now();
        this.lastLoginAt = null;
    }

    // 로그인 기록이 있는 멤버: 기존 회원
    private Member(Grade grade, String name, String loginId, String password, LocalDate dateOfBirth, Gender gender, String email, String phoneNumber, Role role, Status status, LocalDateTime createdAt,LocalDateTime lastLoginAt) {
        this.grade = grade;
        this.name = name;
        this.loginId = loginId;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
        this.lastLoginAt = lastLoginAt;
    }

}
