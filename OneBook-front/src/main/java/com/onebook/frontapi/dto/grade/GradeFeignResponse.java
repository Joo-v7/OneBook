package com.onebook.frontapi.dto.grade;

public record GradeFeignResponse(

        /**
         * id
         * 1: 일반, 2: 로얄, 3: 골드, 4: 플래티넘
         */

        Integer id,
        String name,
        int accumulationRate,
        String description
) {}
