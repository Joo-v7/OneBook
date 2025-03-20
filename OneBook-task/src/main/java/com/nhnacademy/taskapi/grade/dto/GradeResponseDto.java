package com.nhnacademy.taskapi.grade.dto;

import com.nhnacademy.taskapi.grade.domain.Grade;

public record GradeResponseDto(

        /**
         * id
         * 1: 일반, 2: 로얄, 3: 골드, 4: 플래티넘
         */

        Integer id,
        String name,
        int accumulationRate,
        String description
)
{
     public static GradeResponseDto from(Grade grade) {
         return new GradeResponseDto(
                 grade.getId(), grade.getName(), grade.getAccumulationRate(), grade.getDescription()
         );
     }
}
