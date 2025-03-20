package com.onebook.frontapi.dto.grade;

public record GradeResponse(
        String name,
        int accumulationRate,
        String description
) {
    public static GradeResponse from(GradeFeignResponse gradeFeignResponse) {
        return new GradeResponse(
                gradeFeignResponse.name(),
                gradeFeignResponse.accumulationRate(),
                gradeFeignResponse.description()
        );
    }
}
