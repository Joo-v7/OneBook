package com.nhnacademy.taskapi.stock.service;


import com.nhnacademy.taskapi.book.domain.Book;
import com.nhnacademy.taskapi.book.exception.BookNotFoundException;
import com.nhnacademy.taskapi.book.repository.BookRepository;
import com.nhnacademy.taskapi.book.service.BookService;
import com.nhnacademy.taskapi.publisher.domain.Publisher;
import com.nhnacademy.taskapi.publisher.repository.PublisherRepository;
import com.nhnacademy.taskapi.stock.domain.Stock;
import com.nhnacademy.taskapi.stock.dto.StockCreateUpdateDTO;
import com.nhnacademy.taskapi.stock.exception.InvalidStockAmountException;
import com.nhnacademy.taskapi.stock.exception.StockDuplicateException;
import com.nhnacademy.taskapi.stock.exception.StockNotFoundException;
import com.nhnacademy.taskapi.stock.repository.StockRepository;
import com.nhnacademy.taskapi.stock.service.Impl.StockServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class StockServiceTest {
    @Mock
    private StockRepository stockRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private PublisherRepository publisherRepository;

    @InjectMocks
    private StockServiceImpl stockService;



    private Stock stock;
    private Book book;


    @BeforeEach
    void setUp() {
        Publisher publisher = new Publisher();
        publisher.setName("test");

        book = new Book();
        book.setTitle("test");
        book.setPrice(100);
        book.setContent("test");
        book.setAmount(10);
        book.setDescription("test");
        book.setPubdate(java.time.LocalDate.now());
        book.setIsbn13("1111111111111");
        book.setSalePrice(500);
        book.setPublisher(publisher);

        when(bookRepository.save(book)).thenReturn(book);
        book = bookRepository.save(book);
    }

    @Test
    @DisplayName("addStock_Success")
    void addStockSuccess() {
        Stock stock = new Stock();
        stock.setStock(100);
        stock.setBook(book);


        StockCreateUpdateDTO dto = new StockCreateUpdateDTO();
        dto.setBookId(stock.getBook().getBookId());
        dto.setAmount(stock.getStock());

        when(bookRepository.findById(book.getBookId())).thenReturn(java.util.Optional.of(book));

        when(stockRepository.findByBook_bookId(book.getBookId())).thenReturn(null);
        when(stockRepository.save(any(Stock.class))).thenReturn(stock);

        Stock result = stockService.addStock(dto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(stock.getStock(), result.getStock());
    }

    @Test
    @DisplayName("addStock_Fail_amount < 0")
    void addStockFail_amountLessThanZero() {
        StockCreateUpdateDTO dto = new StockCreateUpdateDTO();
        dto.setBookId(book.getBookId());
        dto.setAmount(-1);

        Assertions.assertThrows(InvalidStockAmountException.class, () -> stockService.addStock(dto));
    }

    @Test
    @DisplayName("addStock_Fail_BookNotFoundException")
    void addStockFail_BookNotFoundException() {
        StockCreateUpdateDTO dto = new StockCreateUpdateDTO();
        dto.setBookId(book.getBookId());
        dto.setAmount(10);

        when(bookRepository.findById(book.getBookId())).thenReturn(java.util.Optional.empty());

        Assertions.assertThrows(BookNotFoundException.class, () -> stockService.addStock(dto));
    }

    @Test
    @DisplayName("addStock_Fail_duplicateException")
    void addStockFail_duplicateException() {
        Stock stock = new Stock();
        stock.setStock(100);
        stock.setBook(book);
        StockCreateUpdateDTO dto = new StockCreateUpdateDTO();
        dto.setBookId(stock.getBook().getBookId());
        dto.setAmount(stock.getStock());

        when(bookRepository.findById(book.getBookId())).thenReturn(java.util.Optional.of(book));
        when(stockRepository.findByBook_bookId(book.getBookId())).thenReturn(stock);

        Assertions.assertThrows(StockDuplicateException.class, () -> stockService.addStock(dto));
    }

    @Test
    @DisplayName("updateStock_Success")
    void updateStockSuccess() {
        Stock stock = new Stock();
        stock.setStock(100);
        stock.setBook(book);

        when(stockRepository.findByBook_bookId(book.getBookId())).thenReturn(stock);

        StockCreateUpdateDTO dto = new StockCreateUpdateDTO();
        dto.setBookId(stock.getBook().getBookId());
        dto.setAmount(1000);

        when(stockRepository.save(any(Stock.class))).thenReturn(stock);

        Stock result = stockService.updateStock(dto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(dto.getAmount(), result.getStock());
        verify(stockRepository).save(any(Stock.class));
    }

    @Test
    @DisplayName("updateStock_Fail_amount < 0")
    void updateStockFail_amountLessThanZero() {
        Stock stock = new Stock();
        stock.setStock(100);
        stock.setBook(book);
        StockCreateUpdateDTO dto = new StockCreateUpdateDTO();
        dto.setBookId(stock.getBook().getBookId());
        dto.setAmount(-1);
        Assertions.assertThrows(InvalidStockAmountException.class, () -> stockService.updateStock(dto));
    }

    @Test
    @DisplayName("updateStock_Fail_StockNotFoundException")
    void updateStockFail_StockNotFoundException() {
        StockCreateUpdateDTO dto = new StockCreateUpdateDTO();
        dto.setBookId(book.getBookId());
        dto.setAmount(10);

        when(stockRepository.findByBook_bookId(book.getBookId())).thenReturn(null);

        Assertions.assertThrows(StockNotFoundException.class, () -> stockService.updateStock(dto));
    }
}
