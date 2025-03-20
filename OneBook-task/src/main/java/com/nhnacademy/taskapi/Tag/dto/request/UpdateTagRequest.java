package com.nhnacademy.taskapi.Tag.dto.request;

import com.nhnacademy.taskapi.Tag.domain.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateTagRequest(

        @NotNull(message = "태그 아이디는 필수 입력 항목입니다.")
        Long tagId,

        @NotBlank(message = "태그 이름은 필수 입력 항목입니다.")
        String tagName
) {
    public Tag toEntity() {
        return Tag.builder()
                  .tagId(tagId)
                  .name(tagName)
                  .build();
    }
}
