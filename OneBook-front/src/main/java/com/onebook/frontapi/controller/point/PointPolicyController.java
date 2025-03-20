package com.onebook.frontapi.controller.point;

import com.onebook.frontapi.dto.point.request.CreatePointPolicyRequest;
import com.onebook.frontapi.dto.point.request.UpdatePointPolicyRequest;
import com.onebook.frontapi.feign.point.PointPolicyFeignClient;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/point")
public class PointPolicyController {

    private final PointPolicyFeignClient pointPolicyFeignClient;

    // 포인트 정책 등록 화면
    @GetMapping("/createPointPolicy")
    public String createPointPolicyForm(Model model) {
        model.addAttribute("pointPolicyRequest", new CreatePointPolicyRequest());
        return "admin/point/createPointPolicy";  // createPointPolicy.html 뷰 반환
    }

    // 포인트 정책 수정 화면
    @GetMapping("/updatePointPolicy")
    public String updatePointPolicyForm(Model model) {
        // 필요에 따라 수정할 정책 데이터를 가져옴
        // 예시: model.addAttribute("pointPolicyRequest", ...);
        return "admin/point/updatePointPolicy";  // updatePointPolicy.html 뷰 반환
    }

    // 포인트 정책 삭제 화면
    @GetMapping("/deletePointPolicy")
    public String deletePointPolicyForm(Model model) {
        // 필요한 데이터를 모델에 담아서 삭제 화면으로 전달
        // 예시: model.addAttribute("pointPolicyRequest", ...);
        return "admin/point/deletePointPolicy";  // deletePointPolicy.html 뷰 반환
    }

    // 포인트 정책 목록 화면
    @GetMapping("/allPointPolicies")
    public String allPointPolicies(Model model,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size) {
        // 포인트 정책 목록을 가져와서 뷰에 전달
        var response = pointPolicyFeignClient.getAllPointPolicies(page, size);
        model.addAttribute("pointPolicies", response.getContent());
        return "admin/point/allPointPolicies";
    }


    // 포인트 정책 등록
    @PostMapping("/createPointPolicy")
    public String createPointPolicy(@Valid @ModelAttribute CreatePointPolicyRequest request,
                                    BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "admin/point/createPointPolicy";  // 오류가 있으면 등록 페이지로 돌아감
        }
        pointPolicyFeignClient.createPointPolicy(request);
        return "redirect:/admin/point/allPointPolicies";  // 등록 후 목록 페이지로 리다이렉트
    }

    // 포인트 정책 수정
    @PutMapping("/updatePointPolicy/{policyId}")
    public String updatePointPolicy(@PathVariable Long policyId, @Valid @ModelAttribute UpdatePointPolicyRequest request,
                                    BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "admin/point/updatePointPolicy";  // 오류가 있으면 수정 페이지로 돌아감
        }
        pointPolicyFeignClient.updatePointPolicy(policyId, request);
        return "redirect:/admin/point/allPointPolicies";  // 수정 후 목록 페이지로 리다이렉트
    }

    // 포인트 정책 삭제
    @DeleteMapping("/deletePointPolicy/{policyId}")
    public String deletePointPolicy(@PathVariable Long policyId) {
        pointPolicyFeignClient.deactivatePointPolicy(policyId);
        return "redirect:/admin/point/allPointPolicies";  // 삭제 후 목록 페이지로 리다이렉트
    }
}
