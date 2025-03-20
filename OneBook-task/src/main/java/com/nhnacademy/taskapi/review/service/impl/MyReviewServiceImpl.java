package com.nhnacademy.taskapi.review.service.impl;

import com.nhnacademy.taskapi.image.service.ImageService;
import com.nhnacademy.taskapi.member.domain.Member;
import com.nhnacademy.taskapi.member.repository.MemberRepository;
import com.nhnacademy.taskapi.review.domain.Review;
import com.nhnacademy.taskapi.review.dto.MyReviewResponse;
import com.nhnacademy.taskapi.review.exception.InvalidReviewException;
import com.nhnacademy.taskapi.review.repository.ReviewRepository;
import com.nhnacademy.taskapi.review.service.MyReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyReviewServiceImpl implements MyReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final ImageService imageService;

    @Override
    public List<MyReviewResponse> getMyReviews(Long memberId) {

        // 1. 회원 검증
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new InvalidReviewException("존재하지 않는 회원입니다."));

        List<Review> reviews = reviewRepository.findByMemberId(memberId);

        return reviews.stream().map(review -> {
            long bookId = review.getBook().getBookId();
            String bookName = review.getBook().getTitle();

            String bookImageUrl = imageService.getImage(bookId).getUrl();

            LocalDateTime createdAt = review.getCreatedAt();

            int grade = review.getGrade();
            String description = review.getDescription();;

            long reviewId = review.getReviewId();

            return new MyReviewResponse(bookId, bookName, bookImageUrl, createdAt, grade, description, reviewId);
        }).collect(Collectors.toList());
    }
}
