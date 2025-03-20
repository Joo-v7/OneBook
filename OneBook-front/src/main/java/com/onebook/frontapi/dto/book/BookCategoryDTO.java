package com.onebook.frontapi.dto.book;

import com.onebook.frontapi.dto.category.CategoryDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookCategoryDTO {
    private Long bookCategoryId;
    private BookDTO book;
    private CategoryDTO category;


}
