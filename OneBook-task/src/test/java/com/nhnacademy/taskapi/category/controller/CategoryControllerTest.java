package com.nhnacademy.taskapi.category.controller;


import com.nhnacademy.taskapi.category.domain.Category;
import com.nhnacademy.taskapi.category.dto.CategoryCreateDTO;
import com.nhnacademy.taskapi.category.dto.CategoryUpdateDTO;
import com.nhnacademy.taskapi.category.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;


    @Test
    public void testCreateCategoryWithParentAndChild() throws Exception {
        // 부모 카테고리 객체 설정
        Category parentCategory = new Category();
        parentCategory.setCategoryId(1);
        parentCategory.setName("Fiction");

        // 자식 카테고리 객체 설정
        Category childCategory = new Category();
        childCategory.setCategoryId(2);
        childCategory.setName("Science Fiction");
        childCategory.setParentCategory(parentCategory);  // 부모 카테고리 설정

        // CategoryService 모킹: 자식 카테고리 생성
        when(categoryService.addCategory(Mockito.any())).thenReturn(childCategory);

        // POST 요청 테스트 (자식 카테고리 생성)
        mockMvc.perform(post("/task/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Science Fiction\", \"parentCategoryId\": 1}"))
                .andExpect(status().isOk()) // 상태 코드 200 OK
                .andExpect(content().json("{\"categoryId\": 2, \"name\": \"Science Fiction\", \"parentCategory\": {\"categoryId\": 1, \"name\": \"Fiction\"}}")); // 부모 카테고리 확인
    }

    @Test
    public void testModifyCategory() throws Exception {
        // 테스트에 사용할 카테고리 객체 설정
        Category category = new Category();
        category.setCategoryId(1);
        category.setName("Updated Fiction");

        // CategoryService 모킹: 카테고리 수정
        when(categoryService.updateCategory(Mockito.any())).thenReturn(category);

        // PUT 요청 테스트
        mockMvc.perform(put("/task/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"categoryId\": 1, \"name\": \"Updated Fiction\"}"))
                .andExpect(status().isOk()) // 상태 코드 200 OK
                .andExpect(content().json("{\"categoryId\": 1, \"name\": \"Updated Fiction\"}")); // 수정된 카테고리 확인
    }

    @Test
    public void testDeleteCategory() throws Exception {
        // CategoryService 모킹: 카테고리 삭제
        doNothing().when(categoryService).deleteCategory(Mockito.anyInt());

        // DELETE 요청 테스트
        mockMvc.perform(delete("/task/categories/{categoryId}", 1))
                .andExpect(status().isNoContent()); // 상태 코드 204 No Content
    }

    @Test
    public void testGetCategory() throws Exception {
        // 테스트에 사용할 카테고리 객체 설정
        Category category = new Category();
        category.setCategoryId(1);
        category.setName("Fiction");

        // CategoryService 모킹: 카테고리 조회
        when(categoryService.getCategory(Mockito.anyInt())).thenReturn(category);

        // GET 요청 테스트
        mockMvc.perform(get("/task/categories/{categoryId}", 1))
                .andExpect(status().isOk()) // 상태 코드 200 OK
                .andExpect(content().json("{\"categoryId\": 1, \"name\": \"Fiction\"}")); // 카테고리 정보 확인
    }

    @Test
    void testGetCategories() throws Exception {
        // Given
        Category category1 = new Category(1, null, "Category 1", false);
        Category category2 = new Category(2, null, "Category 2", false);
        List<Category> categories = Arrays.asList(category1, category2);

        when(categoryService.getAllCategories()).thenReturn(categories);

        // When & Then
        mockMvc.perform(get("/task/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Category 1"))
                .andExpect(jsonPath("$[1].name").value("Category 2"));
    }

    @Test
    void testGetTopCategories() throws Exception {
        // Given
        Category category1 = new Category(1, null, "Top Category 1", false);
        Category category2 = new Category(2, null, "Top Category 2", false);
        List<Category> topCategories = Arrays.asList(category1, category2);

        when(categoryService.getTopLevelCategories()).thenReturn(topCategories);

        // When & Then
        mockMvc.perform(get("/task/categories/topCategories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Top Category 1"))
                .andExpect(jsonPath("$[1].name").value("Top Category 2"));
    }

    @Test
    void testGetSubCategories() throws Exception {
        // Given
        Category subCategory1 = new Category(1, null, "Sub Category 1", false);
        Category subCategory2 = new Category(2, null, "Sub Category 2", false);
        List<Category> subCategories = Arrays.asList(subCategory1, subCategory2);

        when(categoryService.getSubCategories(1)).thenReturn(subCategories);

        // When & Then
        mockMvc.perform(get("/task/categories/subCategories/{categoryId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Sub Category 1"))
                .andExpect(jsonPath("$[1].name").value("Sub Category 2"));
    }

    @Test
    void testGetAllCategoriesByPaging() throws Exception {
        // Given
        Category category1 = new Category(1, null, "Category 1", false);
        Category category2 = new Category(2, null, "Category 2", false);
        List<Category> categories = Arrays.asList(category1, category2);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Category> categoryPage = new PageImpl<>(categories, pageable, categories.size());

        when(categoryService.getAllCategoriesByPaging(pageable)).thenReturn(categoryPage);

        // When & Then
        mockMvc.perform(get("/task/categories/list")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(2))
                .andExpect(jsonPath("$.content[0].name").value("Category 1"))
                .andExpect(jsonPath("$.content[1].name").value("Category 2"));
    }

}
