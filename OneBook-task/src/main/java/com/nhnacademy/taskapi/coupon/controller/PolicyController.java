package com.nhnacademy.taskapi.coupon.controller;

import com.nhnacademy.taskapi.coupon.domain.dto.policies.request.create.AddPricePolicyForBookRequest;
import com.nhnacademy.taskapi.coupon.domain.dto.policies.request.create.AddPricePolicyForCategoryRequest;
import com.nhnacademy.taskapi.coupon.domain.dto.policies.request.create.AddRatePolicyForBookRequest;
import com.nhnacademy.taskapi.coupon.domain.dto.policies.request.create.AddRatePolicyForCategoryRequest;
import com.nhnacademy.taskapi.coupon.domain.dto.policies.request.update.UpdatePricePolicyForBookRequest;
import com.nhnacademy.taskapi.coupon.domain.dto.policies.request.update.UpdatePricePolicyForCategoryRequest;
import com.nhnacademy.taskapi.coupon.domain.dto.policies.request.update.UpdateRatePolicyForBookRequest;
import com.nhnacademy.taskapi.coupon.domain.dto.policies.request.update.UpdateRatePolicyForCategoryRequest;
import com.nhnacademy.taskapi.coupon.domain.dto.policies.response.*;
import com.nhnacademy.taskapi.coupon.exception.CouponPolicyIllegalArgumentException;
import com.nhnacademy.taskapi.coupon.service.policies.PolicyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/task")
@Transactional
@Tag(name = "Policy", description = "쿠폰 정책을 생성, 조회, 수정, 삭제")  // API 그룹 설명 추가
public class PolicyController {

    private final PolicyService policyService;

    // Create
    @PostMapping("/policies/rate/book")
    public ResponseEntity<RatePolicyForBookResponse> addRatePolicyForBook
            (@Valid @RequestBody AddRatePolicyForBookRequest addRatePolicyForBookRequest, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            throw new CouponPolicyIllegalArgumentException(bindingResult.getAllErrors().getFirst().getDefaultMessage());
        }

