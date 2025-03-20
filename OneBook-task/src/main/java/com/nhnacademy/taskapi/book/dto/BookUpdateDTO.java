package com.nhnacademy.taskapi.book.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookUpdateDTO {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotBlank
    private String description;
    @NotNull
    @Positive
    private int price;
    @NotNull
    @Positive
    private int salePrice;
    @NotNull
    @Positive
    private int stock;

}
