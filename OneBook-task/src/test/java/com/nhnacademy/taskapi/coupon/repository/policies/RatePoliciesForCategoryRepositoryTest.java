package com.nhnacademy.taskapi.coupon.repository.policies;

import com.nhnacademy.taskapi.category.domain.Category;
import com.nhnacademy.taskapi.config.QuerydslConfig;
import com.nhnacademy.taskapi.coupon.domain.entity.policies.RatePolicyForCategory;
import com.nhnacademy.taskapi.coupon.domain.entity.status.PolicyStatus;
import com.nhnacademy.taskapi.publisher.domain.Publisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;


@DataJpaTest
@Import(QuerydslConfig.class)

class RatePoliciesForCategoryRepositoryTest {

    @Autowired
    private RatePoliciesForCategoryRepository ratePoliciesForCategoryRepository;

    @Autowired
    TestEntityManager testEntityManager;

    private RatePolicyForCategory ratePolicyForCategory;

    @BeforeEach
    void setUp() throws NoSuchFieldException {

        // 테스트용 출판사
        Publisher publisher = new Publisher();
        publisher.setName("파랑출판사");

        testEntityManager.persist(publisher);

        // 테스트용 카테고리
        Category category = new Category();

        for(Field field : category.getClass().getDeclaredFields()){
            field.setAccessible(true);
        }

        ReflectionUtils.setField(category.getClass().getDeclaredField("name"),category,"소설" );

        testEntityManager.persist(category);

        // 테스트용 정책상태
        PolicyStatus policyStatus = new PolicyStatus();

        for(Field field :policyStatus.getClass().getDeclaredFields()){
            field.setAccessible(true);
        }

        ReflectionUtils.setField(policyStatus.getClass().getDeclaredField("name"), policyStatus,"미사용" );

        testEntityManager.persist(policyStatus);

        // 테스트용 정책 for Category
        ratePolicyForCategory = new RatePolicyForCategory(
                20,
                50000,
                20000,
                LocalDateTime.now(),
                LocalDateTime.now(),
                "웰컴쿠폰",
                "2023년 1월 신입고객에게 지급하는 웰컴쿠폰",
                category,
                policyStatus
        );

    }

    @Test
    @DisplayName("RatePolicyForBook - save - 동작테스트")
    void saveRatePolicyForCategory() {

        ratePoliciesForCategoryRepository.save(ratePolicyForCategory);
    }

    @Test
    @DisplayName("ratePolicyCategory - findAll - 동작테스트")
    void findAllTest(){

        int pageNo = 1;
        int PAGE_SIZE = 10;

        Pageable pageable = PageRequest.of(pageNo, PAGE_SIZE);
        ratePoliciesForCategoryRepository.findAll(pageable);
    }

    @Test
    @DisplayName("ratePolicyCategory - findById - 동작테스트")
    void findByIdTest(){

        ratePoliciesForCategoryRepository.findById(0L);
    }

    @Test
    @DisplayName("RatePolicyCategory - deleteById - 동작테스트")
    void deleteByIdTest(){

        ratePoliciesForCategoryRepository.deleteById(0L);
    }

}