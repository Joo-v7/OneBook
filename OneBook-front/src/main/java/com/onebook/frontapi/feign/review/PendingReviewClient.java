package com.onebook.frontapi.feign.review;

import com.onebook.frontapi.dto.review.BookReviewableResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "PendingReviewClient", url = "${onebook.gatewayUrl}")
public interface PendingReviewClient {

    @GetMapping("/task/members/pending-reviews")
    List<BookReviewableResponseDto> getPendingReviews();
}
