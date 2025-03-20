package com.onebook.frontapi.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookUpdateDTO {
    private String title;
    private String content;
    private String description;
    private int price;
    private int salePrice;
    private int stock;
}
