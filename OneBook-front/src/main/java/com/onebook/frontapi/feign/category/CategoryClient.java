package com.onebook.frontapi.feign.category;


import com.onebook.frontapi.dto.category.CategoryDTO;
import com.onebook.frontapi.dto.category.CategoryUpdateDTO;
import com.onebook.frontapi.dto.category.CreateCategoryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "categoryClient",  url = "${onebook.gatewayUrl}")
public interface CategoryClient {
    @PostMapping("/task/categories")
    CategoryDTO createCategory(@RequestBody CreateCategoryDTO categoryDTO);

    @PutMapping("/task/categories")
    CategoryDTO updateCategory(@RequestBody CategoryUpdateDTO dto);

    @GetMapping("/task/categories/topCategories")
    List<CategoryDTO> getTopCategories();

    @GetMapping("/task/categories/subCategories/{categoryId}")
    List<CategoryDTO> getSubCategories(@PathVariable int categoryId);

    @GetMapping("/task/categories")
    ResponseEntity<List<CategoryDTO>> getCategories();
  
    @GetMapping("/task/categories/{categoryId}")
    CategoryDTO getCategoryById(@PathVariable int categoryId);

    @DeleteMapping("/task/categories/{categoryId}")
    void deleteCategoryById(@PathVariable int categoryId);

    @GetMapping("/task/categories/list")
    Page<CategoryDTO> listCategories(Pageable pageable);

}
