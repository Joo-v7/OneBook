package com.onebook.frontapi.adaptor.member;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onebook.frontapi.dto.member.ErrorResponse;
import com.onebook.frontapi.dto.member.MemberFeignResponse;
import com.onebook.frontapi.dto.member.MemberRegisterRequest;
import com.onebook.frontapi.dto.member.MemberResponse;
import com.onebook.frontapi.feign.coupon.CouponBoxClient;
import com.onebook.frontapi.feign.member.MemberClient;
import feign.FeignException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class MemberAdaptor {
    /**
     * FeignClient -> Adaptor -> Service -> Client 로 하려고 했으나, 시간상의 문제로 Adaptor 사용 X.
     */

    private final MemberClient memberClient;
    private final CouponBoxClient couponBoxClient;

    public boolean join(@Valid MemberRegisterRequest memberRegisterRequest) {

        try {
           MemberFeignResponse memberFeignResponse =  memberClient.joinRequest(memberRegisterRequest);

           couponBoxClient.issueWelcomeCouponToMember(memberFeignResponse.loginId());
           log.info("----- 웰 컴 쿠 폰 발 급 됨 -----");

        }catch(FeignException e) {
            String errorJson = e.contentUTF8();

            if (errorJson == null || errorJson.isEmpty()) {
                log.error("FeignException: 회원가입에 대한 task의 응답이 비어있습니다.");
                return false;
            }

            // JSON 파싱하여 ErrorResponse로 변환 (Jackson 예시)
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                ErrorResponse errorResponse = objectMapper.readValue(errorJson, ErrorResponse.class);
                log.error("Error Title: {}", errorResponse.getTitle());
                log.error("Error Status: {}", errorResponse.getStatus());
                return false;
            } catch (JsonProcessingException jsonException) {
                log.error("Failed to parse error response: {}", jsonException.getMessage());
                return false;
            }
        }
        return true;
    }

}