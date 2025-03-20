package com.nhnacademy.taskapi.review.service;

import com.nhnacademy.taskapi.book.domain.Book;
import com.nhnacademy.taskapi.grade.domain.Grade;
import com.nhnacademy.taskapi.image.domain.Image;
import com.nhnacademy.taskapi.image.service.ImageService;
import com.nhnacademy.taskapi.member.domain.Member;
import com.nhnacademy.taskapi.member.domain.Member.Gender;
import com.nhnacademy.taskapi.member.domain.Member.Status;
import com.nhnacademy.taskapi.member.repository.MemberRepository;
import com.nhnacademy.taskapi.order.entity.Order;
import com.nhnacademy.taskapi.order.entity.OrderDetail;
import com.nhnacademy.taskapi.payment.domain.Payment;
import com.nhnacademy.taskapi.payment.repository.PaymentRepository;
import com.nhnacademy.taskapi.review.domain.Review;
import com.nhnacademy.taskapi.review.dto.BookReviewableResponse;
import com.nhnacademy.taskapi.review.exception.InvalidReviewException;
import com.nhnacademy.taskapi.review.repository.ReviewRepository;
import com.nhnacademy.taskapi.review.service.impl.PendingReviewServiceImpl;
import com.nhnacademy.taskapi.roles.domain.Role;
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
class PendingReviewServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private PendingReviewServiceImpl pendingReviewService;

    private Member member;
    private Book book1;
    private Book book2;
    private Order order;
    private OrderDetail orderDetail1;
    private OrderDetail orderDetail2;
    private Payment paymentDone;

    private Role memberRole;
    private Grade grade;

    @BeforeEach
    void setUp() throws Exception {

        // Grade 객체 생성
        grade = Grade.create("Gold", 10, "WoW, You Are BlackCow");

        // MEMBER Role 생성
        memberRole = Role.createRole("MEMBER", "내 돈줄");
        Field memberRoleIdField = Role.class.getDeclaredField("id");
        memberRoleIdField.setAccessible(true);
        memberRoleIdField.set(memberRole, 1);

        // 1) Member 생성

        member = Member.createNewMember(
                grade,
                "홍길동",
                "loginId",
                "password",
                LocalDate.of(1990, 1, 1),
                Gender.M,
                "test@example.com",
                "010-1234-5678",
                memberRole
        );
        member.setStatus(Status.ACTIVE);

        Field memberIdField = Member.class.getDeclaredField("id");
        memberIdField.setAccessible(true);
        memberIdField.set(member, 1L);

        // 2) Book 생성
        book1 = new Book();
        book1.setBookId(101L);
        book1.setTitle("테스트 도서1");

        book2 = new Book();
        book2.setBookId(102L);
        book2.setTitle("테스트 도서2");

        // 3) Order 생성
        order = new Order();
        order.setOrderId(777L); // 필요하다면 수동 세팅
        order.setMember(member);
        order.setDateTime(LocalDateTime.now());

        // 4) OrderDetail 생성
        orderDetail1 = new OrderDetail(order, book1, 5000, 2000, 1000, 10);
        orderDetail2 = new OrderDetail(order, book2, 8000, 2000, 1000, 10);

        List<OrderDetail> detailList = Arrays.asList(orderDetail1, orderDetail2);
        order.setOrderDetailList(detailList);

        paymentDone = new Payment();
        paymentDone.setStatus("DONE");
        paymentDone.setRequestedAt(LocalDateTime.now());
        paymentDone.setTotalAmount(9999);
        paymentDone.setOnlyBookAmount(9999);
        paymentDone.setOrder(order);
    }

    /**
     * 존재하지 않는 회원 -> InvalidReviewException
     */
    @Test
    void testGetPendingReviews_memberNotFound() {
        // given
        Long invalidMemberId = 999L;
        given(memberRepository.findById(invalidMemberId)).willReturn(Optional.empty());

        // when / then
        assertThrows(InvalidReviewException.class,
                () -> pendingReviewService.getPendingReviews(invalidMemberId));

        verify(paymentRepository, never()).findByOrder_Member_IdAndStatus(anyLong(), anyString());
    }

    /**
     * DONE 결제가 전혀 없는 경우 -> 빈 리스트
     */
    @Test
    void testGetPendingReviews_noDonePayments() {
        // given
        Long memberId = 1L;
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
        given(paymentRepository.findByOrder_Member_IdAndStatus(memberId, "DONE"))
                .willReturn(Collections.emptyList());

        // when
        List<BookReviewableResponse> result = pendingReviewService.getPendingReviews(memberId);

        // then
        assertTrue(result.isEmpty());
        verify(reviewRepository, never()).findByMemberAndBook(any(Member.class), any(Book.class));
    }

    /**
     * DONE 결제는 있으나 해당 도서 이미 리뷰 -> 제외
     */
    @Test
    void testGetPendingReviews_withDonePayments_butAlreadyReviewed() {
        Image mockImage = new Image();
        mockImage.setUrl("image.com/image2.jpg");

        // given
        Long memberId = 1L;
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
        given(paymentRepository.findByOrder_Member_IdAndStatus(memberId, "DONE"))
                .willReturn(List.of(paymentDone));

        // 이미 리뷰 있음 (book1)
        Review existingReview = new Review();
        existingReview.setReviewId(1000L);
        existingReview.setBook(book1);
        existingReview.setMember(member);

        // book1 -> 리뷰 존재
        given(reviewRepository.findByMemberAndBook(member, book1))
                .willReturn(Optional.of(existingReview));

        // book2 -> 아직 리뷰 없음
        given(reviewRepository.findByMemberAndBook(member, book2))
                .willReturn(Optional.empty());

        // imageService(book2)
        given(imageService.getImage(book2.getBookId()))
                .willReturn(mockImage);

        // when
        List<BookReviewableResponse> result = pendingReviewService.getPendingReviews(memberId);

        // then
        assertEquals(1, result.size()); // book2만 나와야 함
        BookReviewableResponse dto = result.get(0);
        assertEquals(book2.getBookId(), dto.getBookId());
        assertEquals("테스트 도서2", dto.getBookName());
        assertEquals("image.com/image2.jpg", dto.getBookImageUrl());
    }

    /**
     * DONE 결제 있고, 아직 리뷰 없는 도서 -> 리스트 반환
     */
    @Test
    void testGetPendingReviews_noReviewYet() {
        Image mockImage1 = new Image();
        mockImage1.setUrl("image.com/image1.jpg");

        Image mockImage2 = new Image();
        mockImage2.setUrl("image.com/image2.jpg");
        // given
        Long memberId = 1L;
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
        given(paymentRepository.findByOrder_Member_IdAndStatus(memberId, "DONE"))
                .willReturn(List.of(paymentDone));

        // book1, book2 전부 리뷰 없음
        given(reviewRepository.findByMemberAndBook(member, book1))
                .willReturn(Optional.empty());
        given(reviewRepository.findByMemberAndBook(member, book2))
                .willReturn(Optional.empty());

        // imageService
        given(imageService.getImage(book1.getBookId()))
                .willReturn(mockImage1);
        given(imageService.getImage(book2.getBookId()))
                .willReturn(mockImage2);

        // when
        List<BookReviewableResponse> result = pendingReviewService.getPendingReviews(memberId);

        // then
        assertEquals(2, result.size());
        Set<Long> bookIds = new HashSet<>();
        for (BookReviewableResponse dto : result) {
            bookIds.add(dto.getBookId());
        }
        assertTrue(bookIds.contains(book1.getBookId()));
        assertTrue(bookIds.contains(book2.getBookId()));
    }

    /**
     * 동일 도서 여러 번 구매했지만 리뷰 없으면 1회만 표시(중복 제거)
     */
    @Test
    void testGetPendingReviews_sameBookPurchasedMultipleTimes() {
        Image mockImage1 = new Image();
        mockImage1.setUrl("image.com/image1.jpg");

        Image mockImage2 = new Image();
        mockImage2.setUrl("image.com/image2.jpg");
        // given
        Long memberId = 1L;
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));

        // 동일 도서를 2번 구매 -> orderDetail 추가
        OrderDetail detailBook1Again = new OrderDetail(order, book1, 5000, 1000, 10, 10);

        // order.detailList -> book1, book1, book2
        order.setOrderDetailList(Arrays.asList(orderDetail1, detailBook1Again, orderDetail2));

        // paymentDone는 그대로
        given(paymentRepository.findByOrder_Member_IdAndStatus(memberId, "DONE"))
                .willReturn(List.of(paymentDone));

        // 리뷰 없음
        given(reviewRepository.findByMemberAndBook(member, book1)).willReturn(Optional.empty());
        given(reviewRepository.findByMemberAndBook(member, book2)).willReturn(Optional.empty());

        // imageService
        given(imageService.getImage(book1.getBookId()))
                .willReturn(mockImage1);
        given(imageService.getImage(book2.getBookId()))
                .willReturn(mockImage2);

        // when
        List<BookReviewableResponse> result = pendingReviewService.getPendingReviews(memberId);

        // then
        // book1은 2번 구매되었지만 결과에 1번만
        assertEquals(2, result.size());
        Set<Long> bookIds = new HashSet<>();
        for (BookReviewableResponse dto : result) {
            bookIds.add(dto.getBookId());
        }
        assertTrue(bookIds.contains(book1.getBookId()));
        assertTrue(bookIds.contains(book2.getBookId()));
    }
}
