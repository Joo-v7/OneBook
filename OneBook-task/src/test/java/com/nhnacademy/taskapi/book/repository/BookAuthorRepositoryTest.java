package com.nhnacademy.taskapi.book.repository;

import com.nhnacademy.taskapi.author.domain.Author;
import com.nhnacademy.taskapi.author.repository.AuthorRepository;
import com.nhnacademy.taskapi.book.domain.Book;
import com.nhnacademy.taskapi.book.domain.BookAuthor;
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

public class BookAuthorRepositoryTest {

    @Autowired
    private BookAuthorRepository bookAuthorRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Test
    void findByBook_bookIdAndAuthor_authorIdTest() {
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

        Author author = new Author();
        author.setName("Test Author");

        authorRepository.save(author);


        BookAuthor bookAuthor = new BookAuthor();
        bookAuthor.setBook(book);
        bookAuthor.setAuthor(author);

        // Save BookAuthor
        bookAuthorRepository.save(bookAuthor);

        // When
        BookAuthor result = bookAuthorRepository.findByBook_bookIdAndAuthor_authorId(book.getBookId(), author.getAuthorId());

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getBook().getTitle()).isEqualTo("Test Book");
        assertThat(result.getAuthor().getName()).isEqualTo("Test Author");
    }

    @Test
    void deleteByBookTest() {
        Publisher publisher = new Publisher();
        publisher.setName("test");
        publisherRepository.save(publisher);
        // Given
        Book book = new Book();
        book.setTitle("Test Book for Deletion");
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

        Author author = new Author();
        author.setName("Test Author");
        authorRepository.save(author);

        BookAuthor bookAuthor = new BookAuthor();
        bookAuthor.setBook(book);
        bookAuthor.setAuthor(author);

        // Save BookAuthor
        bookAuthorRepository.save(bookAuthor);

        // When
        bookAuthorRepository.deleteByBook(book);

        // Then
        BookAuthor result = bookAuthorRepository.findByBook_bookIdAndAuthor_authorId(book.getBookId(), author.getAuthorId());
        assertThat(result).isNull();  // 삭제 후, 해당 BookAuthor가 없음을 확인
    }
}
