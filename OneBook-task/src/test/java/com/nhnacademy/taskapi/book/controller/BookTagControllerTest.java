package com.nhnacademy.taskapi.book.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.taskapi.book.domain.BookTag;
import com.nhnacademy.taskapi.book.service.BookTagService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(BookTagController.class)
public class BookTagControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookTagService bookTagService;


    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetBookTag() throws Exception {
        // Given
        long bookId = 1L;
        BookTag bookTag = new BookTag();
        bookTag.setBookTagId(1L);
        when(bookTagService.getBookTag(bookId)).thenReturn(bookTag);

        // When & Then
        mockMvc.perform(get("/task/book/tag/{bookId}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookTagId").value(1L));
    }

}
