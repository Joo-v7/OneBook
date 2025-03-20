package com.nhnacademy.taskapi.book.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookAladinDTO {
    @NotBlank
    private String title;
    @NotBlank
    private String authorName;
    @NotNull
    private LocalDate pubdate;
    @NotBlank
    private String description;
    @NotBlank
    private String isbn13;
    @NotNull
    private int priceSales;
    @NotNull
    private int priceStandard;
    @NotBlank
    private String categoryNames;
    @NotBlank
    private String publisherName;
    @NotNull
    private long salesPoint;
    private String cover;
}
