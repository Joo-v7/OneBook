package com.onebook.frontapi.dto.category;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryUpdateDTO {
    private int categoryId;
    private String categoryName;
}
