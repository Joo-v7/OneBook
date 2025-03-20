package com.nhnacademy.taskapi.coupon.repository.policies;

import com.nhnacademy.taskapi.book.domain.Book;
import com.nhnacademy.taskapi.category.domain.Category;
import com.nhnacademy.taskapi.config.QuerydslConfig;
import com.nhnacademy.taskapi.coupon.domain.entity.policies.RatePolicyForBook;
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
import java.time.LocalDate;
import java.time.LocalDateTime;


@DataJpaTest
@Import(QuerydslConfig.class)

class RatePoliciesForBookRepositoryTest {

    @Autowired
    private RatePoliciesForBookRepository ratePoliciesForBookRepository;

    @Autowired
    TestEntityManager testEntityManager;

    private RatePolicyForBook ratePolicyForBook;

    @BeforeEach
    void setUp() throws NoSuchFieldException {

        // 테스트용 출판사
        Publisher publisher = new Publisher();
        publisher.setName("파랑출판사");

        testEntityManager.persist(publisher);

        // 테스트용 책
        Book book = new Book();

        for(Field field : book.getClass().getDeclaredFields()){
            field.setAccessible(true);
        }

        ReflectionUtils.setField(book.getClass().getDeclaredField("title"), book,"테스트 title" );
        ReflectionUtils.setField(book.getClass().getDeclaredField("content"), book,"테스트 content" );
        ReflectionUtils.setField(book.getClass().getDeclaredField("description"), book,"테스트 description");
        ReflectionUtils.setField(book.getClass().getDeclaredField("isbn13"), book,"테스트 isbn13");
        ReflectionUtils.setField(book.getClass().getDeclaredField("price"), book,10000);
        ReflectionUtils.setField(book.getClass().getDeclaredField("salePrice"), book,8000);
        ReflectionUtils.setField(book.getClass().getDeclaredField("amount"), book,500);
        ReflectionUtils.setField(book.getClass().getDeclaredField("views"), book,0);
        ReflectionUtils.setField(book.getClass().getDeclaredField("publisher"), book,publisher);
        ReflectionUtils.setField(book.getClass().getDeclaredField("pubdate"),
                book,LocalDate.of(2023,1,1));

        testEntityManager.persist(book);

        // 테스트용 정책상태
        PolicyStatus policyStatus = new PolicyStatus();

        for(Field field :policyStatus.getClass().getDeclaredFields()){
            field.setAccessible(true);
        }

        ReflectionUtils.setField(policyStatus.getClass().getDeclaredField("name"), policyStatus,"미사용" );

        testEntityManager.persist(policyStatus);

        // 테스트용 정책 for Book
        ratePolicyForBook = new RatePolicyForBook(
                20,
                50000,
                20000,
                LocalDateTime.now(),
                LocalDateTime.now(),
                "웰컴쿠폰",
                "2023년 1월 신입고객에게 지급하는 웰컴쿠폰",
                book,
                policyStatus
        );

    }


    @Test
    @DisplayName("RatePolicyForBook - save - 동작테스트")
    void saveRatePolicyForBook() {

        ratePoliciesForBookRepository.save(ratePolicyForBook);

    }

    @Test
    @DisplayName("ratePolicyBook - findAll - 동작테스트")
    void findAllTest(){

        int pageNo = 1;
        int PAGE_SIZE = 10;

        Pageable pageable = PageRequest.of(pageNo, PAGE_SIZE);
        ratePoliciesForBookRepository.findAll(pageable);
    }

    @Test
    @DisplayName("ratePolicyBook - findById - 동작테스트")
    void findByIdTest(){

        ratePoliciesForBookRepository.findById(0L);
    }

    @Test
    @DisplayName("RatePolicyBook - deleteById - 동작테스트")
    void deleteByIdTest(){

        ratePoliciesForBookRepository.deleteById(0L);
    }

}