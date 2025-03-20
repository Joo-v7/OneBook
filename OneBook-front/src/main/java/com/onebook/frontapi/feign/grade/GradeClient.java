package com.onebook.frontapi.feign.grade;

import com.onebook.frontapi.dto.grade.GradeFeignResponse;
import com.onebook.frontapi.dto.grade.GradeModifyRequest;
import com.onebook.frontapi.dto.grade.GradeRegisterRequest;
import com.onebook.frontapi.dto.grade.GradeResponseForAdmin;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "gradeClient", url="${onebook.gatewayUrl}")
public interface GradeClient {
    @GetMapping("/task/grades/{name}")
    GradeFeignResponse getRequest(@PathVariable(name = "name") String name);

    @GetMapping("/task/grades/list")
    List<GradeFeignResponse> getAllRequest();

    /**
     * 관리자 - 등급 단일 조회 by Id
     */
    @GetMapping("/task/admin/grades/{id}")
    GradeResponseForAdmin getGradeById(@PathVariable("id") Integer id);

    /**
     * 관리자 - 등급 전체 조회
     */
    @GetMapping("/task/admin/grades/list")
    Page<GradeFeignResponse> getAllForAdmin(@RequestParam Pageable pageable);

    /**
     * 관리자 - 등급 등록
     */
    @PostMapping("/task/admin/grades")
    GradeResponseForAdmin registerGrade(@RequestBody GradeRegisterRequest gradeRegisterRequest);

    /**
     * 관리자 - 등급 수정
     */
    @PutMapping("/task/admin/grades/{id}")
    GradeResponseForAdmin modifyGrade(@PathVariable Integer id, @RequestBody GradeModifyRequest gradeModifyRequest);

    @DeleteMapping("/task/admin/grades/{id}")
    void removeGrade(@PathVariable Integer id);
}
