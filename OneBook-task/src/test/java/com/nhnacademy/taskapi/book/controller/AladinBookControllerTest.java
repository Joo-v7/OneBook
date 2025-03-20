package com.nhnacademy.taskapi.book.controller;

import com.nhnacademy.taskapi.book.service.Impl.AladinBookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(AladinBookController.class)
public class AladinBookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AladinBookService aladinBookService;

    @BeforeEach
    void setUp() {
        // 테스트를 위한 초기화 작업
    }

    @Test
    public void testSaveBooksFromAladin_Success() throws Exception {
        // Mocking the service method for a successful scenario
        doNothing().when(aladinBookService).saveBookFromAladin();

        // Send POST request and verify the response
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/task/book/aladin")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()) // Expect HTTP 201 Created
                .andReturn();

        String response = result.getResponse().getContentAsString();
        assertEquals("Books saved successfully.", response); // Check response message
    }
}
