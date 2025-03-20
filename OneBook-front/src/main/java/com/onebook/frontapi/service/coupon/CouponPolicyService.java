package com.onebook.frontapi.service.coupon;

import com.onebook.frontapi.dto.category.CategoryDTO;
import com.onebook.frontapi.dto.coupon.request.couponPolicy.*;
import com.onebook.frontapi.dto.coupon.response.couponPolicy.*;
import com.onebook.frontapi.feign.category.CategoryClient;
import com.onebook.frontapi.feign.coupon.CouponPolicyClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponPolicyService {

    private final CategoryClient categoryClient;
    private final CouponPolicyClient couponPolicyClient;
    private final CouponService couponService;
    private final int PAGE_SIZE = 10;

    public RatePolicyForBookResponse registerRatePolicyForBook
            (AddRatePolicyForBookRequest addRatePolicyForBookRequest){
        return couponPolicyClient.addRatePolicyForBook(addRatePolicyForBookRequest).getBody();
    }

    public RatePolicyForCategoryResponse registerRatePolicyForCategory
            (AddRatePolicyForCategoryRequest addRatePolicyForCategoryRequest){
        return couponPolicyClient.addRatePolicyForCategory(addRatePolicyForCategoryRequest).getBody();
    }

    public PricePolicyForBookResponse registerPricePolicyForBook
            (AddPricePolicyForBookRequest addPricePolicyForBookRequest){
        return couponPolicyClient.addPricePolicyForBook(addPricePolicyForBookRequest).getBody();
    }

    public PricePolicyForCategoryResponse registerPricePolicyForCategory
            (AddPricePolicyForCategoryRequest addPricePolicyForCategoryRequest){
        return couponPolicyClient.addPricePolicyForCategory(addPricePolicyForCategoryRequest).getBody();
    }

    public List<CategoryDTO> getCategoriesForSelect(){

        List<CategoryDTO> categories = categoryClient.getCategories().getBody();
        List<CategoryDTO> existCategories = new ArrayList<>();
        for(CategoryDTO categoryDTO : categories){

            if(!categoryDTO.isStatus()){
                existCategories.add(categoryDTO);
            }

        }

        return existCategories;
    }

    public Page<RatePolicyForBookAndCouponCountResponse> getRatePoliciesForBook(int pageNo){
        Pageable pageable = PageRequest.of(pageNo, PAGE_SIZE);

        // 받아온 page를 순회
        Page<RatePolicyForBookResponse> page = couponPolicyClient.getRatePoliciesForBook(pageable).getBody();
        List<RatePolicyForBookAndCouponCountResponse> resp = new ArrayList<>();

         // 순회하면서 , 각 정책들의 발급된 쿠폰 개수들을 응답에 추가해줌
        for(RatePolicyForBookResponse ratePolicyForBookResponse : page){

            Long count;

            if(couponService.getRateCouponsForBookByPolicyId(ratePolicyForBookResponse.getId()) == null ){
                count = 0l;
            }
            else{
                count = (long) couponService.getRateCouponsForBookByPolicyId(ratePolicyForBookResponse.getId()).size();
            }

            RatePolicyForBookAndCouponCountResponse ratePolicyForBookAndCouponCountResponse =
                    RatePolicyForBookAndCouponCountResponse.changeFeignResponseToResponse(ratePolicyForBookResponse,count);

            resp.add(ratePolicyForBookAndCouponCountResponse);
        }

         return new PageImpl<>(resp,pageable,page.getTotalPages());
    }

    public Page<RatePolicyForCategoryAndCouponCountResponse> getRatePoliciesForCategory(int pageNo){
        Pageable pageable = PageRequest.of(pageNo, PAGE_SIZE);

        // 받아온 page를 순회
        Page<RatePolicyForCategoryResponse> page = couponPolicyClient.getRatePoliciesForCategory(pageable).getBody();
        List<RatePolicyForCategoryAndCouponCountResponse> resp = new ArrayList<>();

        // 순회하면서 , 각 정책들의 발급된 쿠폰 개수들을 응답에 추가해줌
        for(RatePolicyForCategoryResponse ratePolicyForCategoryResponse : page){

            Long count;

            if(couponService.getRateCouponsForCategoryByPolicyId(ratePolicyForCategoryResponse.getId()) == null ){
                count = 0l;
            }
            else{
                count = (long) couponService.getRateCouponsForCategoryByPolicyId(ratePolicyForCategoryResponse.getId())
                        .size();
            }

            RatePolicyForCategoryAndCouponCountResponse ratePolicyForCategoryAndCouponCountResponse =
                    RatePolicyForCategoryAndCouponCountResponse.changeFeignResponseToResponse(ratePolicyForCategoryResponse,count);

            resp.add(ratePolicyForCategoryAndCouponCountResponse);
        }

        return new PageImpl<>(resp,pageable,page.getTotalPages());
    }

    public Page<PricePolicyForBookAndCouponCountResponse> getPricePoliciesForBook(int pageNo){
        Pageable pageable = PageRequest.of(pageNo, PAGE_SIZE);
        // 받아온 page를 순회
        Page<PricePolicyForBookResponse> page = couponPolicyClient.getPricePoliciesForBook(pageable).getBody();
        List<PricePolicyForBookAndCouponCountResponse> resp = new ArrayList<>();

        // 순회하면서 , 각 정책들의 발급된 쿠폰 개수들을 응답에 추가해줌
        for(PricePolicyForBookResponse pricePolicyForBookResponse : page){

            Long count;

            if(couponService.getPriceCouponsForBookByPolicyId(pricePolicyForBookResponse.getId()) == null ){
                count = 0l;
            }
            else{
                count = (long) couponService.getPriceCouponsForBookByPolicyId(pricePolicyForBookResponse.getId())
                        .size();
            }

            PricePolicyForBookAndCouponCountResponse pricePolicyForBookAndCouponCountResponse =
                    PricePolicyForBookAndCouponCountResponse.changeFeignResponseToResponse(pricePolicyForBookResponse,count);

            resp.add(pricePolicyForBookAndCouponCountResponse);
        }

        return new PageImpl<>(resp,pageable,page.getTotalPages());
    }

    public Page<PricePolicyForCategoryAndCouponCountResponse> getPricePoliciesForCategory(int pageNo){
        Pageable pageable = PageRequest.of(pageNo, PAGE_SIZE);
        Page<PricePolicyForCategoryResponse> page = couponPolicyClient.getPricePoliciesForCategory(pageable).getBody();
        List<PricePolicyForCategoryAndCouponCountResponse> resp = new ArrayList<>();

        // 순회하면서 , 각 정책들의 발급된 쿠폰 개수들을 응답에 추가해줌
        for(PricePolicyForCategoryResponse pricePolicyForCategoryResponse : page){

            Long count;

            if(couponService.getPriceCouponsForCategoryByPolicyId(pricePolicyForCategoryResponse.getId()) == null ){
                count = 0l;
            }
            else{
                count = (long) couponService.getPriceCouponsForCategoryByPolicyId(pricePolicyForCategoryResponse.getId())
                    .size();
            }

            PricePolicyForCategoryAndCouponCountResponse pricePolicyForCategoryAndCouponCountResponse =
                    PricePolicyForCategoryAndCouponCountResponse.changeFeignResponseToResponse(pricePolicyForCategoryResponse,count);

            resp.add(pricePolicyForCategoryAndCouponCountResponse);
        }

        return new PageImpl<>(resp,pageable,page.getTotalPages());    }

    public RatePolicyForBookResponse getRatePolicyForBook(Long id){
        return couponPolicyClient.getRatePolicyForBook(id).getBody();
    }

    public RatePolicyForCategoryResponse getRatePolicyForCategory(Long id){
        return couponPolicyClient.getRatePolicyForCategory(id).getBody();
    }

    public PricePolicyForBookResponse getPricePolicyForBook(Long id){
        return couponPolicyClient.getPricePolicyForBook(id).getBody();
    }

    public PricePolicyForCategoryResponse getPricePolicyForCategory(Long id){
        return couponPolicyClient.getPricePolicyForCategory(id).getBody();
    }

    public RatePolicyForBookResponse updateRatePolicyForBook(UpdateRatePolicyForBookRequest updateRatePolicyForBookRequest){
        return couponPolicyClient.updateRatePolicyForBook(updateRatePolicyForBookRequest).getBody();
    }

    public RatePolicyForCategoryResponse updateRatePolicyForCategory(UpdateRatePolicyForCategoryRequest updateRatePolicyForCategoryRequest){
        return couponPolicyClient.updateRatePolicyForCategory(updateRatePolicyForCategoryRequest).getBody();
    }

    public PricePolicyForBookResponse updatePricePolicyForBook(UpdatePricePolicyForBookRequest updatePricePolicyForBookRequest){
        return couponPolicyClient.updatePricePolicyForBook(updatePricePolicyForBookRequest).getBody();
    }

    public PricePolicyForCategoryResponse updatePricePolicyForCategory(UpdatePricePolicyForCategoryRequest updatePricePolicyForCategoryRequest){
        return couponPolicyClient.updatePricePolicyForCategory(updatePricePolicyForCategoryRequest).getBody();
    }

    public RatePolicyForBookResponse deleteRatePolicyForBook(Long id){
        return couponPolicyClient.deleteRatePolicyForBook(id).getBody();
    }

    public RatePolicyForBookResponse deleteRatePolicyForCategory(Long id){
        return couponPolicyClient.deleteRatePolicyForCategory(id).getBody();
    }

    public RatePolicyForBookResponse deletePricePolicyForBook(Long id){
        return couponPolicyClient.deletePricePolicyForBook(id).getBody();
    }

    public RatePolicyForBookResponse deletePricePolicyForCategory(Long id){
        return couponPolicyClient.deletePricePolicyForCategory(id).getBody();
    }

    public List<UsingPolicyResponse> getUsingPolicies(){
        return couponPolicyClient.getUsingPolicies().getBody();
    }
}
