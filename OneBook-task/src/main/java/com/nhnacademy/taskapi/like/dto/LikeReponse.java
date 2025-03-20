package com.nhnacademy.taskapi.like.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LikeReponse {
    private long memberId;
    private long bookId;
}
