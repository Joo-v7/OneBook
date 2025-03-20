package com.nhnacademy.taskapi.book.controller;


import com.nhnacademy.taskapi.book.domain.Book;
import com.nhnacademy.taskapi.book.domain.BookCategory;
import com.nhnacademy.taskapi.book.dto.BookCategorySaveDTO;
import com.nhnacademy.taskapi.book.service.BookCategoryService;
import com.nhnacademy.taskapi.category.domain.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(BookCategoryController.class)
public class BookCategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookCategoryService bookCategoryService;

    @Test
    @DisplayName("createBookCategory - 성공")
    void createBookCategory() throws Exception {
        // Arrange
        Book book = new Book();
        book.setBookId(1L);

        Category category = new Category();
        category.setCategoryId(1);

        BookCategory bookCategory = new BookCategory(1L, book, category);

        BookCategorySaveDTO saveDTO = new BookCategorySaveDTO(1, 1L);

        given(bookCategoryService.save(ArgumentMatchers.any(BookCategorySaveDTO.class)))
                .willReturn(bookCategory);

        // Act & Assert
        mockMvc.perform(post("/task/book/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "categoryId": 1,
                        "bookId": 1
                    }
                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookCategoryId").value(1))
                .andExpect(jsonPath("$.book.bookId").value(1))
                .andExpect(jsonPath("$.category.categoryId").value(1));

        // Verify
        verify(bookCategoryService).save(ArgumentMatchers.any(BookCategorySaveDTO.class));
    }


    @Test
    void testGetAllBookCategories() throws Exception {
        List<BookCategory> bookCategoryList;
        Page<BookCategory> bookCategoryPage;


        Book book1 = new Book();
        book1.setBookId(1L);
        book1.setTitle("Book 1");
        Book book2 = new Book();
        book2.setBookId(2L);
        book2.setTitle("Book 2");
        Category category = new Category();
        category.setCategoryId(1);
        category.setParentCategory(null);

        BookCategory bookCategory1 = new BookCategory(1L, book1, category);
        BookCategory bookCategory2 = new BookCategory(2L, book2, category);

        bookCategoryList = Arrays.asList(bookCategory1, bookCategory2);
        bookCategoryPage = new PageImpl<>(bookCategoryList, PageRequest.of(0, 2), bookCategoryList.size());
        // Given
        int categoryId = 1;
        Pageable pageable = PageRequest.of(0, 2); // Page 0, size 2
        given(bookCategoryService.getBookByCategory(categoryId, pageable)).willReturn(bookCategoryPage);

        // When & Then
        mockMvc.perform(get("/task/book/category")
                        .param("categoryId", String.valueOf(categoryId)) // Pass categoryId as request parameter
                        .param("page", "0") // Pageable parameter
                        .param("size", "2") // Pageable size
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // HTTP 200 OK
                .andExpect(jsonPath("$.content.length()").value(2)) // Check the size of content
                .andExpect(jsonPath("$.content[0].bookCategoryId").value(1)) // First bookCategoryId
                .andExpect(jsonPath("$.content[0].book.title").value("Book 1")) // First book title
                .andExpect(jsonPath("$.content[1].bookCategoryId").value(2)) // Second bookCategoryId
                .andExpect(jsonPath("$.content[1].book.title").value("Book 2")); // Second book title
    }

    @Test
    void testGetBookCategoryByBookId() throws Exception {
        // 테스트용 데이터 준비
        Book book = new Book();
        book.setBookId(1L);
        book.setTitle("Test Book");
        Category category = new Category();
        category.setCategoryId(1);
        category.setName("Fiction");

        BookCategory bookCategory = new BookCategory(1L, book, category);
        // Given
        long bookId = 1L;
        given(bookCategoryService.getBookCategoryByBookId(bookId)).willReturn(bookCategory);

        // When & Then
        mockMvc.perform(get("/task/book/category/{bookid}", bookId) // bookId를 경로 변수로 전달
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // HTTP 200 상태 확인
                .andExpect(jsonPath("$.bookCategoryId").value(1)) // bookCategoryId 검증
                .andExpect(jsonPath("$.book.title").value("Test Book")) // book.title 검증
                .andExpect(jsonPath("$.category.name").value("Fiction")); // category.name 검증
    }


}
