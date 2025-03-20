package com.onebook.frontapi.dto.tag;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagDTO {
    private Long tagId;
    private String name;


    @Builder
    public TagDTO(Long tagId, String name) {
        this.tagId = tagId;
        this.name = name;
    }
}
