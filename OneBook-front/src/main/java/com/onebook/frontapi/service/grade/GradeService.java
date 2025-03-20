package com.onebook.frontapi.service.grade;

import com.onebook.frontapi.dto.grade.*;
import com.onebook.frontapi.feign.grade.GradeClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GradeService {

    private final GradeClient gradeClient;

    // name 으로 등급 조회 (마이페이지 등급용)
    public List<GradeResponse> getGrades() {
        List<GradeResponse> result = new ArrayList<>();

        List<GradeFeignResponse> gradeFeignResponseList = gradeClient.getAllRequest();
        for(GradeFeignResponse grd : gradeFeignResponseList) {
            GradeResponse gvd = GradeResponse.from(grd);
            result.add(gvd);
        }

        return result;
    }

    /**
     * 관리자 - 등급 단일 조회
     */
    public GradeResponseForAdmin getGradeById(Integer gradeId) {
        return gradeClient.getGradeById(gradeId);
    }

    /**
     * 관리자 - 등급 전체 조회
     */
    public Page<GradeResponseForAdmin> getAllGradesForAdmin(Pageable pageable) {
        Page<GradeFeignResponse> result = gradeClient.getAllForAdmin(pageable);
        return result.map(GradeResponseForAdmin::from);
    }

    /**
     * 관리자 - 등급 등록
     */
    public GradeResponseForAdmin registerGrade(GradeRegisterRequest gradeRegisterRequest) {
        return gradeClient.registerGrade(gradeRegisterRequest);
    }

    /**
     * 관리자 - 등급 수정
     */
    public GradeResponseForAdmin modifyGrade(Integer id, GradeModifyRequest gradeModifyRequest) {
        return gradeClient.modifyGrade(id, gradeModifyRequest);
    }

    /**
     * 관리자 - 등급 삭제
     */
    public void removeGrade(Integer id) {
        gradeClient.removeGrade(id);
    }
}
