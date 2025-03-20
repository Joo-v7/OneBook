package com.nhnacademy.taskapi.coupon.repository.policies;

import com.nhnacademy.taskapi.category.domain.Category;
import com.nhnacademy.taskapi.config.QuerydslConfig;
import com.nhnacademy.taskapi.coupon.domain.entity.policies.PricePolicyForCategory;
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

class PricePoliciesForCategoryRepositoryTest {

    @Autowired
    private PricePoliciesForCategoryRepository pricePoliciesForCategoryRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private PricePolicyForCategory pricePolicyForCategory;

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

        pricePolicyForCategory = new PricePolicyForCategory(
                20000,
                5000,
                LocalDateTime.of(2024,1,1,12,00),
                LocalDateTime.of(2024,1,10,12,00),
                "테스트용 정액정책 for Book",
                "테스트용 정액정책 for Book Description",
                category,
                policyStatus
        );
    }

    @Test
    @DisplayName("PricePolicyBook - save - 동작테스트")
    void savePricePolicyBookTest(){

        pricePoliciesForCategoryRepository.save(pricePolicyForCategory);
    }

    @Test
    @DisplayName("PricePolicyCategory - findAll - 동작테스트")
    void findAllTest(){

        int pageNo = 1;
        int PAGE_SIZE = 10;

        Pageable pageable = PageRequest.of(pageNo, PAGE_SIZE);
        pricePoliciesForCategoryRepository.findAll(pageable);
    }

    @Test
    @DisplayName("PricePolicyCategory - findById - 동작테스트")
    void findByIdTest(){

        pricePoliciesForCategoryRepository.findById(0L);
    }

    @Test
    @DisplayName("PricePolicyCategory - deleteById - 동작테스트")
    void deleteByIdTest(){

        pricePoliciesForCategoryRepository.deleteById(0L);
    }
}