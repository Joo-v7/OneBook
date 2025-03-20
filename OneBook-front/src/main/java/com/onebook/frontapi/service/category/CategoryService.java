package com.onebook.frontapi.service.category;

import com.onebook.frontapi.dto.category.CategoryDTO;
import com.onebook.frontapi.dto.category.CategoryUpdateDTO;
import com.onebook.frontapi.dto.category.CreateCategoryDTO;
import com.onebook.frontapi.feign.category.CategoryClient;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryClient categoryClient;

    public CategoryDTO createCategory(CreateCategoryDTO dto) {
        return categoryClient.createCategory(dto);
    }

    public List<CategoryDTO> getTopCategories() {
        List<CategoryDTO> topCategories =  categoryClient.getTopCategories();

        for (CategoryDTO category : topCategories) {
            List<CategoryDTO> subCategories = categoryClient.getSubCategories(category.getCategoryId());  // 하위 카테고리 조회 API 호출
            category.setSubCategories(subCategories);
        }

        return topCategories;
    }

    public CategoryDTO getCategoryById(Integer categoryId) {
        return categoryClient.getCategoryById(categoryId);
    }

    public CategoryDTO updateCategory(CategoryUpdateDTO dto){
        return categoryClient.updateCategory(dto);
    }

    public void deleteCategoryById(Integer categoryId) {
        categoryClient.deleteCategoryById(categoryId);
    }

    public Page<CategoryDTO> getAllCategories(Pageable pageable) {
        return categoryClient.listCategories(pageable);
    }
}
