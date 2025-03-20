package com.nhnacademy.taskapi.grade.domain;

import com.nhnacademy.taskapi.grade.dto.GradeResponseDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "grades")
public class Grade {

    /**
     * 등급 ID
     * 1: 일반
     * 2: 로얄
     * 3: 골드
     * 4: 플래티넘
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="grade_id")
    private Integer id; // default(REGULAR) ID: 1

    @NotBlank
    @Column(name="name", unique=true)
    private String name;

    @Column(name="accumulate_rate", nullable=false)
    private int accumulationRate;

    @Column(name="description", nullable = false)
    private String description;

    public static Grade create(String name, int accumulationRate, String description) {
        return new Grade(name, accumulationRate, description);
    }

    public static Grade from(GradeResponseDto gradeResponseDto) {
        return new Grade(
                gradeResponseDto.id(),
                gradeResponseDto.name(),
                gradeResponseDto.accumulationRate(),
                gradeResponseDto.description()
        );
    }

    public void modifyGrade(String name, int accumulationRate, String description) {
        this.name = name;
        this.accumulationRate = accumulationRate;
        this.description = description;
    }

    private Grade(String name, int accumulationRate, String description) {
        this.name = name;
        this.accumulationRate = accumulationRate;
        this.description = description;
    }

}
