package com.onebook.frontapi.feign.point;

import com.onebook.frontapi.dto.point.request.CreatePointPolicyRequest;
import com.onebook.frontapi.dto.point.request.UpdatePointPolicyRequest;
import com.onebook.frontapi.dto.point.response.PointPolicyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "PointPolicyClient", url = "${onebook.gatewayUrl}")
public interface PointPolicyFeignClient {

    // 포인트 정책 생성
    @PostMapping("/task/point/policy")
    PointPolicyResponse createPointPolicy(@ModelAttribute CreatePointPolicyRequest request);

    // 포인트 정책 수정
    @PutMapping("/task/point/policy/{pointPolicyId}")
    PointPolicyResponse updatePointPolicy(@PathVariable("pointPolicyId") Long pointPolicyId,
                                          @RequestBody UpdatePointPolicyRequest request);

    // 포인트 정책 삭제
    @DeleteMapping("/task/point/policy/{pointPolicyId}")
    void deactivatePointPolicy(@PathVariable("pointPolicyId") Long pointPolicyId);

    // 포인트 정책 조회 (단일)
    @GetMapping("/task/point/policy/{pointPolicyId}")
    PointPolicyResponse getPointPolicyById(@PathVariable("pointPolicyId") Long pointPolicyId);

    // 모든 포인트 정책 조회 (페이지네이션)
    @GetMapping("/task/point/policy/all")
    Page<PointPolicyResponse> getAllPointPolicies(@RequestParam("page") int page,
                                                  @RequestParam("size") int size);

    // 활성화된 포인트 정책 조회
    @GetMapping("/task/point/active-policies")
    List<PointPolicyResponse> getActivePointPolicies();  // 추가된 메서드
}
