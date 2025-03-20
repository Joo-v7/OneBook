package com.onebook.frontapi.dto.role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RoleRegisterRequest(

        @NotBlank(message = "역할 이름은 필수 입력 항목입니다.")
        @Size(max=10, message = "역할 이름은 10자 이하로 입력 가능합니다.")
        String name,

        @NotBlank(message = "역할 설명은 필수 입력 항목입니다.")
        @Size(max=200, message = "역할 설명 200자 이하로 입력 가능합니다.")
        String description

) { }
