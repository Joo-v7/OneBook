package com.nhnacademy.taskapi.grade.repository;

import com.nhnacademy.taskapi.grade.domain.Grade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GradeRepository extends JpaRepository<Grade, Integer> {
    boolean existsByName(String name);
    Optional<Grade> findGradeByName(String name);
    Page<Grade> findAll(Pageable pageable);
}
