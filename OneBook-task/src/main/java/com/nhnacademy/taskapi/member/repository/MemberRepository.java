package com.nhnacademy.taskapi.member.repository;

import com.nhnacademy.taskapi.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByLoginId(String loginId);
    boolean existsByLoginId(String loginId);

    @Query("SELECT m.loginId FROM Member m WHERE m.id = :id")
    Optional<String> getLoginIdById(Long id);

    Page<Member> findAll(Pageable pageable);

}
