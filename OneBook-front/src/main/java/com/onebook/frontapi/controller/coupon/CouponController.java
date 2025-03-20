package com.onebook.frontapi.controller.coupon;

import com.onebook.frontapi.dto.coupon.request.coupon.CreateCouponRequest;
import com.onebook.frontapi.dto.coupon.response.coupon.CouponResponse;
import com.onebook.frontapi.service.coupon.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class CouponController {

    private final CouponService couponService;

    @GetMapping("/coupon")
    public String getAllCouponPage(@RequestParam(required = false, defaultValue = "0", value = "page") int pageNo, Model model ){

        Page<CouponResponse> couponList= couponService.getAllCoupons(pageNo);
        model.addAttribute("couponList",couponList);

        return "coupon/coupon/coupon-list";
    }

    @GetMapping("/coupon/rate-for-book/{policy-id}/create")
    public String getRateCouponForBookCreatePage(@PathVariable(name = "policy-id") Long policyId, Model model){

        model.addAttribute("policyId",policyId);
        return "coupon/coupon/rate-coupon-for-book-create-form";
    }

    @GetMapping("/coupon/rate-for-category/{policy-id}/create")
    public String getRateCouponForCategoryCreatePage(@PathVariable(name = "policy-id") Long policyId, Model model){

        model.addAttribute("policyId",policyId);
        return "coupon/coupon/rate-coupon-for-category-create-form";
    }

    @GetMapping("/coupon/price-for-book/{policy-id}/create")
    public String getPriceCouponForBookCreatePage(@PathVariable(name = "policy-id") Long policyId, Model model){

        model.addAttribute("policyId",policyId);
        return "coupon/coupon/price-coupon-for-book-create-form";
    }

    @GetMapping("/coupon/price-for-category/{policy-id}/create")
    public String getPriceCouponForCategoryCreatePage(@PathVariable(name = "policy-id") Long policyId, Model model){

        model.addAttribute("policyId",policyId);
        return "coupon/coupon/price-coupon-for-category-create-form";
    }

    @PostMapping("/coupon/rate-for-book/create")
    public String createRateCouponForBook(@ModelAttribute CreateCouponRequest createCouponRequest){
        couponService.createRateCouponForBook(createCouponRequest);
        return "redirect:/admin/coupon";
    }

    @PostMapping("/coupon/rate-for-category/create")
    public String createRateCouponForCategory(@ModelAttribute CreateCouponRequest createCouponRequest){
        couponService.createRateCouponForCategory(createCouponRequest);
        return "redirect:/admin/coupon";

    }

    @PostMapping("/coupon/price-for-book/create")
    public String createPriceCouponForBook(@ModelAttribute CreateCouponRequest createCouponRequest){
        couponService.createPriceCouponForBook(createCouponRequest);
        return "redirect:/admin/coupon";

    }

    @PostMapping("/coupon/price-for-category/create")
    public String createPriceCouponForCategory(@ModelAttribute CreateCouponRequest createCouponRequest){
        couponService.createPriceCouponForCategory(createCouponRequest);
        return "redirect:/admin/coupon";
    }

    @DeleteMapping("/coupon/{coupon-number}")
    public String deleteCoupon(@PathVariable(name = "coupon-number") String couponNumber){

        couponService.deleteCoupon(couponNumber);
        return "redirect:/admin/coupon";
    }


}
