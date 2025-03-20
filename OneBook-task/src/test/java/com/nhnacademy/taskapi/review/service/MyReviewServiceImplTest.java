package com.nhnacademy.taskapi.review.service;

import com.nhnacademy.taskapi.image.domain.Image;
import com.nhnacademy.taskapi.image.service.ImageService;
import com.nhnacademy.taskapi.member.domain.Member;
import com.nhnacademy.taskapi.member.repository.MemberRepository;
import com.nhnacademy.taskapi.review.domain.Review;
import com.nhnacademy.taskapi.review.dto.MyReviewResponse;
import com.nhnacademy.taskapi.review.exception.InvalidReviewException;
import com.nhnacademy.taskapi.review.repository.ReviewRepository;
import com.nhnacademy.taskapi.review.service.MyReviewService;
import com.nhnacademy.taskapi.review.service.impl.MyReviewServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class MyReviewServiceImplTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private MyReviewServiceImpl myReviewService; // 테스트 대상

    private Member member;
    private Review review1;
    private Review review2;

    @BeforeEach
    void setUp() throws Exception {

        // 1) Member 엔티티 세팅
        member = new Member();
        Field memberIdField = Member.class.getDeclaredField("id");
        memberIdField.setAccessible(true);
        memberIdField.set(member, 1L);

        // 2) Review
        review1 = new Review();
        review1.setReviewId(100L);
        review1.setCreatedAt(LocalDateTime.of(2024, 1, 1, 12, 0));
        review1.setGrade(5);
        review1.setDescription("첫 번째 리뷰입니다.");

        var book1 = new com.nhnacademy.taskapi.book.domain.Book();
        book1.setBookId(10L);
        book1.setTitle("테스트 도서 A");
        review1.setBook(book1);

        // 작성자
        review1.setMember(member);

        review2 = new Review();
        review2.setReviewId(200L);
        review2.setCreatedAt(LocalDateTime.of(2024, 1, 2, 13, 30));
        review2.setGrade(3);
        review2.setDescription("두 번째 리뷰입니다.");

        var book2 = new com.nhnacademy.taskapi.book.domain.Book();
        book2.setBookId(20L);
        book2.setTitle("테스트 도서 B");
        review2.setBook(book2);

        // 작성자
        review2.setMember(member);
    }

    /**
     * 회원이 DB에 존재하지 않을 때 → InvalidReviewException 발생
     */
    @Test
    void testGetMyReviews_memberNotFound() {
        // given
        Long memberId = 999L;
        given(memberRepository.findById(memberId)).willReturn(Optional.empty());

        // when / then
        assertThrows(InvalidReviewException.class, () ->
                myReviewService.getMyReviews(memberId)
        );
        verify(reviewRepository, never()).findByMemberId(anyLong());
    }

    /**
     * 회원은 존재하지만 리뷰가 없는 경우 → 빈 리스트 반환
     */
    @Test
    void testGetMyReviews_noReviews() {
        // given
        Long memberId = 1L;
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
        // 리뷰가 없다
        given(reviewRepository.findByMemberId(memberId)).willReturn(Collections.emptyList());

        // when
        List<MyReviewResponse> result = myReviewService.getMyReviews(memberId);

        // then
        assertTrue(result.isEmpty());
        verify(imageService, never()).getImage(anyLong());
    }

    /**
     * 회원이 존재하고, 여러 리뷰가 있을 때 → 해당 리뷰들을 MyReviewResponse로 변환
     */
    @Test
    void testGetMyReviews_withReviews() {
        // given
        Long memberId = 1L;
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
        // 리뷰가 2개 있다고 가정
        given(reviewRepository.findByMemberId(memberId)).willReturn(List.of(review1, review2));

        // imageService: 각 도서ID 에 대응되는 Image url
        var mockImageA = new Image();
        mockImageA.setUrl("http://image.com/bookA.jpg");

        var mockImageB = new Image();
        mockImageB.setUrl("http://image.com/bookB.jpg");

        // getImage(10L) -> A, getImage(20L) -> B
        given(imageService.getImage(10L)).willReturn(mockImageA);
        given(imageService.getImage(20L)).willReturn(mockImageB);

        // when
        List<MyReviewResponse> result = myReviewService.getMyReviews(memberId);

        // then
        assertEquals(2, result.size());

        MyReviewResponse firstReview = result.get(0);
        assertEquals(10L, firstReview.getBookId());
        assertEquals("테스트 도서 A", firstReview.getBookName());
        assertEquals("http://image.com/bookA.jpg", firstReview.getBookImageUrl());
        assertEquals(LocalDateTime.of(2024, 1, 1, 12, 0), firstReview.getCreatedAt());
        assertEquals(5, firstReview.getGrade());
        assertEquals("첫 번째 리뷰입니다.", firstReview.getDescription());
        assertEquals(100L, firstReview.getReviewId());

        MyReviewResponse secondReview = result.get(1);
        assertEquals(20L, secondReview.getBookId());
        assertEquals("테스트 도서 B", secondReview.getBookName());
        assertEquals("http://image.com/bookB.jpg", secondReview.getBookImageUrl());
        assertEquals(LocalDateTime.of(2024, 1, 2, 13, 30), secondReview.getCreatedAt());
        assertEquals(3, secondReview.getGrade());
        assertEquals("두 번째 리뷰입니다.", secondReview.getDescription());
        assertEquals(200L, secondReview.getReviewId());

        // imageService 가 bookId 10L, 20L 각각 1번씩 호출되었는지 검증
        verify(imageService, times(1)).getImage(10L);
        verify(imageService, times(1)).getImage(20L);
    }
}
