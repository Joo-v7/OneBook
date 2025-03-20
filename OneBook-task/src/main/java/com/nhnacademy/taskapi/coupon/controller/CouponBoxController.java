package com.nhnacademy.taskapi.coupon.controller;

import com.nhnacademy.taskapi.coupon.domain.dto.coupons.request.IssueCouponToMemberRequest;
import com.nhnacademy.taskapi.coupon.domain.dto.coupons.response.IssuedCouponResponse;
import com.nhnacademy.taskapi.coupon.service.coupons.CouponBoxService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/task")
@Transactional
@Tag(name = "CouponBox", description = "사용자가 가지고 있는 쿠폰 조회, 특정 도서에 적용가능한 쿠폰 조회, 사용자에게 쿠폰 발급")  // API 그룹 설명 추가
public class CouponBoxController {

    private final CouponBoxService couponBoxService;

    @GetMapping("/coupon/coupon-box")
    public ResponseEntity<Page<IssuedCouponResponse>> getIssuedCouponsByMemberId(@RequestHeader("X-MEMBER-ID") Long memberId, Pageable pageable){
        Page<IssuedCouponResponse> couponsOfMembers = couponBoxService.getIssuedCouponsByMemberId(pageable,memberId);
        return ResponseEntity.ok(couponsOfMembers);
    }

    @GetMapping("/coupon/apply/{book-id}")
    public ResponseEntity<List<IssuedCouponResponse>> getIssuedCouponsValidForBookByMemberId
            (@RequestHeader("X-MEMBER-ID") Long memberId, @PathVariable(name = "book-id") Long bookId){

        List<IssuedCouponResponse> couponOfMembersValidForBook =
                couponBoxService.getIssuedCouponsValidForBookByMemberId(memberId,bookId);
        return ResponseEntity.ok(couponOfMembersValidForBook);
    }


    @PostMapping("/coupon/issue")
    public ResponseEntity<IssuedCouponResponse> issueCouponToMember(@RequestHeader("X-MEMBER-ID") Long memberId,
                                                                    @RequestBody IssueCouponToMemberRequest issueCouponToMemberRequest){

        IssuedCouponResponse issuedCouponResponse = couponBoxService.issueCouponToMember(memberId,issueCouponToMemberRequest);
        return ResponseEntity.ok(issuedCouponResponse);
    }

    @PostMapping("/coupon/issue/welcome/{login-id}")
    public ResponseEntity<IssuedCouponResponse> issueWelcomeCouponToMember(@PathVariable(name = "login-id") String loginId){
        IssuedCouponResponse issuedWelcomeCouponResponse =
                couponBoxService.issueWelcomeCouponToMember(loginId);

        return ResponseEntity.ok(issuedWelcomeCouponResponse);
    }
}
