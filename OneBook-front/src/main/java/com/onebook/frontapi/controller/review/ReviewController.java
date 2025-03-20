package com.onebook.frontapi.controller.review;

import com.onebook.frontapi.dto.book.BookDTO;
import com.onebook.frontapi.dto.review.BookReviewableResponseDto;
import com.onebook.frontapi.dto.review.MyReviewResponseDto;
import com.onebook.frontapi.dto.review.ReviewRequestDto;
import com.onebook.frontapi.dto.review.ReviewResponseDto;
import com.onebook.frontapi.feign.review.MyReviewClient;
import com.onebook.frontapi.feign.review.PendingReviewClient;
import com.onebook.frontapi.feign.review.ReviewClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final PendingReviewClient pendingReviewClient;
    private final MyReviewClient myReviewClient;
    private final ReviewClient reviewClient;

    /**
     * 리뷰 작성 폼 페이지
     * @param bookId : 리뷰 작성할 도서 ID
     */
    @GetMapping("/review")
    public String getReviewForm(@RequestParam("bookId") Long bookId, Model model) {
        List<BookReviewableResponseDto> allPending = pendingReviewClient.getPendingReviews();
        BookReviewableResponseDto targetBook = null;
        for (BookReviewableResponseDto dto : allPending) {
            if (dto.getBookId().equals(bookId)) {
                targetBook = dto;
                break;
            }
        }
        model.addAttribute("book", targetBook);
        return "review/reviewForm";
    }

    @GetMapping("/review/pending")
    public String getPendingReviews(Model model) {
        List<BookReviewableResponseDto> pendingBooks = pendingReviewClient.getPendingReviews();
        model.addAttribute("pendingBooks", pendingBooks);
        return "review/pendingReviews";
    }

    @GetMapping("/review/my-review")
    public String getMyReviews(Model model) {
        List<MyReviewResponseDto> myReviews = myReviewClient.getMyReviews();
        model.addAttribute("myReviews", myReviews);
        return "review/myReviews";
    }

    @GetMapping("/review/editForm")
    public String editReviewForm(@RequestParam("reviewId") Long reviewId, Model model) {
        // 리뷰 상세 정보
        ReviewResponseDto review = reviewClient.getReview(reviewId);
        ReviewRequestDto reviewRequestDto = new ReviewRequestDto();
        reviewRequestDto.setGrade(review.getGrade());
        reviewRequestDto.setDescription(review.getDescription());
        reviewRequestDto.setImageUrl(review.getImageUrl());

        model.addAttribute("reviewRequestDto", reviewRequestDto);
        model.addAttribute("bookId", review.getBookId());
        model.addAttribute("reviewId", reviewId);
        return "review/reviewForm";
    }
}
