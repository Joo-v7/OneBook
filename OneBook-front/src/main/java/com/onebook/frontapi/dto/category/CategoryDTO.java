package com.onebook.frontapi.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Setter
@Getter
public class CategoryDTO {
    private int categoryId;

    private CategoryDTO parentCategory;

    private String name;

    private List<CategoryDTO> subCategories;

    private boolean status;
}
