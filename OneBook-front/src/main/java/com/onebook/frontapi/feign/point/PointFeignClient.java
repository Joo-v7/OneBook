package com.onebook.frontapi.feign.point;

import com.onebook.frontapi.dto.point.response.MemberPointResponse;
import com.onebook.frontapi.dto.point.request.PointUpdateRequest;
import com.onebook.frontapi.common.CommonResponse;
import com.onebook.frontapi.dto.point.response.PointLogPageResponseDto;
import com.onebook.frontapi.dto.point.response.PointLogResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "pointFeignClient", url = "${onebook.gatewayUrl}")
public interface PointFeignClient {

    // 포인트 내역 조회
    @GetMapping("/task/member/{userId}/point-logs")
    CommonResponse<List<MemberPointResponse>> getUserPointHistories(@PathVariable("userId") Long userId);

    // 사용자의 포인트 정보 조회
    @GetMapping("/task/member/{userId}/point")
    CommonResponse<MemberPointResponse> getUserPointById(@PathVariable("userId") Long userId);

    // 포인트 추가 (관리자 권한 필요)
    @PostMapping("/task/member/admin/points/{memberId}")
    CommonResponse<String> addPoints(@PathVariable("memberId") Long memberId, @RequestParam int amount, @RequestParam String updatedType);

    // 포인트 상태 업데이트
    @PutMapping("/task/member/admin/point/{userId}")
    CommonResponse<Void> updateUserPointStatus(@PathVariable("userId") Long userId, @RequestBody PointUpdateRequest pointUpdateRequest);

    // 포인트 삭제 (선택 사항)
    @DeleteMapping("/task/member/admin/point/{userId}")
    CommonResponse<Void> deleteUserPoint(@PathVariable("userId") Long userId);

    // 포인트 로그 조회
    @GetMapping("/task/member/point-logs")
    PointLogPageResponseDto getPointLogs(
            @RequestParam("page") int page,
            @RequestParam("size") int size
    );

    @GetMapping("/task/member/current-point")
    int getMemberPoint();
}
