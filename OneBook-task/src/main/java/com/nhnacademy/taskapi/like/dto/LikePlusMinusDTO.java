package com.nhnacademy.taskapi.like.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LikePlusMinusDTO {
    @NotNull
    private long bookId;
    @NotNull
    private long memberId;
}
