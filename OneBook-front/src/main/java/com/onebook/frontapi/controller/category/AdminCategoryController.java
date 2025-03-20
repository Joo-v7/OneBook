package com.onebook.frontapi.controller.category;

import com.onebook.frontapi.dto.category.CategoryDTO;
import com.onebook.frontapi.dto.category.CategoryUpdateDTO;
import com.onebook.frontapi.dto.category.CreateCategoryDTO;
import com.onebook.frontapi.service.category.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/category")
@Slf4j
public class AdminCategoryController {
    private final CategoryService categoryService;

    @GetMapping("/category-list")
    public String categoryList(@RequestParam(value = "categoryId", required = false) Integer categoryId,
                               Model model) {
        if(Objects.nonNull(categoryId)) {
            model.addAttribute("categoryId", categoryId);
        }

        return "admin/categoryList";
    }

    @GetMapping("/category-mini-list")
    public String categoryMiniList(@RequestParam(defaultValue = "0") int page,
                                   Model model) {
        Page<CategoryDTO> categoryList = categoryService.getAllCategories(PageRequest.of(page, 10));
        model.addAttribute("categoryList", categoryList);

        return "admin/categoryMiniList";
    }

    @GetMapping("/create")
    public String createCategoryForm(@RequestParam(value = "categoryId", required = false) Integer categoryId,
                                     Model model) {
        if(Objects.nonNull(categoryId)) {
            model.addAttribute("categoryId", categoryId);
        }


        return "category/categoryRegister";
    }
    @PostMapping("/create")
    public String createCategory(@RequestParam(value = "categoryId", required = false) Integer categoryId,
                                 @RequestParam("categoryName") String name) {
        CreateCategoryDTO dto = new CreateCategoryDTO();
        dto.setCategoryName(name);
        if(Objects.isNull(categoryId)) {
            dto.setCategory(null);
        }else{
            dto.setCategory(categoryService.getCategoryById(categoryId));
        }
        categoryService.createCategory(dto);
        return "redirect:/admin/category/category-list";
    }

    @GetMapping("/updateForm")
    public String updateForm(@RequestParam(value = "categoryId", required = false) Integer categoryId,
                             Model model) {
        CategoryDTO category = null;
        if(Objects.nonNull(categoryId)) {
            category = categoryService.getCategoryById(categoryId);
            model.addAttribute("categoryId", categoryId);
            model.addAttribute("category", category);
        }
        return "category/categoryUpdateForm";
    }


    @PutMapping("/update")
    public String updateCategory(@RequestParam(value = "categoryId", required = false) Integer categoryId,
                                 @RequestParam(value = "categoryName") String name,
                                 Model model) {
        CategoryUpdateDTO dto = new CategoryUpdateDTO();
        dto.setCategoryId(categoryId);
        dto.setCategoryName(name);
        log.info("CategoryName: {}", name);
        categoryService.updateCategory(dto);
        return "redirect:/admin/category/category-list";
    }

    @DeleteMapping("/delete")
    public String deleteCategory(@RequestParam(value = "categoryId") int categoryId){
        log.info("CategoryId: {}", categoryId);

        categoryService.deleteCategoryById(categoryId);
        return "redirect:/admin/category/category-list";
    }



}
