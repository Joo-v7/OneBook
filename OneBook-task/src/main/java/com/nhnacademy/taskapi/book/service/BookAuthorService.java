package com.nhnacademy.taskapi.book.service;

import com.nhnacademy.taskapi.book.domain.BookAuthor;
import com.nhnacademy.taskapi.book.dto.BookAuthorCreateDTO;

public interface BookAuthorService {
    BookAuthor createBookAuthor(BookAuthorCreateDTO dto);

    BookAuthor getBookAuthorByBookId(long bookId);

}
