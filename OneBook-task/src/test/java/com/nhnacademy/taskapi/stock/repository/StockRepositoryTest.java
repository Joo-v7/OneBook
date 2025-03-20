package com.nhnacademy.taskapi.stock.repository;

import com.nhnacademy.taskapi.book.domain.Book;
import com.nhnacademy.taskapi.book.repository.BookRepository;
import com.nhnacademy.taskapi.config.QuerydslConfig;
import com.nhnacademy.taskapi.publisher.domain.Publisher;
import com.nhnacademy.taskapi.publisher.repository.PublisherRepository;
import com.nhnacademy.taskapi.stock.domain.Stock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)

public class StockRepositoryTest {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @Test
    void findByBook_bookId(){
        Publisher publisher = new Publisher();
        publisher.setName("test");
        publisherRepository.save(publisher);

        Book book = new Book();
        book.setTitle("test");
        book.setPrice(100);
        book.setContent("test");
        book.setAmount(10);
        book.setDescription("test");
        book.setPubdate(LocalDate.now());
        book.setIsbn13("1111111111111");
        book.setSalePrice(500);
        book.setPublisher(publisher);


        book = bookRepository.save(book);
//        book = bookRepository.save(book);

        Stock stock = new Stock();
        stock.setStock(100);
        stock.setBook(book);

        stock = stockRepository.save(stock);

        Stock result = stockRepository.findByBook_bookId(book.getBookId());

        assertThat(result).isNotNull();
        assertThat(result.getStock()).isEqualTo(100);
    }

}
