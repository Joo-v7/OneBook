package com.nhnacademy.taskapi.image.controller;



import com.nhnacademy.taskapi.book.domain.Book;
import com.nhnacademy.taskapi.image.domain.Image;
import com.nhnacademy.taskapi.image.dto.ImageSaveDTO;
import com.nhnacademy.taskapi.image.service.ImageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(ImageController.class)
public class ImageControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImageService imageService;

    @Test
    @DisplayName("uploadImage Success Test")
    void uploadImage_Success() throws Exception {
        // Arrange
        MockMultipartFile imageFile = new MockMultipartFile(
                "image", "test-image.jpg", "image/jpeg", "image content".getBytes());

        // 임시 Image 객체 생성
        Image image = new Image();
        image.setName("test-image.jpg");
        image.setUrl("https://cloud.nhn.com/image/test-image.jpg");


        ImageSaveDTO imageSaveDTO = new ImageSaveDTO();
        imageSaveDTO.setBookId(1L);
        imageSaveDTO.setImageName(image.getName());
        imageSaveDTO.setImageBytes(imageFile.getBytes());

        when(imageService.saveImage(any(ImageSaveDTO.class))).thenReturn(image);


        // 이미지 업로드 요청 테스트
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart("/task/image")
                        .file(imageFile)
                        .param("bookId", "1")
                        .param("imageName", "test-image.jpg"))
                .andExpect(status().isCreated())  // 201 CREATED 상태 코드 예상
                .andExpect(jsonPath("$.name").value("test-image.jpg"))
                .andExpect(jsonPath("$.url").value("https://cloud.nhn.com/image/test-image.jpg"));
    }


    @Test
    @DisplayName("deleteImage Success Test")
    void deleteImage_Success() throws Exception {
        // Arrange
        long imageId = 1L;

        // Act & Assert
        mockMvc.perform(delete("/task/image/{imageId}", imageId))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetImage() throws Exception {
        // Given
        long bookId = 1L;
        Image image = new Image(1L, "http://example.com/image.jpg", null, "Book Image");

        when(imageService.getImage(bookId)).thenReturn(image);

        // When & Then
        mockMvc.perform(get("/task/image/{bookId}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.imageId").value(1L))
                .andExpect(jsonPath("$.url").value("http://example.com/image.jpg"))
                .andExpect(jsonPath("$.name").value("Book Image"));
    }

    @Test
    void testUploadImageEmptyFile() throws Exception {
        // Given
        MockMultipartFile emptyFile = new MockMultipartFile("image", "", "image/jpeg", new byte[0]);
        long bookId = 1L;
        String imageName = "Test Image";

        // When & Then
        mockMvc.perform(multipart("/task/image")
                        .file(emptyFile)
                        .param("bookId", String.valueOf(bookId))
                        .param("imageName", imageName))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }





}
