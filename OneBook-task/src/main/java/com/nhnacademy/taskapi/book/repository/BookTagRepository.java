package com.nhnacademy.taskapi.book.repository;

import com.nhnacademy.taskapi.book.domain.Book;
import com.nhnacademy.taskapi.book.domain.BookTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookTagRepository extends JpaRepository<BookTag, Long> {
    BookTag findByBook_BookIdAndTag_TagId(Long bookId, Long tagId);

    void deleteByBook(Book book);

    BookTag findByBook_BookId(Long bookId);
}