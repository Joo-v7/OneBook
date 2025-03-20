package com.onebook.frontapi.dto.book;

import com.onebook.frontapi.dto.publisher.PublisherDTO;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;


@Getter
@Setter
public class BookDTO {

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
}
