package com.nhnacademy.taskapi.book.service;


import com.nhnacademy.taskapi.author.domain.Author;
import com.nhnacademy.taskapi.author.repository.AuthorRepository;
import com.nhnacademy.taskapi.book.domain.Book;
import com.nhnacademy.taskapi.book.domain.BookAuthor;
import com.nhnacademy.taskapi.book.dto.BookAuthorCreateDTO;
import com.nhnacademy.taskapi.book.exception.BookAuthorAlreadyExistsException;
import com.nhnacademy.taskapi.book.repository.BookAuthorRepository;
import com.nhnacademy.taskapi.book.repository.BookRepository;
import com.nhnacademy.taskapi.book.service.Impl.BookAuthorServiceImpl;
import com.nhnacademy.taskapi.publisher.domain.Publisher;
import com.nhnacademy.taskapi.publisher.repository.PublisherRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookAuthorServiceTest {

    @Mock
    private BookAuthorRepository bookAuthorRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private PublisherRepository publisherRepository;

    @InjectMocks
    private BookAuthorServiceImpl bookAuthorService;


    @Test
    @DisplayName("createBookAuthor_Success")
    public void createBookAuthor_Success() {
        Publisher publisher = new Publisher();
        publisher.setName("test");

        Book book = new Book();
        book.setBookId(1L);
        book.setTitle("test");
        book.setPrice(100);
        book.setContent("test");
        book.setAmount(10);
        book.setDescription("test");
        book.setPubdate(java.time.LocalDate.now());
        book.setIsbn13("1111111111111");
        book.setSalePrice(500);
        book.setPublisher(publisher);


        Author author = new Author();
        author.setAuthorId(1);
        author.setName("existTest");

        when(bookRepository.findById(any(Long.class))).thenReturn(java.util.Optional.of(book));
        when(authorRepository.findById(any(Integer.class))).thenReturn(java.util.Optional.of(author));

        BookAuthorCreateDTO dto = new BookAuthorCreateDTO();
        dto.setAuthorId(author.getAuthorId());
        dto.setBookId(book.getBookId());

        BookAuthor bookAuthor = new BookAuthor();
        bookAuthor.setAuthor(author);
        bookAuthor.setBook(book);

        when(bookAuthorRepository.findByBook_bookIdAndAuthor_authorId(book.getBookId(), author.getAuthorId())).thenReturn(null);
        when(bookAuthorRepository.save(any(BookAuthor.class))).thenReturn(bookAuthor);

        BookAuthor result = bookAuthorService.createBookAuthor(dto);

        Assertions.assertNotNull(result);
    }

    @Test
    @DisplayName("createBookAuthor_Fail_AlreadyExist")
    void createBookAuthor_Fail_AlreadyExist() {
        Publisher publisher = new Publisher();
        publisher.setName("test");

        Book book = new Book();
        book.setBookId(1L);
        book.setTitle("test");
        book.setPrice(100);
        book.setContent("test");
        book.setAmount(10);
        book.setDescription("test");
        book.setPubdate(java.time.LocalDate.now());
        book.setIsbn13("1111111111111");
        book.setSalePrice(500);
        book.setPublisher(publisher);


        Author author = new Author();
        author.setAuthorId(1);
        author.setName("existTest");

        when(bookRepository.findById(any(Long.class))).thenReturn(java.util.Optional.of(book));
        when(authorRepository.findById(any(Integer.class))).thenReturn(java.util.Optional.of(author));

        BookAuthorCreateDTO dto = new BookAuthorCreateDTO();
        dto.setAuthorId(author.getAuthorId());
        dto.setBookId(book.getBookId());

        BookAuthor bookAuthor = new BookAuthor();
        bookAuthor.setAuthor(author);
        bookAuthor.setBook(book);

        when(bookAuthorRepository.findByBook_bookIdAndAuthor_authorId(book.getBookId(), author.getAuthorId())).thenReturn(bookAuthor);

        Assertions.assertThrows(BookAuthorAlreadyExistsException.class, () -> bookAuthorService.createBookAuthor(dto));
    }
}
