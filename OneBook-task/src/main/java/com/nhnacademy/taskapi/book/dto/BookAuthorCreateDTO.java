package com.nhnacademy.taskapi.book.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookAuthorCreateDTO {
    @NotNull(message = "bookId는 필수입니다 !")
    private long bookId;
    @NotNull(message = "authorId는 필수입니다 !")
    private int authorId;
}