        RatePolicyForBookResponse  addRatePolicyForBookResponse =
                policyService.addRatePolicyForBook(addRatePolicyForBookRequest);
        return ResponseEntity.ok(addRatePolicyForBookResponse);
    }

    @PostMapping("/policies/rate/category")
    public ResponseEntity<RatePolicyForCategoryResponse> addRatePolicyForCategory
            (@Valid @RequestBody AddRatePolicyForCategoryRequest addRatePolicyForCategoryRequest, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            throw new CouponPolicyIllegalArgumentException(bindingResult.getAllErrors().getFirst().getDefaultMessage());
        }

        RatePolicyForCategoryResponse addRatePolicyForCategoryResponse =
                policyService.addRatePolicyForCategory(addRatePolicyForCategoryRequest);
        return ResponseEntity.ok(addRatePolicyForCategoryResponse);
    }

    @PostMapping("/policies/price/book")
    public ResponseEntity<PricePolicyForBookResponse> addPricePolicyForBook
            (@Valid @RequestBody AddPricePolicyForBookRequest addPricePolicyForBookRequest, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            throw new CouponPolicyIllegalArgumentException(bindingResult.getAllErrors().getFirst().getDefaultMessage());
        }

        PricePolicyForBookResponse addPricePolicyForBookResponse =
                policyService.addPricePolicyForBook(addPricePolicyForBookRequest);
        return ResponseEntity.ok(addPricePolicyForBookResponse);
    }

    @PostMapping("/policies/price/category")
    public ResponseEntity<PricePolicyForCategoryResponse> addPricePolicyForCategory
            (@Valid @RequestBody AddPricePolicyForCategoryRequest addPricePolicyForCategoryRequest, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            throw new CouponPolicyIllegalArgumentException(bindingResult.getAllErrors().getFirst().getDefaultMessage());
        }

        PricePolicyForCategoryResponse addPricePolicyForCategoryResponse =
                policyService.addPricePolicyForCategory(addPricePolicyForCategoryRequest);
        return ResponseEntity.ok(addPricePolicyForCategoryResponse);
    }

    // Read All
    @GetMapping("/policies/rate/book")
    public ResponseEntity<Page<RatePolicyForBookResponse>> getRatePoliciesForBook
            (Pageable pageable)
    {
        Page<RatePolicyForBookResponse> response = policyService.getRatePoliciesForBook(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/policies/rate/category")
    public ResponseEntity<Page<RatePolicyForCategoryResponse>> getRatePoliciesForCategory
            (Pageable pageable)
    {
        Page<RatePolicyForCategoryResponse> responses = policyService.getRatePoliciesForCategory(pageable);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/policies/price/book")
    public ResponseEntity<Page<PricePolicyForBookResponse>> getPricePoliciesForBook
            (Pageable pageable)
    {
        Page<PricePolicyForBookResponse> responses = policyService.getPricePoliciesForBook(pageable);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/policies/price/category")
    public ResponseEntity<Page<PricePolicyForCategoryResponse>> getPricePoliciesForCategory
            (Pageable pageable)
    {
        Page<PricePolicyForCategoryResponse> responses = policyService.getPricePoliciesForCategory(pageable);
        return ResponseEntity.ok(responses);
    }

    // Read One
    @GetMapping("/policies/rate/book/{id}")
    public ResponseEntity<RatePolicyForBookResponse> getRatePolicyForBook(@PathVariable Long id)
    {
        RatePolicyForBookResponse response = policyService.getRatePolicyForBook(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/policies/rate/category/{id}")
    public ResponseEntity<RatePolicyForCategoryResponse> getRatePolicyForCategory(@PathVariable Long id)
    {
        RatePolicyForCategoryResponse response = policyService.getRatePolicyForCategory(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/policies/price/book/{id}")
    public ResponseEntity<PricePolicyForBookResponse> getPricePolicyForBook(@PathVariable Long id)
    {
        PricePolicyForBookResponse response = policyService.getPricePolicyForBook(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/policies/price/category/{id}")
    public ResponseEntity<PricePolicyForCategoryResponse> getPricePolicyForCategory(@PathVariable Long id)
    {
        PricePolicyForCategoryResponse response = policyService.getPricePolicyForCategory(id);
        return ResponseEntity.ok(response);
    }

    // Update
    @PutMapping("/policies/rate/book")
    public ResponseEntity<RatePolicyForBookResponse> updateRatePolicyForBook
        (@Valid @RequestBody UpdateRatePolicyForBookRequest updateRatePolicyForBookRequest, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            throw new CouponPolicyIllegalArgumentException(bindingResult.getAllErrors().getFirst().getDefaultMessage());
        }

        RatePolicyForBookResponse response = policyService.updateRatePolicyForBook(updateRatePolicyForBookRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/policies/rate/category")
    public ResponseEntity<RatePolicyForCategoryResponse> updateRatePolicyForCategory
            (@Valid @RequestBody UpdateRatePolicyForCategoryRequest updateRatePolicyForCategoryRequest, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            throw new CouponPolicyIllegalArgumentException(bindingResult.getAllErrors().getFirst().getDefaultMessage());
        }

        RatePolicyForCategoryResponse response = policyService.updateRatePolicyForCategory(updateRatePolicyForCategoryRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/policies/price/book")
    public ResponseEntity<PricePolicyForBookResponse> updatePricePolicyForBook
            (@Valid @RequestBody UpdatePricePolicyForBookRequest updatePricePolicyForBookRequest, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            throw new CouponPolicyIllegalArgumentException(bindingResult.getAllErrors().getFirst().getDefaultMessage());
        }

        PricePolicyForBookResponse response = policyService.updatePricePolicyForBook(updatePricePolicyForBookRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/policies/price/category")
    public ResponseEntity<PricePolicyForCategoryResponse> updatePricePolicyForCategory
            (@Valid @RequestBody UpdatePricePolicyForCategoryRequest updatePricePolicyForCategoryRequest, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            throw new CouponPolicyIllegalArgumentException(bindingResult.getAllErrors().getFirst().getDefaultMessage());
        }

        PricePolicyForCategoryResponse response = policyService.updatePricePolicyForCategory(updatePricePolicyForCategoryRequest);
        return ResponseEntity.ok(response);
    }

    // Delete
    @DeleteMapping("/policies/rate/book/{id}")
    public ResponseEntity<RatePolicyForBookResponse> deleteRatePolicyForBook(@PathVariable Long id)
    {
        RatePolicyForBookResponse response = policyService.deleteRatePolicyForBook(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/policies/rate/category/{id}")
    public ResponseEntity<RatePolicyForCategoryResponse> deleteRatePolicyForCategory(@PathVariable Long id)
    {
        RatePolicyForCategoryResponse response = policyService.deleteRatePolicyForCategory(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/policies/price/book/{id}")
    public ResponseEntity<PricePolicyForBookResponse> deletePricePolicyForBook(@PathVariable Long id)
    {
        PricePolicyForBookResponse response = policyService.deletePricePolicyForBook(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/policies/price/category/{id}")
    public ResponseEntity<PricePolicyForCategoryResponse> deletePricePolicyForCategory(@PathVariable Long id)
    {
        PricePolicyForCategoryResponse response = policyService.deletePricePolicyForCategory(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/policies/using")
    public ResponseEntity<List<UsingPolicyResponse>> getUsingPolicies(){

        List<UsingPolicyResponse> response = policyService.getUsingPolicy();
        return ResponseEntity.ok(response);
    }

}
