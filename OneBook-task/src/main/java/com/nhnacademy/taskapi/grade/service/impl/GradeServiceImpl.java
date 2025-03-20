package com.nhnacademy.taskapi.grade.service.impl;

import com.nhnacademy.taskapi.grade.domain.Grade;
import com.nhnacademy.taskapi.grade.dto.GradeModifyRequestDto;
import com.nhnacademy.taskapi.grade.dto.GradeRegisterRequestDto;
import com.nhnacademy.taskapi.grade.dto.GradeResponseDto;
import com.nhnacademy.taskapi.grade.exception.GradeIllegalArgumentException;
import com.nhnacademy.taskapi.grade.exception.GradeNotFoundException;
import com.nhnacademy.taskapi.grade.repository.GradeRepository;
import com.nhnacademy.taskapi.grade.service.GradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class GradeServiceImpl implements GradeService {

    private final GradeRepository gradeRepository;

    // 모든 등급 조회
    @Transactional(readOnly = true)
    @Override
    public List<GradeResponseDto> getAllGrades() {
        List<Grade> gradeList = gradeRepository.findAll();
        List<GradeResponseDto> gradeResponseDtoList = new ArrayList<>();

        if(gradeList.isEmpty()) {
            return gradeResponseDtoList;
        }

        for(Grade g : gradeList) {
            GradeResponseDto gradeResponseDto = GradeResponseDto.from(g);
            gradeResponseDtoList.add(gradeResponseDto);
        }

        return gradeResponseDtoList;
    }

    // 관리자 - 모든 등급 조회
    @Transactional(readOnly = true)
    @Override
    public Page<GradeResponseDto> getAllGradesForAdmin(Pageable pageable) {
        Page<Grade> grades = gradeRepository.findAll(pageable);

        return grades.map(GradeResponseDto::from);
    }

    // 인조키(id)로 등급 조회
    @Transactional(readOnly = true)
    @Override
    public GradeResponseDto getGradeById(Integer id) {
        if(!gradeRepository.existsById(id)) {
            throw new GradeIllegalArgumentException("Grade id does not exist");
        }

        Grade grade = gradeRepository.findById(id)
                .orElseThrow(()->new GradeNotFoundException("Not Found grade by" + id));

        return GradeResponseDto.from(grade);
    }

    // 이름(unique)로 등급 조회
    @Transactional(readOnly = true)
    @Override
    public GradeResponseDto getGradeByName(String name) {
        Grade grade = gradeRepository.findGradeByName(name)
                .orElseThrow(()-> new GradeNotFoundException("Not Found Grade by " + name));

        return GradeResponseDto.from(grade);
    }

    // default 등급(REGULAR) 가져오기 - default 등급(REGULAR)의 ID는 반드시 1.
    @Transactional(readOnly = true)
    @Override
    public GradeResponseDto getDefaultGrade() {
        Grade grade = gradeRepository.findById(1).orElseThrow(()-> new GradeNotFoundException("Not Found Default Grade(id:1)"));
        return GradeResponseDto.from(grade);
    }

//    // 중복 확인 - 회원 등급 이름(name)이 존재하는지 확인
//    @Transactional(readOnly = true)
//    @Override
//    public boolean isDuplicateName(String name) {
//        return gradeRepository.existsByName(name);
//    }

    // 등급 등록
    @Override
    public GradeResponseDto registerGrade(GradeRegisterRequestDto gradeRegisterRequestDto) {
        if(gradeRepository.existsByName(gradeRegisterRequestDto.name())) {
            throw new GradeIllegalArgumentException("Grade Name does Already exists");
        }

        Grade grade = Grade.create(
                gradeRegisterRequestDto.name(),
                gradeRegisterRequestDto.accumulationRate(),
                gradeRegisterRequestDto.description()
        );

        try {

            Grade result = gradeRepository.save(grade);
            return GradeResponseDto.from(result);

        }catch(DataIntegrityViolationException e) {
            throw new GradeIllegalArgumentException("Failed to save grade in the database: Invalid format");
        }
    }

    // 등급 수정
    @Override
    public GradeResponseDto modifyGrade(Integer id, GradeModifyRequestDto gradeModifyRequestDto) {
        Grade grade = gradeRepository.findById(id).orElseThrow(() -> new GradeNotFoundException("Not Found Grade by " + id));
        grade.modifyGrade(gradeModifyRequestDto.name(), gradeModifyRequestDto.accumulationRate(), gradeModifyRequestDto.description());

        return GradeResponseDto.from(grade);
    }

    // 회원 등급 삭제
    @Override
    public void removeGrade(Integer id) {
        Grade grade = gradeRepository.findById(id).orElseThrow(() -> new GradeNotFoundException("Not Found Grade by " + id));
        gradeRepository.delete(grade);
    }

}
