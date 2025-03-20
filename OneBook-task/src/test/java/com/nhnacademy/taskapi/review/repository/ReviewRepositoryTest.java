package com.nhnacademy.taskapi.review.repository;

import com.nhnacademy.taskapi.book.domain.Book;
import com.nhnacademy.taskapi.book.repository.BookRepository;
import com.nhnacademy.taskapi.config.QuerydslConfig;
import com.nhnacademy.taskapi.grade.domain.Grade;
import com.nhnacademy.taskapi.grade.repository.GradeRepository;
import com.nhnacademy.taskapi.member.domain.Member;
import com.nhnacademy.taskapi.member.domain.Member.Gender;
import com.nhnacademy.taskapi.member.domain.Member.Status;
import com.nhnacademy.taskapi.member.repository.MemberRepository;
import com.nhnacademy.taskapi.publisher.domain.Publisher;
import com.nhnacademy.taskapi.publisher.repository.PublisherRepository;
import com.nhnacademy.taskapi.roles.domain.Role;
import com.nhnacademy.taskapi.roles.repository.RoleRepository;
import com.nhnacademy.taskapi.review.domain.Review;
import com.nhnacademy.taskapi.review.domain.ReviewImage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import jakarta.validation.ConstraintViolationException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(QuerydslConfig.class)

