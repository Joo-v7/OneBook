package com.onebook.frontapi.dto.member;


public record MemberLoginRequest(

        String loginId,
        String password

) {}
