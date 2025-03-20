package com.onebook.frontapi.dto.tag;

import jakarta.validation.constraints.NotBlank;

public record CreateTagRequest(

        @NotBlank(message = "태그 이름은 필수 입력 항목입니다.")
        String tagName
) {
    public TagDTO toEntity() {
        return TagDTO.builder()
                .tagId(null)
                .name(tagName)
                .build();
    }
}
