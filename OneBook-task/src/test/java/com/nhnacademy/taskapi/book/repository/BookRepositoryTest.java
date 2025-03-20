package com.nhnacademy.taskapi.book.repository;


import com.nhnacademy.taskapi.book.domain.Book;
import com.nhnacademy.taskapi.config.QuerydslConfig;
import com.nhnacademy.taskapi.publisher.domain.Publisher;
import com.nhnacademy.taskapi.publisher.repository.PublisherRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)

public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PublisherRepository publisherRepository;


    @Test
    void findByIsbn13Test(){
        Publisher publisher = new Publisher();
        publisher.setName("test");
        publisherRepository.save(publisher);

        Book book = new Book();
        book.setBookId(1L);
        book.setTitle("test");
        book.setPrice(100);
        book.setContent("test");
        book.setAmount(10);
        book.setDescription("test");
        book.setPubdate(LocalDate.now());
        book.setIsbn13("1111111111111");
        book.setSalePrice(500);
        book.setPublisher(publisher);

        bookRepository.save(book);

        Book result = bookRepository.findByIsbn13("1111111111111");

        assertThat(result).isNotNull();
        assertThat(result.getIsbn13()).isEqualTo("1111111111111");
    }

    @Test
    void findAllByOrderByAmountTest(){

        Publisher publisher = new Publisher();
        publisher.setName("test");
        publisherRepository.save(publisher);

        for (int i = 1; i <= 100; i++) {
            Book book = new Book();
            book.setTitle("test"+i);
            book.setPrice(100);
            book.setContent("test");
            book.setAmount(i);
            book.setDescription("test");
            book.setPubdate(LocalDate.now());
            book.setIsbn13("1111111111111");
            book.setSalePrice(500);
            book.setPublisher(publisher);
            bookRepository.save(book);
        }

        // 페이지 요청 설정
        Pageable pageable = PageRequest.of(0, 50); // 첫 번째 페이지, 한 페이지에 50개 항목

        // 쿼리 실행
        Page<Book> booksPage = bookRepository.findAllByOrderByAmount(pageable);

        // 결과 검증
        assertThat(booksPage).isNotNull();
        assertThat(booksPage.getTotalElements()).isGreaterThanOrEqualTo(50);
        assertThat(booksPage.getContent()).hasSize(50); // 페이지 크기 검증

        // 첫 번째 책의 판매량이 두 번째 책의 판매량보다 크다는 것을 확인
        assertThat(booksPage.getContent().get(0).getAmount())
                .isGreaterThanOrEqualTo(booksPage.getContent().get(1).getAmount());
    }

    @Test
    void findAllByOrderByPubdateTest(){
        // Given
        Publisher publisher = new Publisher();
        publisher.setName("Test Publisher");
        publisherRepository.save(publisher);

        Book book1 = new Book();
        book1.setTitle("Book 1");
        book1.setContent("Content 1");
        book1.setDescription("Description 1");
        book1.setIsbn13("978-1-234-56789-0");
        book1.setPrice(1000);
        book1.setSalePrice(800);
        book1.setAmount(100);
        book1.setViews(0);
        book1.setPubdate(java.time.LocalDate.of(2023, 1, 1));
        book1.setPublisher(publisher);

        Book book2 = new Book();
        book2.setTitle("Book 2");
        book2.setContent("Content 2");
        book2.setDescription("Description 2");
        book2.setIsbn13("978-1-234-56789-1");
        book2.setPrice(2000);
        book2.setSalePrice(1500);
        book2.setAmount(200);
        book2.setViews(0);
        book2.setPubdate(java.time.LocalDate.of(2024, 1, 1));
        book2.setPublisher(publisher);

        // Save books
        bookRepository.save(book1);
        bookRepository.save(book2);

        // When
        var pageable = PageRequest.of(0, 10, Sort.by("pubdate").descending());


    }
}
