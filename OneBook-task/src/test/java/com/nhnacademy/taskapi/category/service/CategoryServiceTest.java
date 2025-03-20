package com.nhnacademy.taskapi.category.service;


import com.nhnacademy.taskapi.category.domain.Category;
import com.nhnacademy.taskapi.category.dto.CategoryCreateDTO;
import com.nhnacademy.taskapi.category.dto.CategoryUpdateDTO;
import com.nhnacademy.taskapi.category.exception.CategoryNameDuplicateException;
import com.nhnacademy.taskapi.category.exception.CategoryNotFoundException;
import com.nhnacademy.taskapi.category.exception.InvalidCategoryNameException;
import com.nhnacademy.taskapi.category.repository.CategoryRepository;
import com.nhnacademy.taskapi.category.service.Impl.CategoryServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;


    @Test
    @DisplayName("addCategory_Success_High")
    public void addCategory_Success() {
        String categoryName = "TestCategory";

        Category category = new Category();
        category.setName(categoryName);
        category.setParentCategory(null);



        CategoryCreateDTO dto = new CategoryCreateDTO();
        dto.setCategoryName(categoryName);
        dto.setCategory(null);

        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category result = categoryService.addCategory(dto);

        assertNotNull(result);
        assertEquals(categoryName, result.getName());
    }

    @Test
    @DisplayName("addCategory_Success_Low")
    public void addCategory_Success_Low() {
        String categoryName = "TestCategory";

        Category category = new Category();
        category.setName(categoryName);
        category.setParentCategory(null);


        Category categoryLow = new Category();
        categoryLow.setName("TestCategory_Low");
        categoryLow.setParentCategory(category);

        CategoryCreateDTO dto = new CategoryCreateDTO();
        dto.setCategoryName("TestCategory_Low");
        dto.setCategory(category);


        when(categoryRepository.save(any(Category.class))).thenReturn(categoryLow);

        Category result = categoryService.addCategory(dto);

        assertNotNull(result);
        assertEquals(categoryLow.getName(), result.getName());
        assertEquals(categoryLow.getParentCategory().getName(), result.getParentCategory().getName());
    }

    @Test
    @DisplayName("addCategory_Fail_CategoryName is Null")
    void addCategory_Fail_InvalidCategoryName() {
        String categoryName = null;
        CategoryCreateDTO dto = new CategoryCreateDTO();
        dto.setCategoryName(categoryName);
        dto.setCategory(null);

        Assertions.assertThrows(InvalidCategoryNameException.class, () -> categoryService.addCategory(dto) );
    }

    @Test
    @DisplayName("addCategory_Fail_CategoryName is Empty")
    void addCategory_Fail_InvalidCategoryName_Empty() {
        String categoryName = "";
        CategoryCreateDTO dto = new CategoryCreateDTO();
        dto.setCategoryName(categoryName);
        dto.setCategory(null);

        Assertions.assertThrows(InvalidCategoryNameException.class, () -> categoryService.addCategory(dto) );
    }

    @Test
    @DisplayName("addCategory_Fail_CategoryName Duplicate")
    void addCategory_Fail_InvalidCategoryName_Duplicate() {
        when(categoryRepository.existsByName(any(String.class))).thenReturn(true);

        CategoryCreateDTO dto = new CategoryCreateDTO();
        dto.setCategoryName("test");
        dto.setCategory(null);

        Assertions.assertThrows(CategoryNameDuplicateException.class, () -> categoryService.addCategory(dto));
    }


    @Test
    @DisplayName("addCategoryByAladin_Success")
    void addCategoryByAladin_Success() {
        // Given
        String categoryName = "Test>Test1>Test2";
        String[] nameList = categoryName.split(">");

        // Mocking findByName to return null for all categories
        when(categoryRepository.findByName(any(String.class))).thenReturn(null);

        // When
        Category result = categoryService.addCategoryByAladin(categoryName);

        // Then
        // Verify save is called for each category
        verify(categoryRepository, times(nameList.length)).save(any(Category.class));

        // Verify the result is the last category
        assertNotNull(result);
        assertEquals("Test2", result.getName());

        // Verify parent-child relationships
        ArgumentCaptor<Category> categoryCaptor = ArgumentCaptor.forClass(Category.class);
        verify(categoryRepository, times(nameList.length)).save(categoryCaptor.capture());

        List<Category> savedCategories = categoryCaptor.getAllValues();
        assertEquals(nameList.length, savedCategories.size());

        // Check hierarchy
        for (int i = 0; i < nameList.length; i++) {
            Category savedCategory = savedCategories.get(i);
            assertEquals(nameList[i], savedCategory.getName());
            if (i == 0) {
                assertNull(savedCategory.getParentCategory());
            } else {
                assertEquals(nameList[i - 1], savedCategory.getParentCategory().getName());
            }
        }
    }


    @Test
    @DisplayName("addCategoryByAladin_Success_alreadyExist")
    void addCategoryByAladin_Success_alreadyExist() {
        // Given
        String categoryName = "Test>Test1>Test2";
        String[] nameList = categoryName.split(">");

        Category category = new Category();
        category.setName("Test");
        category.setParentCategory(null);

        // Mocking findByName to return null for all categories
        when(categoryRepository.findByName(any(String.class))).thenReturn(category);

        // When
        Category result = categoryService.addCategoryByAladin(categoryName);




    }




    @Test
    @DisplayName("updateCategory_Success")
    void updateCategory_Success() {
        Category category = new Category();
        category.setCategoryId(1);
        category.setName("TestCategory");
        category.setParentCategory(null);

        CategoryUpdateDTO dto = new CategoryUpdateDTO();
        dto.setCategoryId(category.getCategoryId());
        dto.setCategoryName("RealCategory");


        when(categoryRepository.findById(any(Integer.class))).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category result = categoryService.updateCategory(dto);

        assertNotNull(result);
        assertEquals("RealCategory", result.getName());
    }

    @Test
    @DisplayName("updateCategory_Fail_NotFound")
    void updateCategory_Fail_NotFound() {

        when(categoryRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(CategoryNotFoundException.class, () -> categoryService.updateCategory(new CategoryUpdateDTO()));
    }

    @Test
    @DisplayName("updateCategory_Fail_Duplicate")
    void updateCategory_Fail_Duplicate() {

        Category category = new Category();
        category.setCategoryId(1);
        category.setName("TestCategory");
        category.setParentCategory(null);

        CategoryUpdateDTO dto = new CategoryUpdateDTO();
        dto.setCategoryId(1);
        dto.setCategoryName("TestCategory");

        when(categoryRepository.findById(any(Integer.class))).thenReturn(Optional.of(category));
        when(categoryRepository.existsByName(any(String.class))).thenReturn(true);

        Assertions.assertThrows(CategoryNameDuplicateException.class, () -> categoryService.updateCategory(dto));
    }

    @Test
    @DisplayName("updateCategory_Fail_InvalidName_Null")
    void updateCategory_Fail_InvalidName() {
        Category category = new Category();
        category.setCategoryId(1);
        category.setName("TestCategory");
        category.setParentCategory(null);

        CategoryUpdateDTO dto = new CategoryUpdateDTO();
        dto.setCategoryId(1);
        dto.setCategoryName(null);

        when(categoryRepository.findById(any(Integer.class))).thenReturn(Optional.of(category));
        when(categoryRepository.existsByName(any())).thenReturn(false);

        Assertions.assertThrows(InvalidCategoryNameException.class, () -> categoryService.updateCategory(dto));
    }

    @Test
    @DisplayName("updateCategory_Fail_InvalidName_Empty")
    void updateCategory_Fail_InvalidName_Empty() {
        Category category = new Category();
        category.setCategoryId(1);
        category.setName("TestCategory");
        category.setParentCategory(null);

        CategoryUpdateDTO dto = new CategoryUpdateDTO();
        dto.setCategoryId(1);
        dto.setCategoryName("");

        when(categoryRepository.findById(any(Integer.class))).thenReturn(Optional.of(category));
        when(categoryRepository.existsByName(any())).thenReturn(false);

        Assertions.assertThrows(InvalidCategoryNameException.class, () -> categoryService.updateCategory(dto));
    }

//    @Test
//    @DisplayName("deleteCategory_Success")
//    void deleteCategory_Success() {
//        Category category = new Category();
//        category.setCategoryId(1);
//        category.setName("TestCategory");
//        category.setParentCategory(null);
//
//        when(categoryRepository.findById(any(Integer.class))).thenReturn(Optional.of(category));
//
//        categoryService.deleteCategory(1);
//        verify(categoryRepository).delete(any(Category.class));
//    }
//
//    @Test
//    @DisplayName("deleteCategory_Fail_NotFound")
//    void deleteCategory_Fail_NotFound() {
//        when(categoryRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
//
//        Assertions.assertThrows(CategoryNotFoundException.class, () -> categoryService.deleteCategory(1));
//    }

    @Test
    @DisplayName("getCategory_Success")
    void getCategory_Success() {
        Category category = new Category();
        category.setCategoryId(1);
        category.setName("TestCategory");
        category.setParentCategory(null);

        when(categoryRepository.findById(any(Integer.class))).thenReturn(Optional.of(category));

        Category result = categoryService.getCategory(1);
        assertNotNull(result);
    }

}
