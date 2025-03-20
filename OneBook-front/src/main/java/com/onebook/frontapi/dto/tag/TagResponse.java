package com.onebook.frontapi.dto.tag;

import lombok.Builder;

@Builder
public record TagResponse(Long tagId, String tagName) {
    public static TagResponse fromEntity(TagDTO tag) {
        return TagResponse.builder()
                .tagId(tag.getTagId())
                .tagName(tag.getName())
                .build();
    }
}