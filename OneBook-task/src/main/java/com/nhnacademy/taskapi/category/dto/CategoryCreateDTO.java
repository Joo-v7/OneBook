package com.nhnacademy.taskapi.category.dto;


import com.nhnacademy.taskapi.category.domain.Category;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryCreateDTO {
    @NotBlank
    private String categoryName;
    @Valid
    private Category category;
}
