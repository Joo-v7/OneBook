package com.onebook.frontapi.dto.book;

import com.onebook.frontapi.dto.author.AuthorDTO;
import com.onebook.frontapi.dto.publisher.PublisherDTO;
import com.onebook.frontapi.dto.tag.TagDTO;
import com.onebook.frontapi.dto.tag.TagResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class BookSaveDTO {

    private String title;

    private AuthorDTO author;

    private String content;

    private String pubdate;

    private String description;

    private String isbn13;

    private Integer priceSales;

    private Integer price;

    private int categoryId;

    private PublisherDTO publisher;
    private TagResponse tag;
    private Integer stock;
}
