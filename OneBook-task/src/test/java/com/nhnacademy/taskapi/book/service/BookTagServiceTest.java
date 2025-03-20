package com.nhnacademy.taskapi.book.service;


import com.nhnacademy.taskapi.Tag.domain.Tag;
import com.nhnacademy.taskapi.Tag.repository.TagRepository;
import com.nhnacademy.taskapi.book.domain.Book;
import com.nhnacademy.taskapi.book.domain.BookTag;
import com.nhnacademy.taskapi.book.exception.BookTagAlreadyExistException;
import com.nhnacademy.taskapi.book.repository.BookRepository;
import com.nhnacademy.taskapi.book.repository.BookTagRepository;
import com.nhnacademy.taskapi.book.service.Impl.BookTagServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookTagServiceTest {
    @Mock
    private BookTagRepository bookTagRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private BookTagServiceImpl bookTagService;


    @Test
    @DisplayName("addBookTag_Success")
    public void addBookTag_Success() {
        Book book = new Book();
        book.setBookId(1L);
        Tag tag = new Tag();
        tag.setTagId(1L);

        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.of(book));
        when(tagRepository.findById(any(Long.class))).thenReturn(Optional.of(tag));

        BookTag bookTag = new BookTag();
        bookTag.setBook(book);
        bookTag.setTag(tag);

        when(bookTagRepository.findByBook_BookIdAndTag_TagId(any(Long.class), any(Long.class))).thenReturn(null);

        when(bookTagRepository.save(any(BookTag.class))).thenReturn(bookTag);

        BookTag result = bookTagService.addBookTag(book.getBookId(), tag.getTagId());
        Assertions.assertNotNull(result);
        verify(bookTagRepository).save(any(BookTag.class));
    }

    @Test
    @DisplayName("addBookTag_Fail_AlreadyExist")
    void addBookTag_Fail_AlreadyExist() {
        Book book = new Book();
        book.setBookId(1L);
        Tag tag = new Tag();
        tag.setTagId(1L);

        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.of(book));
        when(tagRepository.findById(any(Long.class))).thenReturn(Optional.of(tag));

        BookTag bookTag = new BookTag();
        bookTag.setBook(book);
        bookTag.setTag(tag);

        when(bookTagRepository.findByBook_BookIdAndTag_TagId(any(Long.class), any(Long.class))).thenReturn(bookTag);

        Assertions.assertThrows(BookTagAlreadyExistException.class, () -> bookTagService.addBookTag(book.getBookId(), tag.getTagId()));
    }
}
