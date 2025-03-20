package com.onebook.frontapi.service.book;

import com.onebook.frontapi.dto.book.BookAuthorDTO;
import com.onebook.frontapi.feign.book.BookAuthorClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class BookAuthorService {
    private final BookAuthorClient bookAuthorClient;

    public BookAuthorDTO getBookAuthor(Long bookId) {
        return bookAuthorClient.getBookAuthor(bookId);
    }
}
