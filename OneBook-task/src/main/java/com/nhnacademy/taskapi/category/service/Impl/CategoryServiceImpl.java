package com.nhnacademy.taskapi.category.service.Impl;


import com.nhnacademy.taskapi.book.domain.BookCategory;
import com.nhnacademy.taskapi.book.dto.BookCategorySaveDTO;
import com.nhnacademy.taskapi.book.repository.BookCategoryRepository;
import com.nhnacademy.taskapi.book.service.BookCategoryService;
import com.nhnacademy.taskapi.category.domain.Category;
import com.nhnacademy.taskapi.category.dto.CategoryCreateDTO;
import com.nhnacademy.taskapi.category.dto.CategoryUpdateDTO;
import com.nhnacademy.taskapi.category.exception.CategoryNameDuplicateException;
import com.nhnacademy.taskapi.category.exception.CategoryNotFoundException;
import com.nhnacademy.taskapi.category.exception.InvalidCategoryNameException;
import com.nhnacademy.taskapi.category.repository.CategoryRepository;
import com.nhnacademy.taskapi.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final BookCategoryService bookCategoryService;

    @Transactional
    @Override
    public Category addCategory(CategoryCreateDTO dto) {
        if(Objects.isNull(dto.getCategoryName()) || dto.getCategoryName().trim().isEmpty()){
            throw new InvalidCategoryNameException("CategoryName is Null OR Empty !");
        }


        Category category = new Category();

        if(!categoryRepository.existsByName(dto.getCategoryName())){
            if(Objects.isNull(dto.getCategory())){
                // 최상위 카테고리 등록
                category.setName(dto.getCategoryName());
                category.setParentCategory(null);
            }else{
                // 하위 카테고리 등록
                category.setName(dto.getCategoryName());
                category.setParentCategory(dto.getCategory());
            }
        }else{
            throw new CategoryNameDuplicateException("This CategoryName is Exist !");
        }

        return categoryRepository.save(category);
    }


    //알라딘 Api 전용 카테고리 등록
    @Override
    public Category addCategoryByAladin(String categoryName){
        String[] nameList = categoryName.split(">");

        Category parentCategory = null;
        for(String name: nameList){
            Category existCategory = categoryRepository.findByName(name);
            if(Objects.isNull(existCategory)){
                Category newCategory = new Category();
                newCategory.setName(name);
                newCategory.setParentCategory(parentCategory);
                categoryRepository.save(newCategory);
                parentCategory=newCategory;
            }else{
                parentCategory = existCategory;
            }
        }

        return parentCategory;
    }

    @Transactional
    @Override
    public Category updateCategory(CategoryUpdateDTO updateDTO){
        Category category = categoryRepository.findById(updateDTO.getCategoryId()).orElseThrow(() -> new CategoryNotFoundException("this Category Not Exist !"));

        if(categoryRepository.existsByName(updateDTO.getCategoryName())){
            throw new CategoryNameDuplicateException("Category name already Exist !");
        }

        if(Objects.isNull(updateDTO.getCategoryName()) || updateDTO.getCategoryName().trim().isEmpty()){
            throw new InvalidCategoryNameException("This CategoryName is Null OR Empty");
        }
        category.setName(updateDTO.getCategoryName());


        return categoryRepository.save(category);
    }

    @Transactional
    @Override
    public void deleteCategory(int categoryId){
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException("this Category Not Exist !"));

        category.setStatus(true);
    }


    @Override
    public Category getCategory(int categoryId){
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException("This Category Not Found !"));
        return category;
    }

    @Override
    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    @Override
    public List<Category> getTopLevelCategories() {
        return categoryRepository.findTopLevelCategories();
    }
    @Override
    public List<Category> getSubCategories(int categoryId) {
        Category category = getCategory(categoryId);
        return categoryRepository.findByParentCategory(category);
    }


    @Override
    public Page<Category> getAllCategoriesByPaging(Pageable pageable){
        return categoryRepository.findAll(pageable);
    }
}
