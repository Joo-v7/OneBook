package com.onebook.frontapi.dto.book;

import com.onebook.frontapi.dto.author.AuthorDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookAuthorDTO {
    private int bookAuthorId;
    private BookDTO book;
    private AuthorDTO author;
}
