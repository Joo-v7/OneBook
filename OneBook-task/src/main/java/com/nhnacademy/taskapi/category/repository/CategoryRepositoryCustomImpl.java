package com.nhnacademy.taskapi.category.repository;

import com.nhnacademy.taskapi.category.domain.Category;
import com.nhnacademy.taskapi.category.domain.QCategory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@RequiredArgsConstructor
public class CategoryRepositoryCustomImpl implements CategoryRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    QCategory category = QCategory.category;

    @Override
    public List<Category> findTopLevelCategories() {
        return queryFactory.selectFrom(category)
                .where(category.parentCategory.isNull())
                .fetch();
    }
}
