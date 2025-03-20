package com.nhnacademy.taskapi.book.dto;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookCategorySaveDTO {
    @NotNull(message = "categoryId는 필수입니다 !")
    private int categoryId;
    @NotNull(message = "bookId는 필수입니다 !")
    private long bookId;
}
