package com.nhnacademy.taskapi.book.dto;


import com.nhnacademy.taskapi.Tag.domain.Tag;
import com.nhnacademy.taskapi.author.domain.Author;
import com.nhnacademy.taskapi.category.domain.Category;
import com.nhnacademy.taskapi.publisher.domain.Publisher;
import jakarta.persistence.GeneratedValue;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookSaveDTO {
    @NotBlank(message = "title은 필수입니다 !")
    @Length(max = 100)
    private String title;
    @Valid
    private Author author;
    @NotBlank
    private String content;
    @NotBlank
    private String pubdate;
    @NotBlank
    private String description;
    @NotBlank
    @Length(max = 13)
    private String isbn13;
    @NotNull
    @Positive
    private Integer priceSales;
    @NotNull
    @Positive
    private Integer price;
    @NotNull
    private int categoryId;
    @Valid
    private Publisher publisher;
    @Valid
    private Tag tag;
    @NotNull
    @Positive
    private Integer stock;
}
