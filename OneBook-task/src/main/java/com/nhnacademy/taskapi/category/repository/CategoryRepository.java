package com.nhnacademy.taskapi.category.repository;

import com.nhnacademy.taskapi.category.domain.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer>, CategoryRepositoryCustom {
    Category findByName(String name);
    boolean existsByName(String name);

//    @Query("SELECT c FROM Category c WHERE c.parentCategory IS NULL")
//    List<Category> findTopLevelCategories();

    // 특정 카테고리의 하위 카테고리 조회
    List<Category> findByParentCategory(Category parentCategory);

    Page<Category> findAll(Pageable pageable);


}
