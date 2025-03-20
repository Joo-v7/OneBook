package com.nhnacademy.taskapi.book.dto;


import com.nhnacademy.taskapi.publisher.domain.Publisher;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BookSearchAllResponse {
    private long bookId;
    private String title;
    private String publisherName;
    private String description;
    private int price;
    private int salePrice;
    private long amount;
    private LocalDate pubdate;
    private boolean status;
}