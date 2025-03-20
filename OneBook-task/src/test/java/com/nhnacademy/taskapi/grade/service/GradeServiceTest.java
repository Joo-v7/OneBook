package com.nhnacademy.taskapi.grade.service;

import com.nhnacademy.taskapi.grade.domain.Grade;
import com.nhnacademy.taskapi.grade.dto.GradeModifyRequestDto;
import com.nhnacademy.taskapi.grade.dto.GradeRegisterRequestDto;
import com.nhnacademy.taskapi.grade.dto.GradeResponseDto;
import com.nhnacademy.taskapi.grade.exception.GradeIllegalArgumentException;
import com.nhnacademy.taskapi.grade.repository.GradeRepository;
import com.nhnacademy.taskapi.grade.service.impl.GradeServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class GradeServiceTest {

    @Mock
    private GradeRepository gradeRepository;

    @InjectMocks
    private GradeServiceImpl gradeService;

    @Test
    @DisplayName("Get All Grade Successfully")
    void getAllGradeTest1() {
        GradeResponseDto gradeResponseDto = new GradeResponseDto(1, "ROYAL", 10, "일반 등급");
        List<Grade> gradeList = Arrays.asList(Grade.from(gradeResponseDto));
        Mockito.when(gradeRepository.findAll()).thenReturn(gradeList);

        List<GradeResponseDto> result = gradeService.getAllGrades();

        Mockito.verify(gradeRepository, Mockito.times(1)).findAll();

        assertThat(result.getFirst().id()).isEqualTo(1);
        assertThat(result.getFirst().name()).isEqualTo("ROYAL");
        assertThat(result.getFirst().accumulationRate()).isEqualTo(10);
        assertThat(result.getFirst().description()).isEqualTo("일반 등급");
    }

    @Test
    @DisplayName("Get All Grade Empty")
    void getAllGradeTest2() {
        List<Grade> gradeList = new ArrayList<>();
        Mockito.when(gradeRepository.findAll()).thenReturn(gradeList);

        List<GradeResponseDto> result = gradeService.getAllGrades();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Get Grade By Id Successfully")
    void getGradeByIdTest() {
        GradeResponseDto gradeResponseDto = new GradeResponseDto(1, "ROYAL", 10, "일반 등급");
        Mockito.when(gradeRepository.findById(1)).thenReturn(Optional.of(Grade.from(gradeResponseDto)));
        Mockito.when(gradeRepository.existsById(1)).thenReturn(true);

        GradeResponseDto result = gradeService.getGradeById(1);

        Mockito.verify(gradeRepository, Mockito.times(1)).findById(1);
        assertThat(result.id()).isEqualTo(1);
        assertThat(result.name()).isEqualTo("ROYAL");
        assertThat(result.accumulationRate()).isEqualTo(10);
        assertThat(result.description()).isEqualTo("일반 등급");
    }

    @Test
    @DisplayName("Get Grade Failed - doesn't exists id")
    void failedGetOneGradeFailed() {
        Mockito.when(gradeRepository.existsById(1)).thenReturn(false);

        Assertions.assertThrows(GradeIllegalArgumentException.class, ()->gradeService.getGradeById(1));
    }

    @Test
    @DisplayName("Get Default Grade Successfully")
    void getDefaultGradeTest() {
        GradeResponseDto gradeResponseDto = new GradeResponseDto(1, "ROYAL", 10, "일반 등급");
        Mockito.when(gradeRepository.findById(1)).thenReturn(Optional.of(Grade.from(gradeResponseDto)));

        GradeResponseDto result = gradeService.getDefaultGrade();

        Mockito.verify(gradeRepository, Mockito.times(1)).findById(1);
        assertThat(result.id()).isEqualTo(1);
        assertThat(result.name()).isEqualTo("ROYAL");
        assertThat(result.accumulationRate()).isEqualTo(10);
        assertThat(result.description()).isEqualTo("일반 등급");
    }

//    @Test
//    @DisplayName("Duplicate Grade by name")
//    void isDuplicateNameTest() {
//        Mockito.when(gradeRepository.existsByName(Mockito.anyString())).thenReturn(true);
//
//        if(gradeService.existsByName("test")) {
//            log.info("grade exists");
//        }else {
//            log.info("grade doesn't exist");
//        }
//    }

    @Test
    @DisplayName("Register Grade Successfully")
    void registerGradeTest() {
        GradeResponseDto gradeResponseDto = new GradeResponseDto(1, "ROYAL", 10, "일반 등급");

        Mockito.when(gradeRepository.existsByName(Mockito.anyString())).thenReturn(false);
        Mockito.when(gradeRepository.save(Mockito.any())).thenReturn(Grade.from(gradeResponseDto));

        GradeResponseDto result = gradeService.registerGrade(new GradeRegisterRequestDto("test", 10, "test"));

        Mockito.verify(gradeRepository, Mockito.times(1)).save(Mockito.any());
        assertThat(result.id()).isEqualTo(1);
        assertThat(result.name()).isEqualTo("ROYAL");
        assertThat(result.accumulationRate()).isEqualTo(10);
        assertThat(result.description()).isEqualTo("일반 등급");
    }

    @Test
    @DisplayName("Register Grade Failed 1 - Name Not Found")
    void registerGradeFailedTest1() {
        Mockito.when(gradeRepository.existsByName(Mockito.any())).thenReturn(true);

        Assertions.assertThrows(GradeIllegalArgumentException.class,
                () -> gradeService.registerGrade(new GradeRegisterRequestDto("test", 1, "test"))
        );
    }

    @Test
    @DisplayName("Register Grade Failed 2 - DB error")
    void registerGradeFailedTest2() {
        Mockito.when(gradeRepository.existsByName(Mockito.anyString())).thenReturn(false);
        Mockito.when(gradeRepository.save(Mockito.any()))
                .thenThrow(DataIntegrityViolationException.class);

        Assertions.assertThrows(GradeIllegalArgumentException.class,
                ()->gradeService.registerGrade(new GradeRegisterRequestDto("test", 1, "test"))
        );
    }

    @Test
    @DisplayName("Modify Grade Successfully")
    void modifyGradeTest() {
        GradeResponseDto gradeResponseDto = new GradeResponseDto(1, "ROYAL", 10, "일반 등급");

        Mockito.when(gradeRepository.findById(1)).thenReturn(Optional.of(Grade.from(gradeResponseDto)));

        GradeResponseDto result = gradeService.modifyGrade(1, new GradeModifyRequestDto("hello", 1, "world"));

        Mockito.verify(gradeRepository, Mockito.times(1)).findById(Mockito.any());
        assertThat(result.id()).isEqualTo(1);
        assertThat(result.name()).isEqualTo("hello");
        assertThat(result.accumulationRate()).isEqualTo(1);
        assertThat(result.description()).isEqualTo("world");
    }

//    @Test
//    @DisplayName("Modify Grade Failed - Db error")
//    void modifyGradeFailedTest() {
//        Grade grade = Grade.create("test", 1, "test");
//        Mockito.when(gradeRepository.existsById(Mockito.anyInt())).thenReturn(true);
//        Mockito.when(gradeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(grade));
//        Mockito.when(gradeRepository.save(Mockito.any())).thenThrow(DataIntegrityViolationException.class);
//
//        assertThrows(GradeDataIntegrityViolationException.class,
//                ()->gradeService.modifyGrade(1, new GradeModifyRequestDto("test", 1, "test"))
//                );
//
//    }

    @Test
    @DisplayName("Remove Grade Successfully")
    void RemoveGrade() {
        GradeResponseDto gradeResponseDto = new GradeResponseDto(1, "ROYAL", 10, "일반 등급");

        Mockito.when(gradeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(Grade.from(gradeResponseDto)));

        gradeService.removeGrade(Mockito.anyInt());

        Mockito.verify(gradeRepository, Mockito.times(1)).delete(Mockito.any());
    }


}
