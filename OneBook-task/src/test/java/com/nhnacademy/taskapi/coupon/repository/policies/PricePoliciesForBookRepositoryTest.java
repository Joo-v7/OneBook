package com.nhnacademy.taskapi.coupon.repository.policies;

import com.nhnacademy.taskapi.book.domain.Book;
import com.nhnacademy.taskapi.config.QuerydslConfig;
import com.nhnacademy.taskapi.coupon.domain.entity.policies.PricePolicyForBook;
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

class PricePoliciesForBookRepositoryTest {

    @Autowired
    private PricePoliciesForBookRepository pricePoliciesForBookRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private PricePolicyForBook pricePolicyForBook;

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
                book, LocalDate.of(2023,1,1));

        testEntityManager.persist(book);

        // 테스트용 정책상태
        PolicyStatus policyStatus = new PolicyStatus();

        for(Field field :policyStatus.getClass().getDeclaredFields()){
            field.setAccessible(true);
        }

        ReflectionUtils.setField(policyStatus.getClass().getDeclaredField("name"), policyStatus,"미사용" );

        testEntityManager.persist(policyStatus);

        pricePolicyForBook = new PricePolicyForBook(
                20000,
                5000,
                LocalDateTime.of(2024,1,1,12,00),
                LocalDateTime.of(2024,1,10,12,00),
                "테스트용 정액정책 for Book",
                "테스트용 정액정책 for Book Description",
                book,
                policyStatus
        );
    }

    @Test
    @DisplayName("PricePolicyBook - save - 동작테스트")
    void savePricePolicyBookTest(){

        pricePoliciesForBookRepository.save(pricePolicyForBook);
    }

    @Test
    @DisplayName("PricePolicyBook - findAll - 동작테스트")
    void findAllTest(){

        int pageNo = 1;
        int PAGE_SIZE = 10;

        Pageable pageable = PageRequest.of(pageNo, PAGE_SIZE);
        pricePoliciesForBookRepository.findAll(pageable);
    }

    @Test
    @DisplayName("PricePolicyBook - findById - 동작테스트")
    void findByIdTest(){

        pricePoliciesForBookRepository.findById(0L);
    }

    @Test
    @DisplayName("PricePolicyBook - deleteById - 동작테스트")
    void deleteByIdTest(){

        pricePoliciesForBookRepository.deleteById(0L);
    }
}