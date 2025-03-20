package com.nhnacademy.taskapi.book.repository;

import com.nhnacademy.taskapi.Tag.domain.Tag;
import com.nhnacademy.taskapi.Tag.repository.TagRepository;
import com.nhnacademy.taskapi.book.domain.Book;
import com.nhnacademy.taskapi.book.domain.BookTag;
import com.nhnacademy.taskapi.config.QuerydslConfig;
import com.nhnacademy.taskapi.publisher.domain.Publisher;
import com.nhnacademy.taskapi.publisher.repository.PublisherRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)

public class BookTagRepositoryTest {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private PublisherRepository publisherRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private BookTagRepository bookTagRepository;


    @Test
    void findByBook_BookIdAndTag_TagIdTest() {
        Publisher publisher = new Publisher();
        publisher.setName("test");
        publisherRepository.save(publisher);
        Book book = new Book();
        book.setTitle("Test Book");
        book.setContent("Test Content");
        book.setDescription("Test Description");
        book.setIsbn13("978-1-234-56789-0");
        book.setPrice(1000);
        book.setSalePrice(800);
        book.setAmount(100);
        book.setPublisher(publisher);
        book.setViews(0);
        book.setPubdate(java.time.LocalDate.of(2023, 1, 1));
        bookRepository.save(book);

        Tag tag = new Tag();
        tag.setName("Test Tag");
        tagRepository.save(tag);

        BookTag bookTag = new BookTag();
        bookTag.setBook(book);
        bookTag.setTag(tag);
        bookTagRepository.save(bookTag);

        // When
        BookTag result = bookTagRepository.findByBook_BookIdAndTag_TagId(book.getBookId(), tag.getTagId());

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getBook().getTitle()).isEqualTo("Test Book");
        assertThat(result.getTag().getName()).isEqualTo("Test Tag");
    }

    @Test
    void deleteByBookTest() {
        Publisher publisher = new Publisher();
        publisher.setName("test");
        publisherRepository.save(publisher);
        // Given
        Book book = new Book();
        book.setTitle("Test Book");
        book.setContent("Test Content");
        book.setDescription("Test Description");
        book.setIsbn13("978-1-234-56789-0");
        book.setPrice(1000);
        book.setSalePrice(800);
        book.setAmount(100);
        book.setViews(0);
        book.setPublisher(publisher);
        book.setPubdate(java.time.LocalDate.of(2023, 1, 1));
        bookRepository.save(book);

        Tag tag = new Tag();
        tag.setName("Test Tag");
        tagRepository.save(tag);

        BookTag bookTag = new BookTag();
        bookTag.setBook(book);
        bookTag.setTag(tag);
        bookTagRepository.save(bookTag);

        // When
        bookTagRepository.deleteByBook(book);
        BookTag result = bookTagRepository.findByBook_BookIdAndTag_TagId(book.getBookId(), tag.getTagId());

        // Then
        assertThat(result).isNull();
    }

}
