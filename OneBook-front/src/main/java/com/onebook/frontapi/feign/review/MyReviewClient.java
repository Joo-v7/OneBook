package com.onebook.frontapi.feign.review;

import com.onebook.frontapi.dto.review.MyReviewResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "myReviewClient",url = "${onebook.gatewayUrl}" )
public interface MyReviewClient {

    @GetMapping("/task/members/my-reviews")
    List<MyReviewResponseDto> getMyReviews();
}
