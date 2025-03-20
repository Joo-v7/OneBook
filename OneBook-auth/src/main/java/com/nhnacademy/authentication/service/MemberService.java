package com.nhnacademy.authentication.service;

import com.nhnacademy.authentication.dev_Member.DevMember;
import com.nhnacademy.authentication.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public DevMember findMember(String id){
        Optional<DevMember> memberById = memberRepository.findMemberById(id);


        return memberById.orElse( null);
    }
}
