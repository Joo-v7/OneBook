package com.nhnacademy.authentication.repository;

import com.nhnacademy.authentication.dev_Member.DevMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<DevMember, Integer> {
    Optional<DevMember> findMemberById(String id);
}
