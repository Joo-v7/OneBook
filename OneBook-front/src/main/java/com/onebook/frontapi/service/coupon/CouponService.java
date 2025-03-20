package com.onebook.frontapi.service.coupon;

import com.onebook.frontapi.dto.coupon.request.coupon.CreateCouponRequest;
import com.onebook.frontapi.dto.coupon.response.coupon.CouponResponse;
import com.onebook.frontapi.feign.coupon.CouponClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponClient couponClient;
    private final int PAGE_SIZE = 10;

    public Page<CouponResponse> getAllCoupons(int pageNo){

        Pageable pageable = PageRequest.of(pageNo,PAGE_SIZE, Sort.by("creationTime").descending());
        return couponClient.getAllCoupons(pageable).getBody();
    }

    public CouponResponse deleteCoupon(String couponNumber){
        return couponClient.deleteCoupon(couponNumber).getBody();
    }

    public List<CouponResponse> createRateCouponForBook(CreateCouponRequest createCouponRequest){
        return couponClient.createRateCouponForBook(createCouponRequest).getBody();
    }

    public List<CouponResponse> createRateCouponForCategory(CreateCouponRequest createCouponRequest){
        return couponClient.createRateCouponForCategory(createCouponRequest).getBody();
    }

    public List<CouponResponse> createPriceCouponForBook(CreateCouponRequest createCouponRequest){
        return couponClient.createPriceCouponForBook(createCouponRequest).getBody();
    }

    public List<CouponResponse> createPriceCouponForCategory(CreateCouponRequest createCouponRequest){
        return couponClient.createPriceCouponForCategory(createCouponRequest).getBody();
    }

    public List<CouponResponse> getRateCouponsForBookByPolicyId(Long policyId){

        return couponClient.getRateCouponsForBookByPolicyId(policyId).getBody();
    }

    public List<CouponResponse> getRateCouponsForCategoryByPolicyId(Long policyId){

        return couponClient.getRateCouponsForCategoryByPolicyId(policyId).getBody();
    }

    public List<CouponResponse> getPriceCouponsForBookByPolicyId(Long policyId){

        return couponClient.getPriceCouponsForBookByPolicyId(policyId).getBody();
    }

    public List<CouponResponse> getPriceCouponsForCategoryByPolicyId(Long policyId){

        return couponClient.getPriceCouponsForCategoryByPolicyId(policyId).getBody();
    }


}
