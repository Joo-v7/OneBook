package com.nhnacademy.taskapi.grade.service;

import com.nhnacademy.taskapi.grade.domain.Grade;
import com.nhnacademy.taskapi.grade.dto.GradeModifyRequestDto;
import com.nhnacademy.taskapi.grade.dto.GradeRegisterRequestDto;
import com.nhnacademy.taskapi.grade.dto.GradeResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GradeService {
    List<GradeResponseDto> getAllGrades();
    Page<GradeResponseDto> getAllGradesForAdmin(Pageable pageable);
    GradeResponseDto getDefaultGrade();
    GradeResponseDto getGradeById(Integer id);
    GradeResponseDto getGradeByName(String name);
    GradeResponseDto registerGrade(GradeRegisterRequestDto gradeRegisterRequestDto);
//    boolean isDuplicateName(String name);
GradeResponseDto modifyGrade(Integer id, GradeModifyRequestDto gradeModifyRequestDto);
    void removeGrade(Integer name);
}
