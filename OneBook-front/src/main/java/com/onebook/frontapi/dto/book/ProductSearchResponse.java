package com.onebook.frontapi.dto.book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchResponse {
    private long bookId;
    private String title;
    private String publisherName;
    private String description;
    private int price;
    private int salePrice;
    private long amount;
    private LocalDate pubdate;
    private boolean status;
    private String authorName;
    private String imageUrl;
}
