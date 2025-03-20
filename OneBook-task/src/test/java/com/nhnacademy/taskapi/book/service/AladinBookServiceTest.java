package com.nhnacademy.taskapi.book.service;

import com.nhnacademy.taskapi.adapter.AladinApiAdapter;
import com.nhnacademy.taskapi.author.domain.Author;
import com.nhnacademy.taskapi.author.service.AuthorService;
import com.nhnacademy.taskapi.book.domain.Book;
import com.nhnacademy.taskapi.book.domain.BookAuthor;
import com.nhnacademy.taskapi.book.domain.BookCategory;
import com.nhnacademy.taskapi.book.dto.BookAladinDTO;
import com.nhnacademy.taskapi.book.dto.BookAuthorCreateDTO;
import com.nhnacademy.taskapi.book.dto.BookCategorySaveDTO;
import com.nhnacademy.taskapi.book.service.Impl.AladinBookService;
import com.nhnacademy.taskapi.category.domain.Category;
import com.nhnacademy.taskapi.category.service.CategoryService;
import com.nhnacademy.taskapi.publisher.domain.Publisher;
import com.nhnacademy.taskapi.publisher.service.PublisherService;
import com.nhnacademy.taskapi.stock.domain.Stock;
import com.nhnacademy.taskapi.stock.dto.StockCreateUpdateDTO;
import com.nhnacademy.taskapi.stock.service.StockService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AladinBookServiceTest {
    @Mock
    private PublisherService publisherService;
    @Mock
    private AuthorService authorService;
    @Mock
    private CategoryService categoryService;
    @Mock
    private BookAuthorService bookAuthorService;
    @Mock
    private BookCategoryService bookCategoryService;
    @Mock
    private StockService stockService;
    @Mock
    private AladinApiAdapter aladinApiAdapter;

    @InjectMocks
    private AladinBookService aladinBookService;



    @Test
    void testSaveAladin() {
        // Mock response from Aladin API
        String mockResponse = "{ \"item\": [" +
                "{ \"title\": \"Book Title\", \"author\": \"Author Name\", \"pubDate\": \"2023-12-01\", " +
                "\"description\": \"Book Description\", \"isbn13\": \"1234567890123\", \"priceSales\": 10000, " +
                "\"priceStandard\": 12000, \"categoryName\": \"Fiction\", \"publisher\": \"Publisher Name\", \"salesPoint\": 5000 }" +
                "] }";

        when(aladinApiAdapter.fetchAladinData(anyString())).thenReturn(mockResponse);

        List<BookAladinDTO> bookList = aladinBookService.saveAladin();

        // Verify the results
        assertThat(bookList).hasSize(1);
        BookAladinDTO book = bookList.get(0);
        assertThat(book.getTitle()).isEqualTo("Book Title");
        assertThat(book.getAuthorName()).isEqualTo("Author Name");
        assertThat(book.getPubdate()).isEqualTo(LocalDate.of(2023, 12, 1));
        assertThat(book.getPriceSales()).isEqualTo(10000);
        assertThat(book.getCategoryNames()).isEqualTo("Fiction");
        verify(aladinApiAdapter).fetchAladinData(anyString());
    }

//    @Test
//    public void testSaveBookFromAladin() {
//        // 1. Mock 데이터를 생성
//        String mockResponse = "{ \"item\": [ { \"title\": \"Book Title\", \"author\": \"Author Name\", \"pubDate\": \"2023-12-01\", \"description\": \"Book Description\", \"isbn13\": \"1234567890123\", \"priceSales\": 15000, \"priceStandard\": 20000, \"categoryName\": \"Category Name\", \"publisher\": \"Publisher Name\", \"salesPoint\": 1000 } ] }";
//
//        // 2. BookAladinDTO 객체를 생성하고 세터를 통해 값 설정
//        BookAladinDTO bookAladinDTO = new BookAladinDTO();
//        bookAladinDTO.setTitle("Book Title");
//        bookAladinDTO.setAuthorName("Author Name");
//        bookAladinDTO.setPubdate(LocalDate.of(2023, 12, 1));
//        bookAladinDTO.setDescription("Book Description");
//        bookAladinDTO.setIsbn13("1234567890123");
//        bookAladinDTO.setPriceSales(15000);
//        bookAladinDTO.setPriceStandard(20000);
//        bookAladinDTO.setCategoryNames("Category Name");
//        bookAladinDTO.setPublisherName("Publisher Name");
//        bookAladinDTO.setSalesPoint(1000);
//
//        List<BookAladinDTO> mockBookAladinDTOList = Arrays.asList(bookAladinDTO);
//
//        // 3. Mock 메서드 설정
//        when(aladinApiAdapter.fetchAladinData(anyString())).thenReturn(mockResponse);
//        when(publisherService.addPublisherByAladin(anyString())).thenReturn(new Publisher());
//        when(authorService.addAuthorByAladin(anyString())).thenReturn(new Author());
//        when(categoryService.addCategoryByAladin(anyString())).thenReturn(new Category());
//
//        // 4. 실제 메서드 실행
//        aladinBookService.saveBookFromAladin();
//
//        // 5. 결과 검증
//        // Mock된 데이터에 대해 실제로 서비스가 정상적으로 호출되었는지 확인
//        verify(publisherService, times(1)).addPublisherByAladin("Publisher Name");
//        verify(authorService, times(1)).addAuthorByAladin("Author Name");
//        verify(categoryService, times(1)).addCategoryByAladin("Category Name");
//        verify(bookAuthorService, times(1)).createBookAuthor(any());
//        verify(bookCategoryService, times(1)).save(any());
//        verify(stockService, times(1)).addStock(any());
//
//        // 추가적인 검증을 통해 Book 객체에 대한 검증을 할 수 있습니다.
//    }

}
