package com.onebook.frontapi.controller.coupon;

import com.onebook.frontapi.dto.category.CategoryDTO;
import com.onebook.frontapi.dto.coupon.request.couponPolicy.*;
import com.onebook.frontapi.dto.coupon.response.couponPolicy.*;
import com.onebook.frontapi.service.coupon.CouponPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class CouponPolicyController {

    private final CouponPolicyService couponPolicyService;

    // 쿠폰 정책 등록
    // 정률정책forBook 등록 폼
    @GetMapping("/coupon-policies/rate/book/register")
    public String getRatePolicyForBookRegisterForm(){
        return "coupon/policy-register/rate-policy-for-book-register-form";
    }

    // 정률정책forCategory 등록 폼
    @GetMapping("/coupon-policies/rate/category/register")
    public String getRatePolicyForCategoryRegisterForm(Model model){

        List<CategoryDTO> categoriesForSelect = couponPolicyService.getCategoriesForSelect();
        model.addAttribute("categoriesForSelect",categoriesForSelect);
        return "coupon/policy-register/rate-policy-for-category-register-form";
    }

    // 정액정책forBook 등록 폼
    @GetMapping("/coupon-policies/price/book/register")
    public String getPricePolicyForBookRegisterForm(){

        return "coupon/policy-register/price-policy-for-book-register-form";
    }
    // 정액정책forCategory 등록 폼
    @GetMapping("/coupon-policies/price/category/register")
    public String getPricePolicyForCategoryRegisterForm(Model model){

        List<CategoryDTO> categoriesForSelect = couponPolicyService.getCategoriesForSelect();
        model.addAttribute("categoriesForSelect",categoriesForSelect);
        return "coupon/policy-register/price-policy-for-category-register-form";
    }

    // 정률정책 for Book 등록
    @PostMapping("/coupon-policies/rate/book/register")
    public String registerRatePolicyForBook(@ModelAttribute AddRatePolicyForBookRequest addRatePolicyForBookRequest){

        couponPolicyService.registerRatePolicyForBook(addRatePolicyForBookRequest);
        return "redirect:/admin/coupon-policies/rate/book";
    }

    // 정률정책 for category 등록
    @PostMapping("/coupon-policies/rate/category/register")
    public String registerRatePolicyForCategory(@ModelAttribute AddRatePolicyForCategoryRequest addRatePolicyForCategoryRequest){

        couponPolicyService.registerRatePolicyForCategory(addRatePolicyForCategoryRequest);
        return "redirect:/admin/coupon-policies/rate/category";
    }

    // 정액정책 for Book 등록
    @PostMapping("/coupon-policies/price/book/register")
    public String registerPricePolicyForBook(@ModelAttribute AddPricePolicyForBookRequest addPricePolicyForBookRequest){

        couponPolicyService.registerPricePolicyForBook(addPricePolicyForBookRequest);
        return "redirect:/admin/coupon-policies/price/book";
    }

    // 정액정책 for Category 등록
    @PostMapping("/coupon-policies/price/category/register")
    public String registerPricePolicyForCategory(@ModelAttribute AddPricePolicyForCategoryRequest addPricePolicyForCategoryRequest){

        couponPolicyService.registerPricePolicyForCategory(addPricePolicyForCategoryRequest);
        return "redirect:/admin/coupon-policies/price/category";
    }

    // 쿠폰 조회

    // 정률정책 for Book 조회
    @GetMapping("/coupon-policies/rate/book")
    public String getRatePoliciesForBook
    (@RequestParam(required = false, defaultValue = "0", value = "page") int pageNo, Model model){
        Page<RatePolicyForBookAndCouponCountResponse> policyList = couponPolicyService.getRatePoliciesForBook(pageNo);

        model.addAttribute("policyList",policyList.getContent());
        model.addAttribute("totalPages",policyList.getTotalPages());
        return "coupon/policy-list/rate-policy-for-book-list";
    }

    // 정률정책 for Category 조회
    @GetMapping("/coupon-policies/rate/category")
    public String getRatePoliciesForCategory
    (@RequestParam(required = false, defaultValue = "0", value = "page") int pageNo, Model model){
        Page<RatePolicyForCategoryAndCouponCountResponse> policyList = couponPolicyService.getRatePoliciesForCategory(pageNo);

        model.addAttribute("policyList",policyList.getContent());
        model.addAttribute("totalPages",policyList.getTotalPages());
        return "coupon/policy-list/rate-policy-for-category-list";
    }

    // 정액정책 for Book 조회
    @GetMapping("/coupon-policies/price/book")
    public String getPricePoliciesForBook
    (@RequestParam(required = false, defaultValue = "0", value = "page") int pageNo, Model model){
        Page<PricePolicyForBookAndCouponCountResponse> policyList = couponPolicyService.getPricePoliciesForBook(pageNo);

        model.addAttribute("policyList",policyList.getContent());
        model.addAttribute("totalPages",policyList.getTotalPages());
        return "coupon/policy-list/price-policy-for-book-list";
    }

    // 정액정책 for Category 조회
    @GetMapping("/coupon-policies/price/category")
    public String getPricePoliciesForCategory
    (@RequestParam(required = false, defaultValue = "0", value = "page") int pageNo, Model model){
        Page<PricePolicyForCategoryAndCouponCountResponse> policyList = couponPolicyService.getPricePoliciesForCategory(pageNo);

        model.addAttribute("policyList",policyList.getContent());
        model.addAttribute("totalPages",policyList.getTotalPages());
        return "coupon/policy-list/price-policy-for-category-list";
    }

    // 쿠폰 수정
    // 정률정책forBook 수정 폼
    @GetMapping("/coupon-policies/rate/book/modify/{id}")
    public String getRatePolicyForBookModifyForm(@PathVariable Long id, Model model){

        RatePolicyForBookResponse ratePolicyForBookResponse = couponPolicyService.getRatePolicyForBook(id);
        model.addAttribute("ratePolicyForBook",ratePolicyForBookResponse);
        return "coupon/policy-modify/rate-policy-for-book-modify-form";
    }

    // 정률정책forCategory 수정 폼
    @GetMapping("/coupon-policies/rate/category/modify/{id}")
    public String getRatePolicyForCategoryModifyForm(@PathVariable Long id,Model model){

        RatePolicyForCategoryResponse ratePolicyForCategoryResponse = couponPolicyService.getRatePolicyForCategory(id);
        model.addAttribute("ratePolicyForCategory",ratePolicyForCategoryResponse);
        List<CategoryDTO> categoriesForSelect = couponPolicyService.getCategoriesForSelect();
        model.addAttribute("categoriesForSelect",categoriesForSelect);
        return "coupon/policy-modify/rate-policy-for-category-modify-form";
    }

    // 정액정책forBook 수정 폼
    @GetMapping("/coupon-policies/price/book/modify/{id}")
    public String getPricePolicyForBookModifyForm(@PathVariable Long id,Model model){

        PricePolicyForBookResponse pricePolicyForBookResponse = couponPolicyService.getPricePolicyForBook(id);
        model.addAttribute("pricePolicyForBook",pricePolicyForBookResponse);
        return "coupon/policy-modify/price-policy-for-book-modify-form";
    }

    // 정액정책forCategory 수정 폼
    @GetMapping("/coupon-policies/price/category/modify/{id}")
    public String getPricePolicyForCategoryModifyForm(@PathVariable Long id,Model model){

        PricePolicyForCategoryResponse pricePolicyForCategoryResponse = couponPolicyService.getPricePolicyForCategory(id);
        model.addAttribute("pricePolicyForCategory",pricePolicyForCategoryResponse);
        List<CategoryDTO> categoriesForSelect = couponPolicyService.getCategoriesForSelect();
        model.addAttribute("categoriesForSelect",categoriesForSelect);
        return "coupon/policy-modify/price-policy-for-category-modify-form";
    }

    // 수정
    @PutMapping("/coupon-policies/rate/book/modify")
    public String modifyRatePolicyForBook(@ModelAttribute UpdateRatePolicyForBookRequest updateRatePolicyForBookRequest){

        couponPolicyService.updateRatePolicyForBook(updateRatePolicyForBookRequest);
        return "redirect:/admin/coupon-policies/rate/book";
    }

    @PutMapping("/coupon-policies/rate/category/modify")
    public String modifyRatePolicyForCategory(@ModelAttribute UpdateRatePolicyForCategoryRequest updateRatePolicyForCategoryRequest){

        couponPolicyService.updateRatePolicyForCategory(updateRatePolicyForCategoryRequest);
        return "redirect:/admin/coupon-policies/rate/category";
    }

    @PutMapping("/coupon-policies/price/book/modify")
    public String modifyPricePolicyForBook(@ModelAttribute UpdatePricePolicyForBookRequest updatePricePolicyForBookRequest){

        couponPolicyService.updatePricePolicyForBook(updatePricePolicyForBookRequest);
        return "redirect:/admin/coupon-policies/price/book";
    }

    @PutMapping("/coupon-policies/price/category/modify")
    public String modifyPricePolicyForCategory(@ModelAttribute UpdatePricePolicyForCategoryRequest updatePricePolicyForCategoryRequest){

        couponPolicyService.updatePricePolicyForCategory(updatePricePolicyForCategoryRequest);
        return "redirect:/admin/coupon-policies/price/category";
    }

    // 삭제

    @DeleteMapping("/coupon-policies/rate/book/{id}")
    public String deleteRatePolicyForBook(@PathVariable Long id){
        couponPolicyService.deleteRatePolicyForBook(id);
        return "redirect:/coupon-policies/rate/book";

    }

    @DeleteMapping("/coupon-policies/rate/category/{id}")
    public String deleteRatePolicyForCategory(@PathVariable Long id){
        couponPolicyService.deleteRatePolicyForCategory(id);
        return "redirect:/coupon-policies/rate/category";
    }

    @DeleteMapping("/coupon-policies/price/book/{id}")
    public String deletePricePolicyForBook(@PathVariable Long id){
        couponPolicyService.deletePricePolicyForBook(id);
        return "redirect:/coupon-policies/price/book";
    }

    @DeleteMapping("/coupon-policies/price/category/{id}")
    public String deletePricePolicyForCategory(@PathVariable Long id){
        couponPolicyService.deletePricePolicyForCategory(id);
        return "redirect:/coupon-policies/price/category";
    }
}
