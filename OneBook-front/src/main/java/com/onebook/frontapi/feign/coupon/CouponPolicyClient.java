package com.onebook.frontapi.feign.coupon;

import com.onebook.frontapi.dto.coupon.request.couponPolicy.*;
import com.onebook.frontapi.dto.coupon.response.couponPolicy.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@FeignClient(name = "CouponPolicyClient",  url = "${onebook.gatewayUrl}")
public interface CouponPolicyClient {

    @PostMapping("/task/policies/rate/book")
    ResponseEntity<RatePolicyForBookResponse> addRatePolicyForBook
            (@RequestBody AddRatePolicyForBookRequest addRatePolicyForBookRequest);

    @PostMapping("/task/policies/rate/category")
    ResponseEntity<RatePolicyForCategoryResponse> addRatePolicyForCategory
            (@RequestBody AddRatePolicyForCategoryRequest addRatePolicyForCategoryRequest);

    @PostMapping("/task/policies/price/book")
    ResponseEntity<PricePolicyForBookResponse> addPricePolicyForBook
            (@RequestBody AddPricePolicyForBookRequest addPricePolicyForBookRequest);

    @PostMapping("/task/policies/price/category")
    ResponseEntity<PricePolicyForCategoryResponse> addPricePolicyForCategory
            (@RequestBody AddPricePolicyForCategoryRequest addPricePolicyForCategoryRequest);

    @GetMapping("/task/policies/rate/book")
    ResponseEntity<Page<RatePolicyForBookResponse>> getRatePoliciesForBook(Pageable pageable);

    @GetMapping("/task/policies/rate/category")
    ResponseEntity<Page<RatePolicyForCategoryResponse>> getRatePoliciesForCategory(Pageable pageable);

    @GetMapping("/task/policies/price/book")
    ResponseEntity<Page<PricePolicyForBookResponse>> getPricePoliciesForBook(Pageable pageable);

    @GetMapping("/task/policies/price/category")
    ResponseEntity<Page<PricePolicyForCategoryResponse>> getPricePoliciesForCategory(Pageable pageable);

    @GetMapping("/task/policies/rate/book/{id}")
    ResponseEntity<RatePolicyForBookResponse> getRatePolicyForBook(@PathVariable Long id);

    @GetMapping("/task/policies/rate/category/{id}")
    ResponseEntity<RatePolicyForCategoryResponse> getRatePolicyForCategory(@PathVariable Long id);

    @GetMapping("/task/policies/price/book/{id}")
    ResponseEntity<PricePolicyForBookResponse> getPricePolicyForBook(@PathVariable Long id);

    @GetMapping("/task/policies/price/category/{id}")
    ResponseEntity<PricePolicyForCategoryResponse> getPricePolicyForCategory(@PathVariable Long id);

    @PutMapping("/task/policies/rate/book")
    ResponseEntity<RatePolicyForBookResponse> updateRatePolicyForBook
            (@RequestBody UpdateRatePolicyForBookRequest updateRatePolicyForBookRequest);

    @PutMapping("/task/policies/rate/category")
    ResponseEntity<RatePolicyForCategoryResponse> updateRatePolicyForCategory
            (@RequestBody UpdateRatePolicyForCategoryRequest updateRatePolicyForCategoryRequest);

    @PutMapping("/task/policies/price/book")
    ResponseEntity<PricePolicyForBookResponse> updatePricePolicyForBook
            (@RequestBody UpdatePricePolicyForBookRequest updatePricePolicyForBookRequest);

    @PutMapping("/task/policies/price/category")
    ResponseEntity<PricePolicyForCategoryResponse> updatePricePolicyForCategory
            (@RequestBody UpdatePricePolicyForCategoryRequest updatePricePolicyForCategoryRequest);

    @DeleteMapping("/task/policies/rate/book/{id}")
    ResponseEntity<RatePolicyForBookResponse> deleteRatePolicyForBook(@PathVariable Long id);

    @DeleteMapping("/task/policies/rate/category/{id}")
    ResponseEntity<RatePolicyForBookResponse> deleteRatePolicyForCategory(@PathVariable Long id);

    @DeleteMapping("/task/policies/price/book/{id}")
    ResponseEntity<RatePolicyForBookResponse> deletePricePolicyForBook(@PathVariable Long id);

    @DeleteMapping("/task/policies/price/category/{id}")
    ResponseEntity<RatePolicyForBookResponse> deletePricePolicyForCategory(@PathVariable Long id);

    @GetMapping("/task/policies/using")
    ResponseEntity<List<UsingPolicyResponse>> getUsingPolicies();
}
