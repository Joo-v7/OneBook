package com.onebook.frontapi.feign.like;

import com.onebook.frontapi.dto.like.LikeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "likeClient", url = "${onebook.gatewayUrl}")
public interface LikeClient {
    @PostMapping("/task/like/{bookId}")
    boolean createLike(@PathVariable long bookId);

    @GetMapping("/task/like/{bookId}")
    LikeDto getLike(@PathVariable("bookId") Long bookId);

    @GetMapping("/task/like/{bookId}/check")
    boolean checkLike(@PathVariable("bookId") Long bookId);

}