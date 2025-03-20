package com.nhnacademy.taskapi.member.dto;

import com.nhnacademy.taskapi.book.domain.Book;
import com.nhnacademy.taskapi.image.domain.Image;

public record MemberLikeViewDto(
        Book book,
        Image image
) {
}
