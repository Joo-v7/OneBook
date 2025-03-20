package com.onebook.frontapi.dto.category;


import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCategoryDTO {
    private String categoryName;

    private CategoryDTO category;
}
