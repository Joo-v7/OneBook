package com.nhnacademy.taskapi.book.service;

import com.nhnacademy.taskapi.book.domain.BookTag;

public interface BookTagService {
    BookTag addBookTag(long bookId, long tagId);
    BookTag getBookTag(long bookId);
}
