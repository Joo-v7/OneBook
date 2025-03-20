package com.onebook.frontapi.dto.book;

import com.onebook.frontapi.dto.publisher.PublisherDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Setter
@Getter
public class ProductDTO {
    private long  bookId;

    private PublisherDTO publisher;


    private String title;


    private String content;

    private String description;


    private String isbn13;

    private int price;


    private int salePrice;

    private long amount;

    private long views;


    private LocalDate pubdate;

    private String url;
}
