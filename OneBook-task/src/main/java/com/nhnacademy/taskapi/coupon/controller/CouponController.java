package com.nhnacademy.taskapi.coupon.controller;

import com.nhnacademy.taskapi.coupon.domain.dto.coupons.request.CreateCouponRequest;
import com.nhnacademy.taskapi.coupon.domain.dto.coupons.response.CouponResponse;
import com.nhnacademy.taskapi.coupon.service.coupons.CouponService;
import feign.Response;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/task")
@Transactional
@Tag(name = "Coupon", description = "쿠폰을 생성, 조회, 삭제")  // API 그룹 설명 추가
public class CouponController {

    private final CouponService couponService;

    @PostMapping("/coupon/rate/book")
    public ResponseEntity<List<CouponResponse>> createRateCouponForBook(@RequestBody CreateCouponRequest createCouponRequest){
        List<CouponResponse> couponResponses = couponService.createRateCouponForBook(createCouponRequest);
        return ResponseEntity.ok(couponResponses);
    }

    @PostMapping("/coupon/rate/category")
    public ResponseEntity<List<CouponResponse>> createRateCouponForCategory(@RequestBody CreateCouponRequest createCouponRequest){
        List<CouponResponse> couponResponses = couponService.createRateCouponForCategory(createCouponRequest);
        return ResponseEntity.ok(couponResponses);
    }

    @PostMapping("/coupon/price/book")
    public ResponseEntity<List<CouponResponse>> createPriceCouponForBook(@RequestBody CreateCouponRequest createCouponRequest){
        List<CouponResponse> couponResponses = couponService.createPriceCouponForBook(createCouponRequest);
        return ResponseEntity.ok(couponResponses);
    }

    @PostMapping("/coupon/price/category")
    public ResponseEntity<List<CouponResponse>> createPriceCouponForCategory(@RequestBody CreateCouponRequest createCouponRequest){
        List<CouponResponse> couponResponses = couponService.createPriceCouponForCategory(createCouponRequest);
        return ResponseEntity.ok(couponResponses);
    }

    @GetMapping("/coupon")
    public ResponseEntity<Page<CouponResponse>> getAllCoupons(Pageable pageable){

        Page<CouponResponse> couponResponses = couponService.getAllCoupons(pageable);
        return ResponseEntity.ok(couponResponses);
    }

    // 해당 정책으로 생성된, 발급 가능한 쿠폰들 받아오기
    @GetMapping("/coupon/rate/book/{policy-id}")
    public ResponseEntity<List<CouponResponse>> getRateCouponsForBookByPolicyId
        (@PathVariable(name = "policy-id") Long policyId){

        List<CouponResponse> couponResponses = couponService.getRateCouponsForBook(policyId);
        return ResponseEntity.ok(couponResponses);
    }

    @GetMapping("/coupon/rate/category/{policy-id}")
    public ResponseEntity<List<CouponResponse>> getRateCouponsForCategoryByPolicyId
            (@PathVariable(name = "policy-id") Long policyId){

        List<CouponResponse> couponResponses = couponService.getRateCouponsForCategory(policyId);
        return ResponseEntity.ok(couponResponses);

    }

    @GetMapping("/coupon/price/book/{policy-id}")
    public ResponseEntity<List<CouponResponse>> getPriceCouponsForBookByPolicyId
            (@PathVariable(name = "policy-id") Long policyId){

        List<CouponResponse> couponResponses = couponService.getPriceCouponsForBook(policyId);
        return ResponseEntity.ok(couponResponses);

    }

    @GetMapping("/coupon/price/category/{policy-id}")
    public ResponseEntity<List<CouponResponse>> getPriceCouponsForCategoryByPolicyId
            (@PathVariable(name = "policy-id") Long policyId){

        List<CouponResponse> couponResponses = couponService.getPriceCouponsForCategory(policyId);
        return ResponseEntity.ok(couponResponses);

    }

    @GetMapping("/coupon/{coupon-number}")
    public ResponseEntity<CouponResponse> getCouponByCouponNumber(@PathVariable(name = "coupon-number") String couponNumber){

        CouponResponse couponResponse = couponService.getCouponByCouponNumber(couponNumber);
        return ResponseEntity.ok(couponResponse);
    }

    @DeleteMapping("/coupon/{coupon-number}")
    public ResponseEntity<CouponResponse> deleteCoupon(@PathVariable(name = "coupon-number") String couponNumber){

        CouponResponse couponResponse = couponService.deleteCoupon(couponNumber);
        return ResponseEntity.ok(couponResponse);
    }

}
