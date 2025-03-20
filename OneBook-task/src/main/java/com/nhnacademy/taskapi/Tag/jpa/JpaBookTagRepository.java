package com.nhnacademy.taskapi.Tag.jpa;

import com.nhnacademy.taskapi.Tag.domain.Tag;
import com.nhnacademy.taskapi.book.domain.Book;
import com.nhnacademy.taskapi.book.domain.BookTag;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JpaBookTagRepository extends JpaRepository<BookTag, Long> {
    List<BookTag> findByBook(Book book);
    List<BookTag> findByTag(Tag tag);
}