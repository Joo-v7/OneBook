package com.onebook.frontapi.service.book;

import com.onebook.frontapi.dto.book.BookTagDTO;
import com.onebook.frontapi.feign.book.BookTagClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookTagService {
    private final BookTagClient bookTagClient;

    public BookTagDTO getBookTagByBookId(Long bookId) {
        return bookTagClient.getBookTagByBookId(bookId);
    }
}
