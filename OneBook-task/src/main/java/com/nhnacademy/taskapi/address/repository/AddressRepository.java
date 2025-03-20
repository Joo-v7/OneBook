package com.nhnacademy.taskapi.address.repository;

import com.netflix.spectator.api.NoopRegistry;
import com.nhnacademy.taskapi.address.domain.entity.MemberAddress;
import com.nhnacademy.taskapi.member.domain.Member;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<MemberAddress , Long> {

    List<MemberAddress> findMemberAddressByMember(Member member);

    NoopRegistry findMemberAddressByMember_Id(Long memberId);
}
