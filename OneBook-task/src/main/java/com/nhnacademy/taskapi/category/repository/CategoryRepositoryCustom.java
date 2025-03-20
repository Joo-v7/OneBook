package com.nhnacademy.taskapi.category.repository;

import com.nhnacademy.taskapi.category.domain.Category;

import java.util.List;

public interface CategoryRepositoryCustom {
    List<Category> findTopLevelCategories();
}