class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ReviewImageRepository reviewImageRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private GradeRepository gradeRepository;
    @Autowired
    private PublisherRepository publisherRepository;

    Member member;
    Book book;

    @BeforeEach
    void setUp() {
        // Grade 생성
        Grade grade = Grade.create("Gold", 10, "BlackCow");
        gradeRepository.save(grade);

        // Role 생성
        Role role = Role.createRole("MEMBER", "나의 돈줄");
        roleRepository.save(role);

        // Member 생성
        member = Member.createNewMember(
                grade,
                "testUser",
                "testLoginId",
                "testPassword",
                LocalDate.of(1990, 1, 1),
                Gender.M,
                "test@example.com",
                "010-1234-5678",
                role
        );
        member.setStatus(Status.ACTIVE);
        memberRepository.save(member);

        // Publisher 생성
        Publisher publisher = new Publisher();
        publisher.setName("Test Publisher");
        publisherRepository.save(publisher);

        // Book 생성
        book = new Book();
        book.setPublisher(publisher);
        book.setTitle("화성 갈끄니까");
        book.setDescription("테슬라 제발제발");
        book.setContent("목차");
        book.setIsbn13("1234567890123");
        book.setPrice(10000);
        book.setSalePrice(9000);
        book.setAmount(10);
        book.setViews(0);
        book.setPubdate(LocalDate.now());
        bookRepository.save(book);
    }

    @Test
    @DisplayName("리뷰와 리뷰 이미지를 정상적으로 저장하고 조회할 수 있다.")
    void testSaveAndFindReviewWithImages() {
        // Given
        Review review = new Review();
        review.setMember(member);
        review.setBook(book);
        review.setGrade(5);
        review.setDescription("개추준다");
        review.setCreatedAt(LocalDateTime.now());

        // 리뷰 사진첨부
        ReviewImage image1 = new ReviewImage();
        image1.setReview(review);
        image1.setImageUrl("http://example.com/image1.jpg");

        ReviewImage image2 = new ReviewImage();
        image2.setReview(review);
        image2.setImageUrl("http://example.com/image2.jpg");

        review.getReviewImage().add(image1);
        review.getReviewImage().add(image2);

        // When
        Review savedReview = reviewRepository.save(review);

        // Then
        Optional<Review> foundReviewOpt = reviewRepository.findById(savedReview.getReviewId());
        assertTrue(foundReviewOpt.isPresent());

        Review foundReview = foundReviewOpt.get();
        assertEquals("개추준다", foundReview.getDescription());
        assertEquals(2, foundReview.getReviewImage().size());
        assertTrue(foundReview.getReviewImage().stream()
                .anyMatch(img -> img.getImageUrl().equals("http://example.com/image1.jpg")));
        assertTrue(foundReview.getReviewImage().stream()
                .anyMatch(img -> img.getImageUrl().equals("http://example.com/image2.jpg")));
    }

    @Test
    @DisplayName("리뷰 저장 시 필수 필드 누락 시 ConstraintViolationException 발생")
    void testSaveReviewWithMissingFields() {
        // Given
        Review review = new Review();
        review.setMember(member);
        review.setBook(book);
        review.setGrade(5);
        review.setDescription(""); // @NotBlank 위반
        review.setCreatedAt(LocalDateTime.now());

        // When/Then
        assertThrows(ConstraintViolationException.class, () -> {
            reviewRepository.save(review);
            reviewRepository.flush(); // 즉시 적용하여 예외 발생
        });
    }


    @Test
    @DisplayName("리뷰와 리뷰 이미지를 삭제할 수 있다. (admin role)")
    void testDeleteReviewWithImages() {
        // Given
        Review review = new Review();
        review.setMember(member);
        review.setBook(book);
        review.setGrade(4);
        review.setDescription("Good book!");
        review.setCreatedAt(LocalDateTime.now());

        ReviewImage image1 = new ReviewImage();
        image1.setReview(review);
        image1.setImageUrl("http://example.com/image1.jpg");

        ReviewImage image2 = new ReviewImage();
        image2.setReview(review);
        image2.setImageUrl("http://example.com/image2.jpg");

        review.getReviewImage().add(image1);
        review.getReviewImage().add(image2);

        reviewRepository.save(review);

        // When
        reviewRepository.delete(review);

        // Then
        Optional<Review> foundReviewOpt = reviewRepository.findById(review.getReviewId());
        assertFalse(foundReviewOpt.isPresent());

        // 리뷰 이미지도 함께 삭제되었는지 확인
        List<ReviewImage> images = reviewImageRepository.findAll();
        assertTrue(images.isEmpty());
    }

    @Test
    @DisplayName("findAverageGradeByBookId: 리뷰 2개(평점4,5) 평균 4.5 반환")
    void testFindAverageGradeByBookId() {
        // 리뷰 삽입
        Review r1 = new Review();
        r1.setMember(member);
        r1.setBook(book);
        r1.setGrade(4);
        r1.setDescription("Good");
        r1.setCreatedAt(LocalDateTime.now());
        reviewRepository.save(r1);

        Review r2 = new Review();
        r2.setMember(member);
        r2.setBook(book);
        r2.setGrade(5);
        r2.setDescription("Excellent");
        r2.setCreatedAt(LocalDateTime.now());
        reviewRepository.save(r2);

        Optional<Double> avgGrade = reviewRepository.findAverageGradeByBookId(book.getBookId());
        assertTrue(avgGrade.isPresent());
        assertEquals(4.5, avgGrade.get());
    }

    @Test
    @DisplayName("findAverageGradeByBookId: 리뷰없을때 빈 Optional")
    void testFindAverageGradeByBookIdNoReviews() {
        // 리뷰 없음
        Optional<Double> avgGrade = reviewRepository.findAverageGradeByBookId(book.getBookId());
        assertFalse(avgGrade.isPresent());
    }

    @Test
    @DisplayName("findByMemberAndBook: 특정 멤버와 도서에 대한 리뷰 존재")
    void testFindByMemberAndBook() {
        Review r = new Review();
        r.setMember(member);
        r.setBook(book);
        r.setGrade(5);
        r.setDescription("개추 드립니다");
        r.setCreatedAt(LocalDateTime.now());
        reviewRepository.save(r);

        Optional<Review> found = reviewRepository.findByMemberAndBook(member, book);
        assertTrue(found.isPresent());
        assertEquals("개추 드립니다", found.get().getDescription());
    }

    @Test
    @DisplayName("findByMemberAndBook: 특정 멤버와 도서에 대한 리뷰 없음")
    void testFindByMemberAndBookNoReview() {
        // 리뷰 없음
        Optional<Review> found = reviewRepository.findByMemberAndBook(member, book);
        assertFalse(found.isPresent());
    }

    @Test
    @DisplayName("findByBookBookId: 페이징 테스트(리뷰3개, size=2 -> 첫페이지2개,두번째페이지1개)")
    void testFindByBookBookIdPagination() {
        // 리뷰 3개 생성
        for (int i = 1; i <= 3; i++) {
            Review r = new Review();
            r.setMember(member);
            r.setBook(book);
            r.setGrade(i);
            r.setDescription("Review " + i);
            r.setCreatedAt(LocalDateTime.now().minusMinutes(i));
            reviewRepository.save(r);
        }

        // 첫 페이지 size=2
        var pageable = PageRequest.of(0, 2, Sort.by("createdAt").descending());
        var page1 = reviewRepository.findByBookBookId(book.getBookId(), pageable);
        assertEquals(2, page1.getContent().size());

        // 두번째 페이지 size=2 (실제 데이터는 3개이므로 1개만 나옴)
        var pageable2 = PageRequest.of(1, 2, Sort.by("createdAt").descending());
        var page2 = reviewRepository.findByBookBookId(book.getBookId(), pageable2);
        assertEquals(1, page2.getContent().size());
    }
}
