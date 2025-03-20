package com.nhnacademy.taskapi.like.service;

import com.nhnacademy.taskapi.like.domain.Like;
import com.nhnacademy.taskapi.like.dto.LikePlusMinusDTO;
import com.nhnacademy.taskapi.like.dto.LikeReponse;
import com.nhnacademy.taskapi.member.domain.Member;

public interface LikeService {

    LikeReponse plusLike(long bookId, long memberId);

    void minusLike(LikePlusMinusDTO dto);
    LikeReponse getLikeByBook(long bookId);
    boolean toggleLike(long bookId, long memberId);
    boolean checkLike(long bookId, long memberId);
}
