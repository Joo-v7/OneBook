package com.nhnacademy.taskapi.like.controller;


import com.nhnacademy.taskapi.like.domain.Like;
import com.nhnacademy.taskapi.like.dto.LikePlusMinusDTO;
import com.nhnacademy.taskapi.like.dto.LikeReponse;
import com.nhnacademy.taskapi.like.service.LikeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/task/like")
@Slf4j
@Tag(name = "Like", description = "도서에 대한 사용자의 좋아요를 등록, 삭제, 조회")  // API 그룹 설명 추가
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/{bookId}")
    public ResponseEntity<Boolean> toggleLike(
            @PathVariable long bookId,
            @RequestHeader("X-MEMBER-ID")long memberId) {

        // 현재 좋아요 상태 확인 후 토글 처리
        boolean isLiked = likeService.toggleLike(bookId, memberId);
        return ResponseEntity.ok(isLiked);
    }


    @GetMapping("/{bookId}")
    public ResponseEntity<LikeReponse> getLike(@PathVariable("bookId") long bookId) {
        LikeReponse like = likeService.getLikeByBook(bookId);
        return ResponseEntity.status(HttpStatus.OK).body(like);
    }
    @GetMapping("/{bookId}/check")
    public ResponseEntity<Boolean> checkLike(@PathVariable("bookId") long bookId,
                                             @RequestHeader("X-MEMBER-ID") long memberId) {
        boolean isLiked = likeService.checkLike(bookId, memberId);
        return ResponseEntity.status(HttpStatus.OK).body(isLiked);
    }

}
