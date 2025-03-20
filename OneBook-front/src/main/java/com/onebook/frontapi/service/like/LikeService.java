package com.onebook.frontapi.service.like;

import com.onebook.frontapi.dto.like.LikeDto;
import com.onebook.frontapi.feign.like.LikeClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeClient likeClient;

    public boolean createLike(long bookId) {
        return likeClient.createLike(bookId);
    }

    public LikeDto getLike(Long bookId) {
        return likeClient.getLike(bookId);
    }

    public boolean isLiked(Long bookId) {
        return likeClient.checkLike(bookId);
    }

}
