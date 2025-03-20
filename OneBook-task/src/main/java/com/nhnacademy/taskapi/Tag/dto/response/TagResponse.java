package com.nhnacademy.taskapi.Tag.dto.response;

import com.nhnacademy.taskapi.Tag.domain.Tag;
import lombok.Builder;

@Builder
public record TagResponse(Long tagId, String tagName) {
    public static TagResponse fromEntity(Tag tag) {
        return TagResponse.builder()
                .tagId(tag.getTagId())
                .tagName(tag.getName())
                .build();
    }
}