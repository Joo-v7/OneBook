package com.onebook.frontapi.dto.author;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
public class AuthorDTO {
    private int authorId;
    private String name;
}
