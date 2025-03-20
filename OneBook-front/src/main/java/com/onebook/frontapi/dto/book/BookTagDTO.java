package com.onebook.frontapi.dto.book;

import com.onebook.frontapi.dto.tag.TagDTO;
import com.onebook.frontapi.dto.tag.TagResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookTagDTO {
    private long bookTagId;
    private BookDTO book;
    private TagDTO tag;
}
