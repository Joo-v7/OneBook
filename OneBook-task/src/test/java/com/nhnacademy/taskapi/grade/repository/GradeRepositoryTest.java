package com.nhnacademy.taskapi.grade.repository;

import com.nhnacademy.taskapi.config.QuerydslConfig;
import com.nhnacademy.taskapi.grade.domain.Grade;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
@Import(QuerydslConfig.class)

public class GradeRepositoryTest {

    @Autowired
    private GradeRepository gradeRepository;

    @Test
    @DisplayName("Find Grade")
    void findByIdTest() {
        Grade grade = Grade.create("ROYAL", 10, "일반 등급");
        Grade savedGrade = gradeRepository.save(grade);

        Grade getGrade = gradeRepository.findById(savedGrade.getId()).get();
        if(Objects.isNull(getGrade)) {
            log.error("grade를 찾을 수 없음");
        }
        assertThat(getGrade.getId()).isEqualTo(savedGrade.getId());
        assertThat(getGrade.getAccumulationRate()).isEqualTo(10);
        assertThat(getGrade.getDescription()).isEqualTo("일반 등급");
    }

    @Test
    @DisplayName("Find All Grade")
    void findAllTest() {
        Grade grade = Grade.create("ROYAL", 10, "일반 등급");
        gradeRepository.save(grade);

        List<Grade> getGradeList = gradeRepository.findAll();
        Grade target = getGradeList.get(0);

       assertThat(target.getName()).isEqualTo("ROYAL");
       assertThat(target.getAccumulationRate()).isEqualTo(10);
       assertThat(target.getDescription()).isEqualTo("일반 등급");
    }

    @Test
    @DisplayName("Save Grade")
    void saveTest() {
        Grade grade = Grade.create("ROYAL", 10, "일반 등급");

        Grade result = gradeRepository.save(grade);

        assertThat(result.getName()).isEqualTo(grade.getName());
        assertThat(result.getAccumulationRate()).isEqualTo(grade.getAccumulationRate());
        assertThat(result.getDescription()).isEqualTo(grade.getDescription());
    }

    @Test
    @DisplayName("Delete Grade")
    void deleteTest() {
        Grade grade = Grade.create("ROYAL", 10, "일반 등급");
        Grade target = gradeRepository.save(grade);

        gradeRepository.delete(target);

        assertThat(gradeRepository.findById(target.getId())).isEmpty();
    }

    @Test
    @DisplayName("Exists Grade")
    void existsTest() {
        Grade grade = Grade.create("ROYAL", 10, "일반 등급");
        Grade savedGrade = gradeRepository.save(grade);

        if(gradeRepository.existsByName("ROYAL")) {
            log.info("exists grade");
        }else {
            log.error("don't exists grade");
        }
    }
}
