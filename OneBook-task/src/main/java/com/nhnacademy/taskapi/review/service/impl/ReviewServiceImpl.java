package com.nhnacademy.taskapi.review.service.impl;

import com.nhnacademy.taskapi.adapter.NhnImageManagerAdapter;
import com.nhnacademy.taskapi.book.domain.Book;
import com.nhnacademy.taskapi.book.repository.BookRepository;
import com.nhnacademy.taskapi.member.domain.Member;
import com.nhnacademy.taskapi.member.repository.MemberRepository;
import com.nhnacademy.taskapi.point.service.PointService;
import com.nhnacademy.taskapi.review.domain.Review;
import com.nhnacademy.taskapi.review.domain.ReviewImage;
import com.nhnacademy.taskapi.review.dto.ReviewRequest;
import com.nhnacademy.taskapi.review.dto.ReviewResponse;
import com.nhnacademy.taskapi.review.exception.ImageLimitExceededException;
import com.nhnacademy.taskapi.review.exception.InvalidReviewException;
import com.nhnacademy.taskapi.review.exception.ReviewAlreadyExistsException;
import com.nhnacademy.taskapi.review.repository.ReviewRepository;
import com.nhnacademy.taskapi.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;

    private final BookRepository bookRepository;
    private final PointService pointService;
    private final NhnImageUploadServiceImpl nhnImageUploadService;
    private final LocalImageUploadServiceImpl localImageUploadService;

    private final NhnImageManagerAdapter nhnImageManagerAdapter;

    @Override
    @Transactional
    public ReviewResponse registerReview(long bookId, Long memberId, ReviewRequest reviewRequest) {
        log.debug("Registering review for bookId: {}, memberId: {}", bookId, memberId);
        System.out.println("member ID : " + memberId);

        // 회원 존재 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new InvalidReviewException("회원이 존재하지 않습니다."));
        log.debug("Member found: {}", member.getId());

        // 도서 존재 확인
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new InvalidReviewException("도서가 존재하지 않습니다."));
        log.debug("Book found: {}", book.getBookId());

        // 동일 도서에 대한 리뷰 작성 여부 확인
        Optional<Review> existingReview = reviewRepository.findByMemberAndBook(member, book);
        if (existingReview.isPresent()) {
            throw new ReviewAlreadyExistsException("이미 해당 도서에 대한 리뷰를 작성하셨습니다.");
        }
        log.debug("No existing review found for this member and book.");

        // 이미지 첨부 개수 확인
        if (reviewRequest.getImageUrl() != null && reviewRequest.getImageUrl().size() > 3) {
            throw new ImageLimitExceededException("이미지는 최대 3장까지 첨부할 수 있습니다.");
        }

        // 사진 첨부 포인트 체크
        boolean isPhotoAttached = false;

        log.debug("Image URL count: {}", reviewRequest.getImageUrl() != null ? reviewRequest.getImageUrl().size() : 0);

        // 리뷰 생성
        Review review = new Review();
        review.setMember(member);
        review.setBook(book);
        review.setGrade(reviewRequest.getGrade());
        review.setDescription(reviewRequest.getDescription());
        review.setCreatedAt(LocalDateTime.now());
        log.debug("Review created with grade: {}, description: {}", review.getGrade(), review.getDescription());

        // 사진 추가
        if (reviewRequest.getImageUrl() != null && !reviewRequest.getImageUrl().isEmpty()) {
            int counter = 1;
            for (String base64Image : reviewRequest.getImageUrl()) {
                isPhotoAttached = true;
                try {
                    String[] parts = base64Image.split(",");
                    byte[] imageBytes = Base64.getDecoder().decode(parts.length > 1 ? parts[1] : parts[0]);

                    // 고유 파일명 생성
                    String fileName = "review_" + memberId + "_" + System.currentTimeMillis() + "_" + counter + ".jpg";
                    counter++;

                    long bookIdValue = book.getBookId();
                    String loginId = member.getLoginId();

                    // NHN 이미지 업로드
//                    String uploadedUrl = nhnImageManagerAdapter.uploadReviewImage(imageBytes, fileName, bookIdValue, loginId);
//                    nhnImageUploadService.uploadImage(imageBytes, fileName, bookIdValue, loginId);

                    // 로컬 이미지 업로드
                    String uploadedUrl = localImageUploadService.uploadImage(imageBytes, fileName, bookIdValue, loginId);

                    ReviewImage reviewImage = new ReviewImage();
                    reviewImage.setImageUrl(uploadedUrl);
                    reviewImage.setReview(review);
                    review.getReviewImage().add(reviewImage);

                    log.debug("Uploaded review image URL: {}", uploadedUrl);
                } catch (IOException e) {
                    log.error("이미지 업로드 실패", e);
                    isPhotoAttached = false;
                }
            }
}

        // 리뷰 저장
        Review savedReview = reviewRepository.save(review);
        log.debug("Review saved with reviewId: {}", savedReview.getReviewId());

        pointService.registerReviewPoints(member, isPhotoAttached);

        // 응답 DTO 생성
        ReviewResponse response = ReviewResponse.builder()
                .reviewId(savedReview.getReviewId())
                .grade(savedReview.getGrade())
                .description(savedReview.getDescription())
                .createdAt(savedReview.getCreatedAt())
                .updatedAt(savedReview.getUpdatedAt())
                .memberId(member.getId())
                .bookId(book.getBookId())
                .imageUrl(
                        savedReview.getReviewImage().stream()
                                .map(ReviewImage::getImageUrl)
                                .collect(Collectors.toList())
                )
                .loginId(member.getLoginId())
                .build();
        log.debug("ReviewResponse created: {}", response);

        return response;
    }

    @Override
    public double getReviewGradeAverage(long bookId) {
        return reviewRepository.findAverageGradeByBookId(bookId)
                .orElse(0.0);
    }

    @Override
    public Page<ReviewResponse> getReviewByBook(long bookId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Review> reviewPage = reviewRepository.findByBookBookId(bookId, pageable);

        return reviewPage.map(review -> ReviewResponse.builder()
                .reviewId(review.getReviewId())
                .grade(review.getGrade())
                .description(review.getDescription())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .memberId(review.getMember().getId())
                .bookId(review.getBook().getBookId())
                .imageUrl(review.getReviewImage().stream()
                        .map(ReviewImage::getImageUrl)
                        .collect(Collectors.toList()))
                .loginId(review.getMember().getLoginId())
                .build());
    }

    @Override
    @Transactional
    public ReviewResponse updateReview(long bookId, long reviewId, Long memberId, ReviewRequest reviewRequest) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new InvalidReviewException("Invalid review ID"));

        // 리뷰 도서 일치 확인
        if (review.getBook().getBookId() != bookId) {
            throw new InvalidReviewException("리뷰가 해당 도서에 속하지 않습니다.");
        }

        // 리뷰 작성자 확인
        if (!review.getMember().getId().equals(memberId)) {
            throw new InvalidReviewException("작성자만 리뷰를 수정할 수 있습니다.");
        }

        // 이미지 첨부 개수 확인
        if (reviewRequest.getImageUrl() != null && reviewRequest.getImageUrl().size() > 3) {
            throw new ImageLimitExceededException("이미지는 최대 3장까지 첨부할 수 있습니다.");
        }

        // 리뷰 정보 수정
        review.setGrade(reviewRequest.getGrade());
        review.setDescription(reviewRequest.getDescription());
        review.setUpdatedAt(LocalDateTime.now());

        // 이미지 업데이트
        review.getReviewImage().clear();
        if (reviewRequest.getImageUrl() != null && !reviewRequest.getImageUrl().isEmpty()) {
            for (String imageUrl : reviewRequest.getImageUrl()) {
                ReviewImage reviewImage = new ReviewImage();
                reviewImage.setImageUrl(imageUrl);
                reviewImage.setReview(review);
                review.getReviewImage().add(reviewImage);
            }
        }

        // 리뷰 저장
        Review updatedReview = reviewRepository.save(review);

        // 응답 DTO 생성
        return ReviewResponse.builder()
                .reviewId(updatedReview.getReviewId())
                .grade(updatedReview.getGrade())
                .description(updatedReview.getDescription())
                .createdAt(updatedReview.getCreatedAt())
                .updatedAt(updatedReview.getUpdatedAt())
                .memberId(updatedReview.getMember().getId())
                .bookId(updatedReview.getBook().getBookId())
                .imageUrl(updatedReview.getReviewImage().stream()
                        .map(ReviewImage::getImageUrl)
                        .collect(Collectors.toList()))
                .loginId(updatedReview.getMember().getLoginId())
                .build();
    }

    @Override
    @Transactional
    public ReviewResponse deleteReview(long bookId, long reviewId, Long memberId) {
        // 회원 존재 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new InvalidReviewException("회원이 존재하지 않습니다."));

        // 리뷰 존재 확인
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new InvalidReviewException("리뷰를 찾을 수 없습니다."));

        // 도서 일치 확인
        if (review.getBook().getBookId() != bookId) {
            throw new InvalidReviewException("리뷰가 해당 도서에 속하지 않습니다.");
        }

        // 관리자인지 확인
        boolean isAdmin = (member.getRole().getId() == 2);  // role_id = 2는 관리자

        if (!isAdmin) {
            throw new InvalidReviewException("해당 리뷰를 삭제할 권한이 없습니다.");
        }

        // 삭제 전 응답 DTO 생성
        // 삭제하기 전, 삭제될 리뷰의 모든 정보를 담아두고 이를 반환하기 위함..
        // client 가 어떤리뷰가 삭제되었는지 정보를 받을 수 있다..
        ReviewResponse response = ReviewResponse.builder()
                .reviewId(review.getReviewId())
                .grade(review.getGrade())
                .description(review.getDescription())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .memberId(review.getMember().getId())
                .bookId(review.getBook().getBookId())
                .imageUrl(review.getReviewImage().stream()
                        .map(ReviewImage::getImageUrl)
                        .collect(Collectors.toList()))
                .loginId(review.getMember().getLoginId())
                .build();

        // 리뷰 삭제
        reviewRepository.delete(review);

        return response;
    }

    // 리뷰 작성 개수
    @Override
    public int getReviewCount(long bookId) {
        return reviewRepository.countByBookBookId(bookId);
    }

    @Override
    public ReviewResponse getReviewById(long memberId, long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new InvalidReviewException("리뷰를 찾을 수 없습니다."));


        return ReviewResponse.builder()
                .reviewId(review.getReviewId())
                .grade(review.getGrade())
                .description(review.getDescription())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .memberId(memberId)
                .bookId(review.getBook().getBookId())
                .imageUrl(review.getReviewImage().stream()
                        .map(ReviewImage::getImageUrl)
                        .collect(Collectors.toList()))
                .loginId(review.getMember().getLoginId())
                .build();
    }
}

