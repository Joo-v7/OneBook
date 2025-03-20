package com.onebook.frontapi.controller.like;

import com.onebook.frontapi.dto.like.LikeDto;
import com.onebook.frontapi.dto.member.MemberResponse;
import com.onebook.frontapi.feign.member.MemberClient;
import com.onebook.frontapi.service.like.LikeService;
import com.onebook.frontapi.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@RequestMapping("/like")
@Slf4j
public class LikeController {
    private final LikeService likeService;
    private final MemberService memberClient;


    @GetMapping
    @ResponseBody
    public boolean addLike(@RequestParam("bookId") long bookId) {
        return likeService.createLike(bookId);
    }

    @GetMapping("/check")
    @ResponseBody
    public boolean checkLike(@RequestParam("bookId") long bookId) {
        return likeService.isLiked(bookId);
    }
}
