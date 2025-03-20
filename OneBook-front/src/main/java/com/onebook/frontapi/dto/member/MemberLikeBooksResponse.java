package com.onebook.frontapi.dto.member;

import com.onebook.frontapi.dto.book.BookDTO;
import com.onebook.frontapi.dto.image.ImageDTO;

public record MemberLikeBooksResponse(
        BookDTO book,
        ImageDTO image
) {
}
