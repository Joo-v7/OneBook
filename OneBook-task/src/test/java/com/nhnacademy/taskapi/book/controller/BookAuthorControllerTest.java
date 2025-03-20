package com.nhnacademy.taskapi.book.controller;


import com.nhnacademy.taskapi.author.domain.Author;
import com.nhnacademy.taskapi.book.domain.Book;
import com.nhnacademy.taskapi.book.domain.BookAuthor;
import com.nhnacademy.taskapi.book.dto.BookAuthorCreateDTO;
import com.nhnacademy.taskapi.book.service.BookAuthorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(BookAuthorController.class)
public class BookAuthorControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookAuthorService bookAuthorService;

    @Test
    @DisplayName("POST /book-author - 성공")
    void createBookAuthor_Success() throws Exception {
        Book book = new Book();
        book.setBookId(1L);

        Author author = new Author();
        author.setAuthorId(1);

        BookAuthor bookAuthor = new BookAuthor(1, book, author);
        BookAuthorCreateDTO bookAuthorCreateDTO = new BookAuthorCreateDTO(1L, 1);
        given(bookAuthorService.createBookAuthor(ArgumentMatchers.any(BookAuthorCreateDTO.class))).willReturn(bookAuthor);

        mockMvc.perform(post("/task/book/author")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
              {
                "bookId": 1,
                "authorId": 1
               }
            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookAuthorId").value(1))
                .andExpect(jsonPath("$.book.bookId").value(1))
                .andExpect(jsonPath("$.author.authorId").value(1));
        verify(bookAuthorService).createBookAuthor(ArgumentMatchers.any(BookAuthorCreateDTO.class));
    }

    @Test
    void testGetBookAuthor() throws Exception {
        Book book = new Book();
        book.setBookId(1L);
        book.setTitle("Test Book");

        Author author = new Author();
        author.setAuthorId(1);
        author.setName("Test Author");



        BookAuthor bookAuthor;
        bookAuthor = new BookAuthor();
        bookAuthor.setBookAuthorId(1);
        bookAuthor.setBook(book);  // 가정된 Book 객체
        bookAuthor.setAuthor(author);
        // Given
        given(bookAuthorService.getBookAuthorByBookId(1L)).willReturn(bookAuthor);

        // When & Then
        mockMvc.perform(get("/task/book/author/1")  // bookId가 1인 경우
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookAuthorId").value(1))
                .andExpect(jsonPath("$.book.bookId").value(1))
                .andExpect(jsonPath("$.book.title").value("Test Book"))
                .andExpect(jsonPath("$.author.authorId").value(1))
                .andExpect(jsonPath("$.author.name").value("Test Author"));
    }

}
