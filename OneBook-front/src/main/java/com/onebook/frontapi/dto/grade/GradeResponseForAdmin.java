package com.onebook.frontapi.dto.grade;

public record GradeResponseForAdmin(
        Integer id,
        String name,
        int accumulationRate,
        String description
) {
    public static GradeResponseForAdmin from(GradeFeignResponse gradeFeignResponse) {
        return new GradeResponseForAdmin(
                gradeFeignResponse.id(),
                gradeFeignResponse.name(),
                gradeFeignResponse.accumulationRate(),
                gradeFeignResponse.description()
        );
    }
}
