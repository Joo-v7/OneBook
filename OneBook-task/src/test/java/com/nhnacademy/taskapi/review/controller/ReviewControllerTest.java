package com.nhnacademy.taskapi.review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.taskapi.review.dto.BookReviewableResponse;
import com.nhnacademy.taskapi.review.dto.MyReviewResponse;
import com.nhnacademy.taskapi.review.dto.ReviewRequest;
import com.nhnacademy.taskapi.review.dto.ReviewResponse;
import com.nhnacademy.taskapi.review.service.MyReviewService;
import com.nhnacademy.taskapi.review.service.PendingReviewService;
import com.nhnacademy.taskapi.review.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(controllers = ReviewController.class)
class ReviewControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ReviewService reviewService;

    @MockBean
    PendingReviewService pendingReviewService;

    @MockBean
    MyReviewService myReviewService;

    private ReviewResponse testReviewResponse
            ;
    private ReviewRequest testReviewRequest;

    @BeforeEach
    void setUp() {
        // 테스트 ReviewRequest
        testReviewRequest = new ReviewRequest(5, "test description", List.of("image1", "image2"));

        // 테스트 ReviewResponse
        testReviewResponse
                = ReviewResponse.builder()
                .reviewId(100L)
                .grade(5)
                .description("test description")
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .memberId(1L)
                .bookId(1L)
                .imageUrl(List.of("image1", "image2"))
                .loginId("testUser")
                .build();
    }

    /**
     * [POST] /task/reviews/books/{bookId}
     * 리뷰 등록
     */
    @Test
    void testRegisterReview() throws Exception {
        // given
        given(reviewService.registerReview(eq(1L), eq(1L), any(ReviewRequest.class)))
                .willReturn(testReviewResponse
                );

        // when & then
        mockMvc.perform(post("/task/reviews/books/{bookId}", 1)
                        .header("X-MEMBER-ID", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testReviewRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.reviewId").value(100L))
                .andExpect(jsonPath("$.grade").value(5))
                .andExpect(jsonPath("$.description").value("test description"))
                .andExpect(jsonPath("$.imageUrl").isArray());

        verify(reviewService, times(1))
                .registerReview(eq(1L), eq(1L), any(ReviewRequest.class));
    }

    /**
     * [GET] /task/reviews/books/{bookId}/average
     * 도서의 리뷰 평점 평균
     */
    @Test
    void testGetReviewGradeAverage() throws Exception {
        // given
        double mockAverage = 4.2;
        given(reviewService.getReviewGradeAverage(1L)).willReturn(mockAverage);

        // when & then
        mockMvc.perform(get("/task/reviews/books/{bookId}/average", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("4.2"));

        verify(reviewService, times(1)).getReviewGradeAverage(1L);
    }

    /**
     * [GET] /task/reviews/books/{bookId}
     * 도서의 리뷰 목록 (페이징)
     */
    @Test
    void testGetReviewsByBook() throws Exception {
        // given
        List<ReviewResponse> mockList = List.of(testReviewResponse
        );
        Page<ReviewResponse> mockPage = new PageImpl<>(mockList, PageRequest.of(0, 10), 1);
        given(reviewService.getReviewByBook(1L, 0, 10)).willReturn(mockPage);

        // when & then
        mockMvc.perform(get("/task/reviews/books/{bookId}", 1L)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].reviewId").value(100L))
                .andExpect(jsonPath("$.content[0].grade").value(5));

        verify(reviewService, times(1))
                .getReviewByBook(1L, 0, 10);
    }

    /**
     * [GET] /task/reviews/count/books/{bookId}
     * 작성된 리뷰 개수 확인
     */
    @Test
    void testGetReviewCount() throws Exception {
        // given
        given(reviewService.getReviewCount(1L)).willReturn(5);

        // when & then
        mockMvc.perform(get("/task/reviews/count/books/{bookId}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));

        verify(reviewService, times(1))
                .getReviewCount(1L);
    }

    /**
     * [PUT] /task/reviews/{reviewId}/books/{bookId}
     * 리뷰 수정
     */
    @Test
    void testUpdateReview() throws Exception {
        // given
        given(reviewService.updateReview(eq(1L), eq(100L), eq(2L), any(ReviewRequest.class)))
                .willReturn(testReviewResponse
                );

        // when & then
        mockMvc.perform(put("/task/reviews/{reviewId}/books/{bookId}", 100, 1)
                        .header("X-MEMBER-ID", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testReviewRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewId").value(100L))
                .andExpect(jsonPath("$.memberId").value(1L)); // testReviewResponse
        // memberId=1L로 설정

        verify(reviewService, times(1))
                .updateReview(eq(1L), eq(100L), eq(2L), any(ReviewRequest.class));
    }

    /**
     * [DELETE] /task/reviews/{reviewId}/books/{bookId}
     * 리뷰 삭제
     */
    @Test
    void testDeleteReview() throws Exception {
        // given
        given(reviewService.deleteReview(eq(1L), eq(100L), eq(2L)))
                .willReturn(testReviewResponse
                );

        // when & then
        mockMvc.perform(delete("/task/reviews/{reviewId}/books/{bookId}", 100, 1)
                        .header("X-MEMBER-ID", 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewId").value(100L));

        verify(reviewService, times(1))
                .deleteReview(eq(1L), eq(100L), eq(2L));
    }

    /**
     * [GET] /task/members/pending-reviews
     * 리뷰를 작성하지 않은 도서 목록
     */
    @Test
    void testGetPendingReviews() throws Exception {
        // given
        List<BookReviewableResponse> mockPending = List.of(
                new BookReviewableResponse(10L, "Book A", "urlA", LocalDateTime.now()),
                new BookReviewableResponse(20L, "Book B", "urlB", LocalDateTime.now())
        );
        given(pendingReviewService.getPendingReviews(99L)).willReturn(mockPending);

        // when & then
        mockMvc.perform(get("/task/members/pending-reviews")
                        .header("X-MEMBER-ID", 99L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookId").value(10L))
                .andExpect(jsonPath("$[1].bookId").value(20L));

        verify(pendingReviewService, times(1))
                .getPendingReviews(99L);
    }

    /**
     * [GET] /task/members/my-reviews
     * 내가 작성한 리뷰 목록
     */
    @Test
    void testGetMyReviews() throws Exception {
        // given
        List<MyReviewResponse> mockMyReviews = List.of(
                new MyReviewResponse(10L, "Book A", "urlA", LocalDateTime.now(), 5, "desc1", 1000L),
                new MyReviewResponse(20L, "Book B", "urlB", LocalDateTime.now(), 3, "desc2", 2000L)
        );
        given(myReviewService.getMyReviews(88L)).willReturn(mockMyReviews);

        // when & then
        mockMvc.perform(get("/task/members/my-reviews")
                        .header("X-MEMBER-ID", 88L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reviewId").value(1000L))
                .andExpect(jsonPath("$[1].reviewId").value(2000L));

        verify(myReviewService, times(1))
                .getMyReviews(88L);
    }

    /**
     * [GET] /task/reviews/{reviewId}
     * 특정 리뷰 상세 조회
     */
    @Test
    void testGetReview() throws Exception {
        // given
        given(reviewService.getReviewById(2L, 999L))
                .willReturn(testReviewResponse
                );

        // when & then
        mockMvc.perform(get("/task/reviews/{reviewId}", 999)
                        .header("X-MEMBER-ID", 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewId").value(100L));

        verify(reviewService, times(1)).getReviewById(2L, 999L);
    }
}
