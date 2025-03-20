package com.nhnacademy.taskapi.like.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.taskapi.like.domain.Like;
import com.nhnacademy.taskapi.like.dto.LikePlusMinusDTO;
import com.nhnacademy.taskapi.like.dto.LikeReponse;
import com.nhnacademy.taskapi.like.service.LikeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(controllers = LikeController.class)
public class LikeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LikeService likeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Toggle Like - 성공 시 좋아요 상태 토글")
    void testToggleLike() throws Exception {
        long bookId = 1L;
        long memberId = 1L;

        // 좋아요 상태 토글 시 true 반환
        when(likeService.toggleLike(bookId, memberId)).thenReturn(true);

        // When & Then
        mockMvc.perform(post("/task/like/{bookId}", bookId)
                        .header("X-MEMBER-ID", memberId))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));  // 좋아요 활성화 상태 확인
    }

    @Test
    @DisplayName("Get Like By Book - 좋아요 정보 조회")
    void testGetLikeByBook() throws Exception {
        long bookId = 1L;

        // 좋아요 정보 반환
        LikeReponse likeReponse = new LikeReponse(bookId, 1L);
        when(likeService.getLikeByBook(bookId)).thenReturn(likeReponse);

        // When & Then
        mockMvc.perform(get("/task/like/{bookId}", bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookId").value(bookId))
                .andExpect(jsonPath("$.memberId").value(1L));
    }

    @Test
    @DisplayName("Check Like - 좋아요 상태 확인")
    void testCheckLike() throws Exception {
        long bookId = 1L;
        long memberId = 1L;

        // 좋아요 상태 확인 시 true 반환
        when(likeService.checkLike(bookId, memberId)).thenReturn(true);

        // When & Then
        mockMvc.perform(get("/task/like/{bookId}/check", bookId)
                        .header("X-MEMBER-ID", memberId))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));  // 좋아요 상태 확인
    }




}
