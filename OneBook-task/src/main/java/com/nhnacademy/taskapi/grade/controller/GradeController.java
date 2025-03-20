package com.nhnacademy.taskapi.grade.controller;

import com.nhnacademy.taskapi.grade.domain.Grade;
import com.nhnacademy.taskapi.grade.dto.GradeModifyRequestDto;
import com.nhnacademy.taskapi.grade.dto.GradeRegisterRequestDto;
import com.nhnacademy.taskapi.grade.dto.GradeResponseDto;
import com.nhnacademy.taskapi.grade.service.GradeService;
import com.nhnacademy.taskapi.member.service.MemberService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Tag(name = "Grade", description = "등급을 생성, 조회, 수정, 삭제")
public class GradeController {

    private final GradeService gradeService;

    // 전체 등급 조회
    @GetMapping("/task/grades/list")
    public ResponseEntity<List<GradeResponseDto>> getGrades() {
        List<GradeResponseDto> gradeResponseDtoList = gradeService.getAllGrades();
        return ResponseEntity.ok().body(gradeResponseDtoList);
    }

    // 등급 조회 - 페이지네이션
    @GetMapping("/task/admin/grades/list")
    public ResponseEntity<Page<GradeResponseDto>> getAllGrades(Pageable pageable) {
        Page<GradeResponseDto> allGradesForAdmin = gradeService.getAllGradesForAdmin(pageable);
        return ResponseEntity.ok(allGradesForAdmin);
    }

    // 인조키(id)로 단일 등급 조회 - url에 id 명시. for 관리자.
    @GetMapping("/task/admin/grades/{id}")
    public ResponseEntity<GradeResponseDto> getGradeById(@PathVariable Integer id) {
        GradeResponseDto gradeResponseDto = gradeService.getGradeById(id);
        return ResponseEntity.ok().body(gradeResponseDto);
    }

    // 이름(unique)로 등급 조회
//    @GetMapping("/{name}")
//    public ResponseEntity<GradeResponseDto> getGradeByName(@PathVariable String name) {
//        GradeResponseDto gradeResponseDto = gradeService.getGradeByName(name);
//        return ResponseEntity.ok(gradeResponseDto);
//    }

    // 등급 등록
    @PostMapping("/task/admin/grades")
    public ResponseEntity<GradeResponseDto> createGrade(@RequestBody @Valid GradeRegisterRequestDto gradeRegisterRequestDto) {
        GradeResponseDto gradeResponseDto = gradeService.registerGrade(gradeRegisterRequestDto);
        return ResponseEntity.ok().body(gradeResponseDto);
    }

    // 등급 수정
    @PutMapping("/task/admin/grades/{id}")
    public ResponseEntity<GradeResponseDto> updateGrade(@PathVariable Integer id, @RequestBody @Valid GradeModifyRequestDto gradeModifyRequestDto) {
        GradeResponseDto gradeResponseDto = gradeService.modifyGrade(id, gradeModifyRequestDto);
        return ResponseEntity.ok().body(gradeResponseDto);
    }

    // 등급 삭제
    @DeleteMapping("/task/admin/grades/{id}")
    public ResponseEntity<String> deleteGrade(@PathVariable Integer id) {
        gradeService.removeGrade(id);
        return ResponseEntity.noContent().build();
    }


}
