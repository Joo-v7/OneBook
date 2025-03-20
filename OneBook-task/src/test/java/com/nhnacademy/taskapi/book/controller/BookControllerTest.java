package com.nhnacademy.taskapi.book.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.taskapi.Tag.domain.Tag;
import com.nhnacademy.taskapi.author.domain.Author;
import com.nhnacademy.taskapi.book.domain.Book;
import com.nhnacademy.taskapi.book.dto.BookRecommendDto;
import com.nhnacademy.taskapi.book.dto.BookSaveDTO;
import com.nhnacademy.taskapi.book.dto.BookSearchAllResponse;
import com.nhnacademy.taskapi.book.dto.BookUpdateDTO;
import com.nhnacademy.taskapi.book.service.BookService;
import com.nhnacademy.taskapi.publisher.domain.Publisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(BookController.class)
public class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;


    @Autowired
    private ObjectMapper objectMapper;

    private BookSaveDTO bookSaveDTO;
    private Book book;


    @BeforeEach
    void setUp() {
        // BookSaveDTO 초기화
        bookSaveDTO = new BookSaveDTO();
        bookSaveDTO.setPublisher(Mockito.mock(Publisher.class));
        bookSaveDTO.setTitle("Test Book");
        bookSaveDTO.setContent("Test Content");
        bookSaveDTO.setDescription("Test Description");
        bookSaveDTO.setIsbn13("1234567890123");
        bookSaveDTO.setPrice(20000);
        bookSaveDTO.setPriceSales(18000);
        bookSaveDTO.setPubdate("2025-01-01");
        bookSaveDTO.setStock(50);
        bookSaveDTO.setCategoryId(1);
        bookSaveDTO.setAuthor(Mockito.mock(Author.class));
        bookSaveDTO.setTag(Mockito.mock(Tag.class));

        // Book 객체 초기화
        book = new Book();
        book.setBookId(1L);
        book.setTitle("Test Book");
        book.setContent("Test Content");
        book.setDescription("Test Description");
        book.setIsbn13("1234567890123");
        book.setPrice(20000);
        book.setSalePrice(18000);
        book.setPubdate(LocalDate.of(2025, 1, 1));
    }

    @Test
    void testAddBook() throws Exception {
        // MockMultipartFile 객체 생성
        MockMultipartFile dtoFile = new MockMultipartFile(
                "dto",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(bookSaveDTO)
        );

        MockMultipartFile imageFile = new MockMultipartFile(
                "image",
                "test-image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test-image-content".getBytes()
        );

        // bookService.saveBook() 모킹
        given(bookService.saveBook(any(BookSaveDTO.class), any(MockMultipartFile.class))).willReturn(book);

        // POST 요청 시뮬레이션
        mockMvc.perform(multipart("/task/book")
                        .file(dtoFile)
                        .file(imageFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isCreated()) // HTTP 201 상태 확인
                .andExpect(jsonPath("$.bookId").value(1L)) // bookId 확인
                .andExpect(jsonPath("$.title").value("Test Book")) // title 확인
                .andExpect(jsonPath("$.isbn13").value("1234567890123")); // isbn13 확인
    }

    @Test
    @DisplayName("Book 수정 테스트 - 성공")
    void modifyBook_Success() throws Exception {
        // Given
        long bookId = 1L;

        BookUpdateDTO bookUpdateDTO = new BookUpdateDTO();
        bookUpdateDTO.setTitle("Updated Title");
        bookUpdateDTO.setContent("Updated Content");
        bookUpdateDTO.setDescription("Updated Description");
        bookUpdateDTO.setPrice(30000);
        bookUpdateDTO.setSalePrice(25000);
        bookUpdateDTO.setStock(100);

        Book updatedBook = new Book();
        updatedBook.setBookId(bookId);
        updatedBook.setTitle(bookUpdateDTO.getTitle());
        updatedBook.setContent(bookUpdateDTO.getContent());
        updatedBook.setDescription(bookUpdateDTO.getDescription());
        updatedBook.setPrice(bookUpdateDTO.getPrice());
        updatedBook.setSalePrice(bookUpdateDTO.getSalePrice());

        Mockito.when(bookService.updateBook(Mockito.eq(bookId), Mockito.any(BookUpdateDTO.class)))
                .thenReturn(updatedBook);

        // When & Then
        mockMvc.perform(put("/task/book/{bookId}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(bookUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookId").value(bookId))
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.content").value("Updated Content"))
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.price").value(30000))
                .andExpect(jsonPath("$.salePrice").value(25000));

        Mockito.verify(bookService, Mockito.times(1))
                .updateBook(Mockito.eq(bookId), Mockito.any(BookUpdateDTO.class));
    }

    @Test
    @DisplayName("책 삭제 테스트 - 성공")
    void deleteBook_Success() throws Exception {
        long bookId = 1L;

        // Mocking
        Mockito.doNothing().when(bookService).deleteBook(bookId);

        mockMvc.perform(delete("/task/book/{bookId}", bookId))
                .andExpect(status().isNoContent());

        Mockito.verify(bookService, Mockito.times(1)).deleteBook(bookId);
    }

    @Test
    @DisplayName("베스트셀러 목록 조회 테스트 - 성공")
    void bestsellersBook_Success() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> mockBooks = new PageImpl<>(List.of(new Book(), new Book()));

        Mockito.when(bookService.bestSellerBooks(pageable)).thenReturn(mockBooks);

        mockMvc.perform(get("/task/book/bestsellers")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        Mockito.verify(bookService, Mockito.times(1)).bestSellerBooks(Mockito.any(Pageable.class));
    }

    @Test
    @DisplayName("신간 목록 조회 테스트 - 성공")
    void newBooks_Success() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> mockBooks = new PageImpl<>(List.of(new Book(), new Book()));

        Mockito.when(bookService.newBooks(pageable)).thenReturn(mockBooks);

        mockMvc.perform(get("/task/book/newbooks")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        Mockito.verify(bookService, Mockito.times(1)).newBooks(Mockito.any(Pageable.class));
    }

    @Test
    @DisplayName("Top 4 신간 목록 조회 테스트 - 성공")
    void top4Books_Success() throws Exception {
        List<Book> mockBooks = List.of(new Book(), new Book(), new Book(), new Book());

        Mockito.when(bookService.newBooksTop4()).thenReturn(mockBooks);

        mockMvc.perform(get("/task/book/newbooks/top4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(4));

        Mockito.verify(bookService, Mockito.times(1)).newBooksTop4();
    }

    @Test
    @DisplayName("Top 4 베스트셀러 목록 조회 테스트 - 성공")
    void bestsellersTop4_Success() throws Exception {
        List<Book> mockBooks = List.of(new Book(), new Book(), new Book(), new Book());

        Mockito.when(bookService.bestSellersTop4()).thenReturn(mockBooks);

        mockMvc.perform(get("/task/book/bestsellers/top4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(4));

        Mockito.verify(bookService, Mockito.times(1)).bestSellersTop4();
    }

    @Test
    @DisplayName("책 상세 조회 테스트 - 성공")
    void getBook_Success() throws Exception {
        long bookId = 1L;
        Book mockBook = new Book();
        mockBook.setBookId(bookId);
        mockBook.setTitle("Sample Book");

        Mockito.when(bookService.getBook(bookId)).thenReturn(mockBook);

        mockMvc.perform(get("/task/book/{bookId}", bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookId").value(bookId))
                .andExpect(jsonPath("$.title").value("Sample Book"));

        Mockito.verify(bookService, Mockito.times(1)).getBook(bookId);
    }

    @Test
    @DisplayName("책 목록 조회 테스트 - 성공")
    void getBookList_Success() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> mockBooks = new PageImpl<>(List.of(new Book(), new Book()));

        Mockito.when(bookService.findAllBooks(pageable)).thenReturn(mockBooks);

        mockMvc.perform(get("/task/book/book-list")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        Mockito.verify(bookService, Mockito.times(1)).findAllBooks(Mockito.any(Pageable.class));
    }

    @Test
    @DisplayName("책 검색 테스트 - 성공")
    void searchBooks_Success() throws Exception {
        String searchString = "Sample";
        BookSearchAllResponse b1 = new BookSearchAllResponse(
                1L, "Sample Book", "Author","desctription", 1000, 100, 50, LocalDate.now(), false

        );




        List<BookSearchAllResponse> mockBooks = List.of(
                b1,
                new BookSearchAllResponse());

        Mockito.when(bookService.searchBookAll(searchString)).thenReturn(mockBooks);

        mockMvc.perform(get("/task/book/search/all")
                        .param("searchString", searchString))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Sample Book"));

        Mockito.verify(bookService, Mockito.times(1)).searchBookAll(searchString);
    }

    @Test
    @DisplayName("추천 도서 조회 테스트 - 성공")
    void recommendBooks_Success() throws Exception {
        List<BookRecommendDto> mockBooks = List.of(
                new BookRecommendDto(1L, "Recommended Book 1"),
                new BookRecommendDto(2L, "Recommended Book 2"));

        Mockito.when(bookService.recommendBooks()).thenReturn(mockBooks);

        mockMvc.perform(get("/task/book/recommend"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Recommended Book 1"));

        Mockito.verify(bookService, Mockito.times(1)).recommendBooks();
    }


}