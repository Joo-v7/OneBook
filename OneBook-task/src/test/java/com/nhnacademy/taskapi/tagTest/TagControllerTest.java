//package com.nhnacademy.taskapi.tagTest;
//
//import com.nhnacademy.taskapi.Tag.controller.TagController;
//import com.nhnacademy.taskapi.Tag.dto.request.CreateTagRequest;
//import com.nhnacademy.taskapi.Tag.dto.request.UpdateTagRequest;
//import com.nhnacademy.taskapi.Tag.dto.response.TagResponse;
//import com.nhnacademy.taskapi.Tag.service.TagService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//public class TagControllerTest {
//
//    @MockBean
//    private TagService tagService;
//
//    @Autowired
//    private TagController tagController;
//
//    private MockMvc mockMvc;
//
//    @BeforeEach
//    void setup() {
//        mockMvc = MockMvcBuilders.standaloneSetup(tagController).build();
//    }
//
//    @Test
//    void createTag() throws Exception {
//        CreateTagRequest request = new CreateTagRequest("testTag");
//        TagResponse response = new TagResponse(1L, "testTag");
//
//        when(tagService.createTag(request)).thenReturn(response);
//
//        mockMvc.perform(post("/task/books/tags")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"tagName\": \"testTag\"}"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.tagName").value("testTag"))
//                .andExpect(jsonPath("$.tagId").value(1));
//
//        verify(tagService, times(1)).createTag(request);
//    }
//
//    @Test
//    void getTag() throws Exception {
//        TagResponse response = new TagResponse(1L, "testTag");
//
//        when(tagService.getTag(1L)).thenReturn(response);
//
//        mockMvc.perform(get("/task/books/tags/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.tagName").value("testTag"))
//                .andExpect(jsonPath("$.tagId").value(1));
//
//        verify(tagService, times(1)).getTag(1L);
//    }
//
//    @Test
//    void updateTag() throws Exception {
//        UpdateTagRequest request = new UpdateTagRequest(1L, "updatedTag");
//        TagResponse response = new TagResponse(1L, "updatedTag");
//
//        when(tagService.updateTag(request)).thenReturn(response);
//
//        mockMvc.perform(put("/task/books/tags")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"tagId\": 1, \"tagName\": \"updatedTag\"}"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.tagName").value("updatedTag"))
//                .andExpect(jsonPath("$.tagId").value(1));
//
//        verify(tagService, times(1)).updateTag(request);
//    }
//
//    @Test
//    void deleteTag() throws Exception {
//        doNothing().when(tagService).removeTag(1L);
//
//        mockMvc.perform(delete("/task/books/tags/1"))
//                .andExpect(status().isNoContent());
//
//        verify(tagService, times(1)).removeTag(1L);
//    }
//}