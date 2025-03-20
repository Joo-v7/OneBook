package com.nhnacademy.taskapi.category.service;

import com.nhnacademy.taskapi.category.domain.Category;
import com.nhnacademy.taskapi.category.dto.CategoryCreateDTO;
import com.nhnacademy.taskapi.category.dto.CategoryUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    Category addCategory(CategoryCreateDTO dto);
    Category updateCategory(CategoryUpdateDTO updateDTO);
    void deleteCategory(int categoryId);

    Category addCategoryByAladin(String categoryName);
    Category getCategory(int categoryId);
    List<Category> getAllCategories();
    List<Category> getTopLevelCategories();
    List<Category> getSubCategories(int categoryId);
    Page<Category> getAllCategoriesByPaging(Pageable pageable);
}
