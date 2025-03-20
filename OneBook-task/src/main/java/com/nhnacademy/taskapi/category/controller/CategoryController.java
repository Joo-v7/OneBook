package com.nhnacademy.taskapi.category.controller;


import com.nhnacademy.taskapi.category.domain.Category;
import com.nhnacademy.taskapi.category.dto.CategoryCreateDTO;
import com.nhnacademy.taskapi.category.dto.CategoryUpdateDTO;
import com.nhnacademy.taskapi.category.service.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/task/categories")
@Tag(name = "Category", description = "카테고리를 생성, 수정, 삭제, 조회")  // API 그룹 설명 추가
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Category> createCategories(@RequestBody CategoryCreateDTO dto){
        Category category = categoryService.addCategory(dto);
        return ResponseEntity.ok().body(category);
    }

    @PutMapping
    public ResponseEntity<Category> modifyCategories(@RequestBody CategoryUpdateDTO dto){
        Category category = categoryService.updateCategory(dto);
        return ResponseEntity.ok().body(category);
    }


    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategories(@PathVariable int categoryId){
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{categoryId}")
    public ResponseEntity<Category> getCategories(@PathVariable int categoryId){
        Category category = categoryService.getCategory(categoryId);
        return ResponseEntity.ok().body(category);
    }

    @GetMapping
    public ResponseEntity<List<Category>> getCategories(){
        List<Category> categoryList = categoryService.getAllCategories();
        return ResponseEntity.ok().body(categoryList);
    }

    @GetMapping("/topCategories")
    public List<Category> getTopCategories() {
        return categoryService.getTopLevelCategories(); // 최상위 카테고리와 하위 카테고리를 반환
    }

    @GetMapping("/subCategories/{categoryId}")
    public List<Category> getSubCategories(@PathVariable int categoryId) {
        return categoryService.getSubCategories(categoryId);
    }

    @GetMapping("/list")
    public ResponseEntity<Page<Category>> getAllCategories(Pageable pageable) {
        Page<Category> categories = categoryService.getAllCategoriesByPaging(pageable);
        return ResponseEntity.ok().body(categories);
    }

}
