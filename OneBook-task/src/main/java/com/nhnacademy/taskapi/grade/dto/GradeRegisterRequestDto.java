package com.nhnacademy.taskapi.grade.dto;


import jakarta.validation.constraints.*;

public record GradeRegisterRequestDto(

        @NotBlank(message="등급 이름은 필수 입력 항목입니다.")
        @Size(max=10, message = "등급 이름은 10자 이하로 입력 가능합니다.")
        String name,

        @NotNull(message = "적립률은 필수 입력 항목입니다.")
        @Min(value=0, message = "적립률은 0 이상이어야 합니다.")
        int accumulationRate,

        @NotBlank(message = "등급 설명은 필수 입력 항목입니다.")
        @Size(max=200, message = "등급 설명은 200자 이하로 입력 가능합니다.")
        String description

)
{}
