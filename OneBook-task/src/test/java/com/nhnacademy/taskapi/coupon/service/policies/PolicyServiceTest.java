//package com.nhnacademy.taskapi.coupon.service.policies;
//
//import com.nhnacademy.taskapi.book.domain.Book;
//import com.nhnacademy.taskapi.book.exception.BookNotFoundException;
//import com.nhnacademy.taskapi.book.repository.BookRepository;
//import com.nhnacademy.taskapi.category.domain.Category;
//import com.nhnacademy.taskapi.category.exception.CategoryNotFoundException;
//import com.nhnacademy.taskapi.category.repository.CategoryRepository;
//import com.nhnacademy.taskapi.coupon.domain.dto.policies.request.create.AddPricePolicyForBookRequest;
//import com.nhnacademy.taskapi.coupon.domain.dto.policies.request.create.AddPricePolicyForCategoryRequest;
//import com.nhnacademy.taskapi.coupon.domain.dto.policies.request.create.AddRatePolicyForBookRequest;
//import com.nhnacademy.taskapi.coupon.domain.dto.policies.request.create.AddRatePolicyForCategoryRequest;
//import com.nhnacademy.taskapi.coupon.domain.dto.policies.response.PricePolicyForBookResponse;
//import com.nhnacademy.taskapi.coupon.domain.dto.policies.response.PricePolicyForCategoryResponse;
//import com.nhnacademy.taskapi.coupon.domain.dto.policies.response.RatePolicyForBookResponse;
//import com.nhnacademy.taskapi.coupon.domain.dto.policies.response.RatePolicyForCategoryResponse;
//import com.nhnacademy.taskapi.coupon.domain.entity.policies.PricePolicyForBook;
//import com.nhnacademy.taskapi.coupon.domain.entity.policies.PricePolicyForCategory;
//import com.nhnacademy.taskapi.coupon.domain.entity.policies.RatePolicyForBook;
//import com.nhnacademy.taskapi.coupon.domain.entity.policies.RatePolicyForCategory;
//import com.nhnacademy.taskapi.coupon.domain.entity.status.PolicyStatus;
//import com.nhnacademy.taskapi.coupon.exception.PolicyNotFoundException;
//import com.nhnacademy.taskapi.coupon.exception.PolicyStatusNotFoundException;
//import com.nhnacademy.taskapi.coupon.repository.policies.PricePoliciesForBookRepository;
//import com.nhnacademy.taskapi.coupon.repository.policies.PricePoliciesForCategoryRepository;
//import com.nhnacademy.taskapi.coupon.repository.policies.RatePoliciesForBookRepository;
//import com.nhnacademy.taskapi.coupon.repository.policies.RatePoliciesForCategoryRepository;
//import com.nhnacademy.taskapi.coupon.repository.status.PolicyStatusRepository;
//import com.nhnacademy.taskapi.publisher.domain.Publisher;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.util.ReflectionUtils;
//import org.springframework.util.Assert;
//
//import java.lang.reflect.Field;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@ExtendWith(MockitoExtension.class)
//class PolicyServiceTest {
//
//    @Mock
//    private CategoryRepository categoryRepository;
//    @Mock
//    private BookRepository bookRepository;
//    @Mock
//    private RatePoliciesForBookRepository ratePoliciesForBookRepository;
//    @Mock
//    private RatePoliciesForCategoryRepository ratePoliciesForCategoryRepository;
//    @Mock
//    private PricePoliciesForBookRepository pricePoliciesForBookRepository;
//    @Mock
//    private PricePoliciesForCategoryRepository pricePoliciesForCategoryRepository;
//    @Mock
//    private  PolicyStatusRepository policyStatusRepository;
//    @InjectMocks
//    private PolicyService policyService;
//
//    private Book book;
//    private Category category;
//    private PolicyStatus usedStatus;
//    private PolicyStatus unusedStatus;
//
//
//
//    @BeforeEach
//    void setUp() throws NoSuchFieldException {
//        // 테스트용 출판사
//        Publisher publisher = new Publisher();
//        publisher.setName("파랑출판사");
//
//        // 테스트용 도서
//        book  = new Book();
//        for(Field field : book.getClass().getDeclaredFields()){
//            field.setAccessible(true);
//        }
//        ReflectionUtils.setField(book.getClass().getDeclaredField("bookId"), book,1L );
//        ReflectionUtils.setField(book.getClass().getDeclaredField("title"), book,"테스트용 도서" );
//        ReflectionUtils.setField(book.getClass().getDeclaredField("content"), book,"테스트용 content" );
//        ReflectionUtils.setField(book.getClass().getDeclaredField("description"), book,"테스트용 description");
//        ReflectionUtils.setField(book.getClass().getDeclaredField("isbn13"), book,"테스트용 Isbn13");
//        ReflectionUtils.setField(book.getClass().getDeclaredField("price"), book,10000);
//        ReflectionUtils.setField(book.getClass().getDeclaredField("salePrice"), book,8000);
//        ReflectionUtils.setField(book.getClass().getDeclaredField("amount"), book,500);
//        ReflectionUtils.setField(book.getClass().getDeclaredField("views"), book,0);
//        ReflectionUtils.setField(book.getClass().getDeclaredField("publisher"), book,publisher);
//        ReflectionUtils.setField(book.getClass().getDeclaredField("pubdate"),
//                book, LocalDate.of(2023,1,1));
//
//        // 테스트용 카테고리
//        category = new Category();
//        for(Field field : category.getClass().getDeclaredFields()){
//            field.setAccessible(true);
//        }
//        ReflectionUtils.setField(category.getClass().getDeclaredField("name"),category,"테스트용 카테고리" );
//
//        // 사용됨
//        usedStatus = new PolicyStatus();
//        for(Field field : usedStatus.getClass().getDeclaredFields()){
//            field.setAccessible(true);
//        }
//        ReflectionUtils.setField(usedStatus.getClass().getDeclaredField("policyStatusId"), usedStatus,0);
//        ReflectionUtils.setField(usedStatus.getClass().getDeclaredField("name"), usedStatus,"사용됨" );
//
//        // 사용되지않음
//        unusedStatus = new PolicyStatus();
//        for(Field field : unusedStatus.getClass().getDeclaredFields()){
//            field.setAccessible(true);
//        }
//        ReflectionUtils.setField(usedStatus.getClass().getDeclaredField("policyStatusId"), unusedStatus,1);
//        ReflectionUtils.setField(usedStatus.getClass().getDeclaredField("name"), unusedStatus,"미사용" );
//
//
//
//    }
//
//    @Test
//    @DisplayName("정률 정책 for Book을 새로 추가 - 정상 동작 테스트")
//    void addRatePolicyForBookTest() throws NoSuchFieldException {
//
//        // 정책 추가 요청
//        AddRatePolicyForBookRequest addRatePolicyForBookRequest = new AddRatePolicyForBookRequest();
//        for(Field field : addRatePolicyForBookRequest.getClass().getDeclaredFields()){
//            field.setAccessible(true);
//        }
//        ReflectionUtils.setField(
//                addRatePolicyForBookRequest.getClass().getDeclaredField("discountRate")
//                ,addRatePolicyForBookRequest
//                , 10);
//        ReflectionUtils.setField(
//                addRatePolicyForBookRequest.getClass().getDeclaredField("minimumOrderAmount")
//                ,addRatePolicyForBookRequest
//                , 30000);
//        ReflectionUtils.setField(
//                addRatePolicyForBookRequest.getClass().getDeclaredField("maximumDiscountPrice")
//                ,addRatePolicyForBookRequest
//                , 10000);
//        ReflectionUtils.setField(
//                addRatePolicyForBookRequest.getClass().getDeclaredField("expirationPeriodStart")
//                ,addRatePolicyForBookRequest
//                , LocalDateTime.of(2024,1,1,12,0));
//        ReflectionUtils.setField(
//                addRatePolicyForBookRequest.getClass().getDeclaredField("expirationPeriodEnd")
//                ,addRatePolicyForBookRequest
//                , LocalDateTime.of(2024,1,10,12,0));
//        ReflectionUtils.setField(
//                addRatePolicyForBookRequest.getClass().getDeclaredField("name")
//                ,addRatePolicyForBookRequest
//                , "테스트용 정책");
//        ReflectionUtils.setField(
//                addRatePolicyForBookRequest.getClass().getDeclaredField("description")
//                ,addRatePolicyForBookRequest
//                , "테스트용 정책 설명");
//        ReflectionUtils.setField(
//                addRatePolicyForBookRequest.getClass().getDeclaredField("bookId")
//                ,addRatePolicyForBookRequest
//                ,1L);
//        ReflectionUtils.setField(
//                addRatePolicyForBookRequest.getClass().getDeclaredField("policyStatusId")
//                ,addRatePolicyForBookRequest
//                ,0);
//
//        Mockito.when(bookRepository.findByIsbn13(addRatePolicyForBookRequest.getBookIsbn13()))
//                .thenReturn(book);
////        Mockito.when(policyStatusRepository.findById(addRatePolicyForBookRequest.getPolicyStatusId()))
////                .thenReturn(Optional.of(usedStatus));
//
//        // 예싱 응답
//        RatePolicyForBookResponse expected =
//                new RatePolicyForBookResponse(
//                        null,
//                        10,
//                        30000,
//                        10000,
//                        LocalDateTime.of(2024,1,1,12,0),
//                        LocalDateTime.of(2024,1,10,12,0),
//                        "테스트용 정책",
//                        "테스트용 정책 설명",
//                        "테스트용 도서",
//                        "테스트용 Isbn13",
//                        "사용됨"
//                );
//
//        // 실제 응답
//        RatePolicyForBookResponse actual
//                = policyService.addRatePolicyForBook(addRatePolicyForBookRequest);
//
//        // addRatePolicyForBook() 메서드 안의 ratePoliciesForBookRepository.save()가 실행되었는지 검증
//        Mockito.verify(ratePoliciesForBookRepository,Mockito.times(1))
//                .save(Mockito.any(RatePolicyForBook.class));
//
//        // 예상응답과 실제응답이 같은지 검증
//        Assertions.assertEquals(
//                expected,actual
//        );
//
//    }
//
//    @Test
//    @DisplayName("정률 정책 for Book을 새로 추가 - 해당하는 ID의 도서가 없을때 - Exception이 정상적으로 발생하는지")
//    void addRatePolicyForBookWhenBookNotFoundExceptionTest() throws NoSuchFieldException {
//
//
//        // 정책 추가 요청
//        AddRatePolicyForBookRequest addRatePolicyForBookRequest = new AddRatePolicyForBookRequest();
//        for(Field field : addRatePolicyForBookRequest.getClass().getDeclaredFields()){
//            field.setAccessible(true);
//        }
//        ReflectionUtils.setField(
//                addRatePolicyForBookRequest.getClass().getDeclaredField("discountRate")
//                ,addRatePolicyForBookRequest
//                , 10);
//        ReflectionUtils.setField(
//                addRatePolicyForBookRequest.getClass().getDeclaredField("minimumOrderAmount")
//                ,addRatePolicyForBookRequest
//                , 30000);
//        ReflectionUtils.setField(
//                addRatePolicyForBookRequest.getClass().getDeclaredField("maximumDiscountPrice")
//                ,addRatePolicyForBookRequest
//                , 10000);
//        ReflectionUtils.setField(
//                addRatePolicyForBookRequest.getClass().getDeclaredField("expirationPeriodStart")
//                ,addRatePolicyForBookRequest
//                , LocalDateTime.of(2024,1,1,12,0));
//        ReflectionUtils.setField(
//                addRatePolicyForBookRequest.getClass().getDeclaredField("expirationPeriodEnd")
//                ,addRatePolicyForBookRequest
//                , LocalDateTime.of(2024,1,10,12,0));
//        ReflectionUtils.setField(
//                addRatePolicyForBookRequest.getClass().getDeclaredField("name")
//                ,addRatePolicyForBookRequest
//                , "테스트용 정책");
//        ReflectionUtils.setField(
//                addRatePolicyForBookRequest.getClass().getDeclaredField("description")
//                ,addRatePolicyForBookRequest
//                , "테스트용 정책 설명");
//        ReflectionUtils.setField(
//                addRatePolicyForBookRequest.getClass().getDeclaredField("bookId")
//                ,addRatePolicyForBookRequest
//                ,99999L); // 존재햐지 않는 도서 ID
//        ReflectionUtils.setField(
//                addRatePolicyForBookRequest.getClass().getDeclaredField("policyStatusId")
//                ,addRatePolicyForBookRequest
//                ,0);
//
//        Mockito.when(bookRepository.findById(99999L)).thenThrow(BookNotFoundException.class);
//
//        Assertions.assertThrows(BookNotFoundException.class,()->{
//                    policyService.addRatePolicyForBook(addRatePolicyForBookRequest);
//                }
//        );
//    }
//
//    @Test
//    @DisplayName("정률 정책 for Book을 새로 추가 - 해당하는 ID의 정책상태가 없을때 - Exception이 정상적으로 발생하는지")
//    void addRatePolicyForBookWhenPolicyStatusNotFoundExceptionTest() throws NoSuchFieldException {
//
//
//        // 정책 추가 요청
//        AddRatePolicyForBookRequest addRatePolicyForBookRequest = new AddRatePolicyForBookRequest();
//        for(Field field : addRatePolicyForBookRequest.getClass().getDeclaredFields()){
//            field.setAccessible(true);
//        }
//        ReflectionUtils.setField(
//                addRatePolicyForBookRequest.getClass().getDeclaredField("discountRate")
//                ,addRatePolicyForBookRequest
//                , 10);
//        ReflectionUtils.setField(
//                addRatePolicyForBookRequest.getClass().getDeclaredField("minimumOrderAmount")
//                ,addRatePolicyForBookRequest
//                , 30000);
//        ReflectionUtils.setField(
//                addRatePolicyForBookRequest.getClass().getDeclaredField("maximumDiscountPrice")
//                ,addRatePolicyForBookRequest
//                , 10000);
//        ReflectionUtils.setField(
//                addRatePolicyForBookRequest.getClass().getDeclaredField("expirationPeriodStart")
//                ,addRatePolicyForBookRequest
//                , LocalDateTime.of(2024,1,1,12,0));
//        ReflectionUtils.setField(
//                addRatePolicyForBookRequest.getClass().getDeclaredField("expirationPeriodEnd")
//                ,addRatePolicyForBookRequest
//                , LocalDateTime.of(2024,1,10,12,0));
//        ReflectionUtils.setField(
//                addRatePolicyForBookRequest.getClass().getDeclaredField("name")
//                ,addRatePolicyForBookRequest
//                , "테스트용 정책");
//        ReflectionUtils.setField(
//                addRatePolicyForBookRequest.getClass().getDeclaredField("description")
//                ,addRatePolicyForBookRequest
//                , "테스트용 정책 설명");
//        ReflectionUtils.setField(
//                addRatePolicyForBookRequest.getClass().getDeclaredField("bookId")
//                ,addRatePolicyForBookRequest
//                ,1L);
//        ReflectionUtils.setField(
//                addRatePolicyForBookRequest.getClass().getDeclaredField("policyStatusId")
//                ,addRatePolicyForBookRequest
//                ,99999); // 존재하지 않는 정책상태 ID
//
//
//        Mockito.when(bookRepository.findById(addRatePolicyForBookRequest.getBookIsbn13()))
//                .thenReturn(Optional.of(book));
//        Mockito.when(policyStatusRepository.findById(99999)).thenThrow(
//                PolicyStatusNotFoundException.class
//        );
//
//
//        Assertions.assertThrows(PolicyStatusNotFoundException.class,()->{
//                    policyService.addRatePolicyForBook(addRatePolicyForBookRequest);
//                }
//        );
//    }
//
//    @Test
//    @DisplayName("정률 정책 for Category를 새로 추가 - 정상 동작 테스트")
//    void addRatePolicyForCategoryTest() throws NoSuchFieldException {
//
//        // 정책 추가 요청
//        AddRatePolicyForCategoryRequest addRatePolicyForCategoryRequest = new AddRatePolicyForCategoryRequest();
//        for(Field field : addRatePolicyForCategoryRequest.getClass().getDeclaredFields()){
//            field.setAccessible(true);
//        }
//        ReflectionUtils.setField(
//                addRatePolicyForCategoryRequest.getClass().getDeclaredField("discountRate")
//                ,addRatePolicyForCategoryRequest
//                , 10);
//        ReflectionUtils.setField(
//                addRatePolicyForCategoryRequest.getClass().getDeclaredField("minimumOrderAmount")
//                ,addRatePolicyForCategoryRequest
//                , 30000);
//        ReflectionUtils.setField(
//                addRatePolicyForCategoryRequest.getClass().getDeclaredField("maximumDiscountPrice")
//                ,addRatePolicyForCategoryRequest
//                , 10000);
//        ReflectionUtils.setField(
//                addRatePolicyForCategoryRequest.getClass().getDeclaredField("expirationPeriodStart")
//                ,addRatePolicyForCategoryRequest
//                , LocalDateTime.of(2024,1,1,12,0));
//        ReflectionUtils.setField(
//                addRatePolicyForCategoryRequest.getClass().getDeclaredField("expirationPeriodEnd")
//                ,addRatePolicyForCategoryRequest
//                , LocalDateTime.of(2024,1,10,12,0));
//        ReflectionUtils.setField(
//                addRatePolicyForCategoryRequest.getClass().getDeclaredField("name")
//                ,addRatePolicyForCategoryRequest
//                , "테스트용 정책");
//        ReflectionUtils.setField(
//                addRatePolicyForCategoryRequest.getClass().getDeclaredField("description")
//                ,addRatePolicyForCategoryRequest
//                , "테스트용 정책 설명");
//        ReflectionUtils.setField(
//                addRatePolicyForCategoryRequest.getClass().getDeclaredField("categoryId")
//                ,addRatePolicyForCategoryRequest
//                ,0);
//        ReflectionUtils.setField(
//                addRatePolicyForCategoryRequest.getClass().getDeclaredField("policyStatusId")
//                ,addRatePolicyForCategoryRequest
//                ,0);
//
//        Mockito.when(categoryRepository.findById(addRatePolicyForCategoryRequest.getCategoryId()))
//                .thenReturn(Optional.of(category));
//        Mockito.when(policyStatusRepository.findById(addRatePolicyForCategoryRequest.getPolicyStatusId()))
//                .thenReturn(Optional.of(usedStatus));
//
//        // 예싱 응답
//        RatePolicyForCategoryResponse expected =
//                new RatePolicyForCategoryResponse(
//                        null,
//                        10,
//                        30000,
//                        10000,
//                        LocalDateTime.of(2024,1,1,12,0),
//                        LocalDateTime.of(2024,1,10,12,0),
//                        "테스트용 정책",
//                        "테스트용 정책 설명",
//                        "테스트용 카테고리",
//                        "사용됨"
//                );
//
//        // 실제 응답
//        RatePolicyForCategoryResponse actual
//                = policyService.addRatePolicyForCategory(addRatePolicyForCategoryRequest);
//
//        // addRatePolicyForBook() 메서드 안의 ratePoliciesForBookRepository.save()가 실행되었는지 검증
//        Mockito.verify(ratePoliciesForCategoryRepository,Mockito.times(1))
//                .save(Mockito.any(RatePolicyForCategory.class));
//
//        // 예상응답과 실제응답이 같은지 검증
//        Assertions.assertEquals(
//                expected,actual
//        );
//
//    }
//
//    @Test
//    @DisplayName("정률 정책 for Category를 새로 추가 - 해당하는 ID의 카테고리가 없을때 - Exception이 정상적으로 발생하는지")
//    void addRatePolicyForCategoryWhenCategoryNotFoundTest() throws NoSuchFieldException {
//
//        // 정책 추가 요청
//        AddRatePolicyForCategoryRequest addRatePolicyForCategoryRequest = new AddRatePolicyForCategoryRequest();
//        for(Field field : addRatePolicyForCategoryRequest.getClass().getDeclaredFields()){
//            field.setAccessible(true);
//        }
//        ReflectionUtils.setField(
//                addRatePolicyForCategoryRequest.getClass().getDeclaredField("discountRate")
//                ,addRatePolicyForCategoryRequest
//                , 10);
//        ReflectionUtils.setField(
//                addRatePolicyForCategoryRequest.getClass().getDeclaredField("minimumOrderAmount")
//                ,addRatePolicyForCategoryRequest
//                , 30000);
//        ReflectionUtils.setField(
//                addRatePolicyForCategoryRequest.getClass().getDeclaredField("maximumDiscountPrice")
//                ,addRatePolicyForCategoryRequest
//                , 10000);
//        ReflectionUtils.setField(
//                addRatePolicyForCategoryRequest.getClass().getDeclaredField("expirationPeriodStart")
//                ,addRatePolicyForCategoryRequest
//                , LocalDateTime.of(2024,1,1,12,0));
//        ReflectionUtils.setField(
//                addRatePolicyForCategoryRequest.getClass().getDeclaredField("expirationPeriodEnd")
//                ,addRatePolicyForCategoryRequest
//                , LocalDateTime.of(2024,1,10,12,0));
//        ReflectionUtils.setField(
//                addRatePolicyForCategoryRequest.getClass().getDeclaredField("name")
//                ,addRatePolicyForCategoryRequest
//                , "테스트용 정책");
//        ReflectionUtils.setField(
//                addRatePolicyForCategoryRequest.getClass().getDeclaredField("description")
//                ,addRatePolicyForCategoryRequest
//                , "테스트용 정책 설명");
//        ReflectionUtils.setField(
//                addRatePolicyForCategoryRequest.getClass().getDeclaredField("categoryId")
//                ,addRatePolicyForCategoryRequest
//                ,99999); // 존재하지 않는 카테고리 번호
//        ReflectionUtils.setField(
//                addRatePolicyForCategoryRequest.getClass().getDeclaredField("policyStatusId")
//                ,addRatePolicyForCategoryRequest
//                ,0);
//
//        Mockito.when(categoryRepository.findById(99999))
//                .thenThrow(CategoryNotFoundException.class);
//
//        Assertions.assertThrows(CategoryNotFoundException.class,
//                ()->{
//                    policyService.addRatePolicyForCategory(addRatePolicyForCategoryRequest);
//                });
//    }
//
//    @Test
//    @DisplayName("정률 정책 for Category를 새로 추가 - 해당하는 ID의 정책상태가 없을때 - Exception이 정상적으로 발생하는지")
//    void addRatePolicyForCategoryWhenPolicyStatusNotFoundException() throws NoSuchFieldException {
//
//        // 정책 추가 요청
//        AddRatePolicyForCategoryRequest addRatePolicyForCategoryRequest = new AddRatePolicyForCategoryRequest();
//        for(Field field : addRatePolicyForCategoryRequest.getClass().getDeclaredFields()){
//            field.setAccessible(true);
//        }
//        ReflectionUtils.setField(
//                addRatePolicyForCategoryRequest.getClass().getDeclaredField("discountRate")
//                ,addRatePolicyForCategoryRequest
//                , 10);
//        ReflectionUtils.setField(
//                addRatePolicyForCategoryRequest.getClass().getDeclaredField("minimumOrderAmount")
//                ,addRatePolicyForCategoryRequest
//                , 30000);
//        ReflectionUtils.setField(
//                addRatePolicyForCategoryRequest.getClass().getDeclaredField("maximumDiscountPrice")
//                ,addRatePolicyForCategoryRequest
//                , 10000);
//        ReflectionUtils.setField(
//                addRatePolicyForCategoryRequest.getClass().getDeclaredField("expirationPeriodStart")
//                ,addRatePolicyForCategoryRequest
//                , LocalDateTime.of(2024,1,1,12,0));
//        ReflectionUtils.setField(
//                addRatePolicyForCategoryRequest.getClass().getDeclaredField("expirationPeriodEnd")
//                ,addRatePolicyForCategoryRequest
//                , LocalDateTime.of(2024,1,10,12,0));
//        ReflectionUtils.setField(
//                addRatePolicyForCategoryRequest.getClass().getDeclaredField("name")
//                ,addRatePolicyForCategoryRequest
//                , "테스트용 정책");
//        ReflectionUtils.setField(
//                addRatePolicyForCategoryRequest.getClass().getDeclaredField("description")
//                ,addRatePolicyForCategoryRequest
//                , "테스트용 정책 설명");
//        ReflectionUtils.setField(
//                addRatePolicyForCategoryRequest.getClass().getDeclaredField("categoryId")
//                ,addRatePolicyForCategoryRequest
//                ,0);
//        ReflectionUtils.setField(
//                addRatePolicyForCategoryRequest.getClass().getDeclaredField("policyStatusId")
//                ,addRatePolicyForCategoryRequest
//                ,99999); // 존재하지 않는 정책상태 ID
//
//        Mockito.when(categoryRepository.findById(addRatePolicyForCategoryRequest.getCategoryId()))
//                .thenReturn(Optional.of(category));
//        Mockito.when(policyStatusRepository.findById(99999)).thenThrow(PolicyStatusNotFoundException.class);
//
//        Assertions.assertThrows(PolicyStatusNotFoundException.class,
//                ()->{
//                    policyService.addRatePolicyForCategory(addRatePolicyForCategoryRequest);
//                });
//    }
//
//    @Test
//    @DisplayName("정액 정책 for Book을 새로 추가 - 정상 동작 테스트")
//    void addPricePolicyForBookTest() throws NoSuchFieldException {
//
//        // 정책 추가 요청
//        AddPricePolicyForBookRequest addPricePolicyForBookRequest = new AddPricePolicyForBookRequest();
//        for(Field field : addPricePolicyForBookRequest.getClass().getDeclaredFields()){
//            field.setAccessible(true);
//        }
//        ReflectionUtils.setField(
//               addPricePolicyForBookRequest.getClass().getDeclaredField("minimumOrderAmount")
//                ,addPricePolicyForBookRequest
//                , 20000);
//        ReflectionUtils.setField(
//                addPricePolicyForBookRequest.getClass().getDeclaredField("discountPrice")
//                ,addPricePolicyForBookRequest
//                , 5000);
//        ReflectionUtils.setField(
//               addPricePolicyForBookRequest.getClass().getDeclaredField("expirationPeriodStart")
//                ,addPricePolicyForBookRequest
//                , LocalDateTime.of(2024,1,1,12,0));
//        ReflectionUtils.setField(
//               addPricePolicyForBookRequest.getClass().getDeclaredField("expirationPeriodEnd")
//                ,addPricePolicyForBookRequest
//                , LocalDateTime.of(2024,1,10,12,0));
//        ReflectionUtils.setField(
//               addPricePolicyForBookRequest.getClass().getDeclaredField("name")
//                ,addPricePolicyForBookRequest
//                , "테스트용 정액정책 for Book");
//        ReflectionUtils.setField(
//               addPricePolicyForBookRequest.getClass().getDeclaredField("description")
//                ,addPricePolicyForBookRequest
//                , "테스트용 정액정책 for Book 설명");
//        ReflectionUtils.setField(
//               addPricePolicyForBookRequest.getClass().getDeclaredField("bookId")
//                ,addPricePolicyForBookRequest
//                ,1L);
//        ReflectionUtils.setField(
//               addPricePolicyForBookRequest.getClass().getDeclaredField("policyStatusId")
//                ,addPricePolicyForBookRequest
//                ,0);
//
//        Mockito.when(bookRepository.findById(addPricePolicyForBookRequest.getBookId()))
//                .thenReturn(Optional.of(book));
//        Mockito.when(policyStatusRepository.findById(addPricePolicyForBookRequest.getPolicyStatusId()))
//                .thenReturn(Optional.of(usedStatus));
//
//        // 예싱 응답
//        PricePolicyForBookResponse expected =
//                new PricePolicyForBookResponse(
//                        null,
//                        20000,
//                        5000,
//                        LocalDateTime.of(2024,1,1,12,0),
//                        LocalDateTime.of(2024,1,10,12,0),
//                        "테스트용 정액정책 for Book",
//                        "테스트용 정액정책 for Book 설명",
//                        "테스트용 도서",
//                        "테스트용 Isbn13",
//                        "사용됨"
//                );
//
//        // 실제 응답
//        PricePolicyForBookResponse actual
//                = policyService.addPricePolicyForBook(addPricePolicyForBookRequest);
//
//        // addRatePolicyForBook() 메서드 안의 ratePoliciesForBookRepository.save()가 실행되었는지 검증
//        Mockito.verify(pricePoliciesForBookRepository,Mockito.times(1))
//                .save(Mockito.any(PricePolicyForBook.class));
//
//        // 예상응답과 실제응답이 같은지 검증
//        Assertions.assertEquals(
//                expected,actual
//        );
//    }
//
//    @Test
//    @DisplayName("정액 정책 for Book을 새로 추가 - 해당하는 ID의 도서가 없을때 - Exception이 정상적으로 발생하는지")
//    void addPricePolicyForBookWhenBookNotFoundTest() throws NoSuchFieldException {
//
//        // 정책 추가 요청
//        AddPricePolicyForBookRequest addPricePolicyForBookRequest = new AddPricePolicyForBookRequest();
//        for(Field field : addPricePolicyForBookRequest.getClass().getDeclaredFields()){
//            field.setAccessible(true);
//        }
//
//        ReflectionUtils.setField(
//                addPricePolicyForBookRequest.getClass().getDeclaredField("minimumOrderAmount")
//                ,addPricePolicyForBookRequest
//                , 20000);
//        ReflectionUtils.setField(
//                addPricePolicyForBookRequest.getClass().getDeclaredField("discountPrice")
//                ,addPricePolicyForBookRequest
//                , 5000);
//        ReflectionUtils.setField(
//                addPricePolicyForBookRequest.getClass().getDeclaredField("expirationPeriodStart")
//                ,addPricePolicyForBookRequest
//                , LocalDateTime.of(2024,1,1,12,0));
//        ReflectionUtils.setField(
//                addPricePolicyForBookRequest.getClass().getDeclaredField("expirationPeriodEnd")
//                ,addPricePolicyForBookRequest
//                , LocalDateTime.of(2024,1,10,12,0));
//        ReflectionUtils.setField(
//                addPricePolicyForBookRequest.getClass().getDeclaredField("name")
//                ,addPricePolicyForBookRequest
//                , "테스트용 정액정책 for Book");
//        ReflectionUtils.setField(
//                addPricePolicyForBookRequest.getClass().getDeclaredField("description")
//                ,addPricePolicyForBookRequest
//                , "테스트용 정액정책 for Book 설명");
//        ReflectionUtils.setField(
//                addPricePolicyForBookRequest.getClass().getDeclaredField("bookId")
//                ,addPricePolicyForBookRequest
//                ,99999L); // 존재햐지 않는 도서 ID
//        ReflectionUtils.setField(
//                addPricePolicyForBookRequest.getClass().getDeclaredField("policyStatusId")
//                ,addPricePolicyForBookRequest
//                ,0);
//
//        Mockito.when(bookRepository.findById(99999L))
//                .thenThrow(BookNotFoundException.class);
//
//        Assertions.assertThrows(BookNotFoundException.class,()->{
//            policyService.addPricePolicyForBook(addPricePolicyForBookRequest);
//        });
//    }
//
//    @Test
//    @DisplayName("정액 정책 for Book을 새로 추가 - 해당하는 ID의 정책상태가 없을때 - Exception이 정상적으로 발생하는지")
//    void addPricePolicyForBookWhenPolicyStatusNotFoundTest() throws NoSuchFieldException {
//
//        // 정책 추가 요청
//        AddPricePolicyForBookRequest addPricePolicyForBookRequest = new AddPricePolicyForBookRequest();
//        for(Field field : addPricePolicyForBookRequest.getClass().getDeclaredFields()){
//            field.setAccessible(true);
//        }
//
//        ReflectionUtils.setField(
//                addPricePolicyForBookRequest.getClass().getDeclaredField("minimumOrderAmount")
//                ,addPricePolicyForBookRequest
//                , 20000);
//        ReflectionUtils.setField(
//                addPricePolicyForBookRequest.getClass().getDeclaredField("discountPrice")
//                ,addPricePolicyForBookRequest
//                , 5000);
//        ReflectionUtils.setField(
//                addPricePolicyForBookRequest.getClass().getDeclaredField("expirationPeriodStart")
//                ,addPricePolicyForBookRequest
//                , LocalDateTime.of(2024,1,1,12,0));
//        ReflectionUtils.setField(
//                addPricePolicyForBookRequest.getClass().getDeclaredField("expirationPeriodEnd")
//                ,addPricePolicyForBookRequest
//                , LocalDateTime.of(2024,1,10,12,0));
//        ReflectionUtils.setField(
//                addPricePolicyForBookRequest.getClass().getDeclaredField("name")
//                ,addPricePolicyForBookRequest
//                , "테스트용 정액정책 for Book");
//        ReflectionUtils.setField(
//                addPricePolicyForBookRequest.getClass().getDeclaredField("description")
//                ,addPricePolicyForBookRequest
//                , "테스트용 정액정책 for Book 설명");
//        ReflectionUtils.setField(
//                addPricePolicyForBookRequest.getClass().getDeclaredField("bookId")
//                ,addPricePolicyForBookRequest
//                ,1L);
//        ReflectionUtils.setField(
//                addPricePolicyForBookRequest.getClass().getDeclaredField("policyStatusId")
//                ,addPricePolicyForBookRequest
//                ,99999); // 존재하지 않는 정책상태 ID
//
//        Mockito.when(bookRepository.findById(addPricePolicyForBookRequest.getBookId()))
//                .thenReturn(Optional.of(book));
//        Mockito.when(policyStatusRepository.findById(99999))
//                .thenThrow(PolicyStatusNotFoundException.class);
//
//        Assertions.assertThrows(PolicyStatusNotFoundException.class,()->{
//            policyService.addPricePolicyForBook(addPricePolicyForBookRequest);
//        });
//    }
//
//    @Test
//    @DisplayName("정액 정책 for Category를 새로 추가 - 정상 동작 테스트")
//    void addPricePolicyForCategoryTest() throws NoSuchFieldException {
//
//        // 정책 추가 요청
//        AddPricePolicyForCategoryRequest addPricePolicyForCategoryRequest = new AddPricePolicyForCategoryRequest();
//        for(Field field : addPricePolicyForCategoryRequest.getClass().getDeclaredFields()){
//            field.setAccessible(true);
//        }
//
//        ReflectionUtils.setField(
//                addPricePolicyForCategoryRequest.getClass().getDeclaredField("minimumOrderAmount")
//                ,addPricePolicyForCategoryRequest
//                , 20000);
//        ReflectionUtils.setField(
//                addPricePolicyForCategoryRequest.getClass().getDeclaredField("discountPrice")
//                ,addPricePolicyForCategoryRequest
//                , 5000);
//        ReflectionUtils.setField(
//                addPricePolicyForCategoryRequest.getClass().getDeclaredField("expirationPeriodStart")
//                ,addPricePolicyForCategoryRequest
//                , LocalDateTime.of(2024,1,1,12,0));
//        ReflectionUtils.setField(
//                addPricePolicyForCategoryRequest.getClass().getDeclaredField("expirationPeriodEnd")
//                ,addPricePolicyForCategoryRequest
//                , LocalDateTime.of(2024,1,10,12,0));
//        ReflectionUtils.setField(
//                addPricePolicyForCategoryRequest.getClass().getDeclaredField("name")
//                ,addPricePolicyForCategoryRequest
//                , "테스트용 정액정책 for Category");
//        ReflectionUtils.setField(
//                addPricePolicyForCategoryRequest.getClass().getDeclaredField("description")
//                ,addPricePolicyForCategoryRequest
//                , "테스트용 정액정책 for Category 설명");
//        ReflectionUtils.setField(
//                addPricePolicyForCategoryRequest.getClass().getDeclaredField("categoryId")
//                ,addPricePolicyForCategoryRequest
//                ,0);
//        ReflectionUtils.setField(
//                addPricePolicyForCategoryRequest.getClass().getDeclaredField("policyStatusId")
//                ,addPricePolicyForCategoryRequest
//                ,0);
//
//        Mockito.when(categoryRepository.findById(addPricePolicyForCategoryRequest.getCategoryId()))
//                .thenReturn(Optional.of(category));
//        Mockito.when(policyStatusRepository.findById(addPricePolicyForCategoryRequest.getPolicyStatusId()))
//                .thenReturn(Optional.of(usedStatus));
//
//        // 예싱 응답
//        PricePolicyForCategoryResponse expected =
//                new PricePolicyForCategoryResponse(
//                        null,
//                        20000,
//                        5000,
//                        LocalDateTime.of(2024,1,1,12,0),
//                        LocalDateTime.of(2024,1,10,12,0),
//                        "테스트용 정액정책 for Category",
//                        "테스트용 정액정책 for Category 설명",
//                        "테스트용 카테고리",
//                        "사용됨"
//                );
//
//        // 실제 응답
//        PricePolicyForCategoryResponse actual
//                = policyService.addPricePolicyForCategory(addPricePolicyForCategoryRequest);
//
//        // addRatePolicyForBook() 메서드 안의 ratePoliciesForBookRepository.save()가 실행되었는지 검증
//        Mockito.verify(pricePoliciesForCategoryRepository,Mockito.times(1))
//                .save(Mockito.any(PricePolicyForCategory.class));
//
//        // 예상응답과 실제응답이 같은지 검증
//        Assertions.assertEquals(
//                expected,actual
//        );
//    }
//
//    @Test
//    @DisplayName("정액 정책 for Category를 새로 추가 - 해당하는 ID의 카테고리가 없을때 - Exception이 정상적으로 발생하는지")
//    void addPricePolicyForCategoryWhenCategoryNotFoundTest() throws NoSuchFieldException {
//
//        // 정책 추가 요청
//        AddPricePolicyForCategoryRequest addPricePolicyForCategoryRequest = new AddPricePolicyForCategoryRequest();
//        for(Field field : addPricePolicyForCategoryRequest.getClass().getDeclaredFields()){
//            field.setAccessible(true);
//        }
//        ReflectionUtils.setField(
//                addPricePolicyForCategoryRequest.getClass().getDeclaredField("minimumOrderAmount")
//                ,addPricePolicyForCategoryRequest
//                , 20000);
//        ReflectionUtils.setField(
//                addPricePolicyForCategoryRequest.getClass().getDeclaredField("discountPrice")
//                ,addPricePolicyForCategoryRequest
//                , 5000);
//        ReflectionUtils.setField(
//                addPricePolicyForCategoryRequest.getClass().getDeclaredField("expirationPeriodStart")
//                ,addPricePolicyForCategoryRequest
//                , LocalDateTime.of(2024,1,1,12,0));
//        ReflectionUtils.setField(
//                addPricePolicyForCategoryRequest.getClass().getDeclaredField("expirationPeriodEnd")
//                ,addPricePolicyForCategoryRequest
//                , LocalDateTime.of(2024,1,10,12,0));
//        ReflectionUtils.setField(
//                addPricePolicyForCategoryRequest.getClass().getDeclaredField("name")
//                ,addPricePolicyForCategoryRequest
//                , "테스트용 정액정책 for Book");
//        ReflectionUtils.setField(
//                addPricePolicyForCategoryRequest.getClass().getDeclaredField("description")
//                ,addPricePolicyForCategoryRequest
//                , "테스트용 정액정책 for Book 설명");
//        ReflectionUtils.setField(
//                addPricePolicyForCategoryRequest.getClass().getDeclaredField("categoryId")
//                ,addPricePolicyForCategoryRequest
//                ,99999); // 존재하지 않는 카테고리 번호
//        ReflectionUtils.setField(
//                addPricePolicyForCategoryRequest.getClass().getDeclaredField("policyStatusId")
//                ,addPricePolicyForCategoryRequest
//                ,0);
//
//        Mockito.when(categoryRepository.findById(99999))
//                .thenThrow(CategoryNotFoundException.class);
//
//        Assertions.assertThrows(CategoryNotFoundException.class, ()->{
//           policyService.addPricePolicyForCategory(addPricePolicyForCategoryRequest);
//        });
//    }
//
//    @Test
//    @DisplayName("정액 정책 for Category를 새로 추가 - 해당하는 ID의 정책상태가 없을때 - Exception이 정상적으로 발생하는지")
//    void addPricePolicyForCategoryWhenPolicyStatusNotFound() throws NoSuchFieldException {
//
//        // 정책 추가 요청
//        AddPricePolicyForCategoryRequest addPricePolicyForCategoryRequest = new AddPricePolicyForCategoryRequest();
//        for(Field field : addPricePolicyForCategoryRequest.getClass().getDeclaredFields()){
//            field.setAccessible(true);
//        }
//        ReflectionUtils.setField(
//                addPricePolicyForCategoryRequest.getClass().getDeclaredField("minimumOrderAmount")
//                ,addPricePolicyForCategoryRequest
//                , 20000);
//        ReflectionUtils.setField(
//                addPricePolicyForCategoryRequest.getClass().getDeclaredField("discountPrice")
//                ,addPricePolicyForCategoryRequest
//                , 5000);
//        ReflectionUtils.setField(
//                addPricePolicyForCategoryRequest.getClass().getDeclaredField("expirationPeriodStart")
//                ,addPricePolicyForCategoryRequest
//                , LocalDateTime.of(2024,1,1,12,0));
//        ReflectionUtils.setField(
//                addPricePolicyForCategoryRequest.getClass().getDeclaredField("expirationPeriodEnd")
//                ,addPricePolicyForCategoryRequest
//                , LocalDateTime.of(2024,1,10,12,0));
//        ReflectionUtils.setField(
//                addPricePolicyForCategoryRequest.getClass().getDeclaredField("name")
//                ,addPricePolicyForCategoryRequest
//                , "테스트용 정액정책 for Book");
//        ReflectionUtils.setField(
//                addPricePolicyForCategoryRequest.getClass().getDeclaredField("description")
//                ,addPricePolicyForCategoryRequest
//                , "테스트용 정액정책 for Book 설명");
//        ReflectionUtils.setField(
//                addPricePolicyForCategoryRequest.getClass().getDeclaredField("categoryId")
//                ,addPricePolicyForCategoryRequest
//                ,0);
//        ReflectionUtils.setField(
//                addPricePolicyForCategoryRequest.getClass().getDeclaredField("policyStatusId")
//                ,addPricePolicyForCategoryRequest
//                ,99999); // 존재하지 않는 정책상태 ID
//
//        Mockito.when(categoryRepository.findById(addPricePolicyForCategoryRequest.getCategoryId()))
//                .thenReturn(Optional.of(category));
//        Mockito.when(policyStatusRepository.findById(99999))
//                .thenThrow(PolicyStatusNotFoundException.class);
//
//        Assertions.assertThrows(PolicyStatusNotFoundException.class, ()->{
//            policyService.addPricePolicyForCategory(addPricePolicyForCategoryRequest);
//        });
//    }
//
//    @Test
//    @DisplayName("정률정책 for Book 전체조회 - pageable 사용")
//    void getRatePoliciesForBookTest() throws NoSuchFieldException {
//
//        List<RatePolicyForBook> list = new ArrayList<>();
//
//        for(int i = 1 ; i <=10;i++){
//
//            RatePolicyForBook ratePolicyForBook = new RatePolicyForBook();
//            for(Field field : ratePolicyForBook.getClass().getDeclaredFields()){
//                field.setAccessible(true);
//                ReflectionUtils.setField(
//                        ratePolicyForBook.getClass().getDeclaredField("ratePolicyForBookId"),
//                        ratePolicyForBook,
//                        (long)i);
//                ReflectionUtils.setField(
//                        ratePolicyForBook.getClass().getDeclaredField("discountRate"),
//                        ratePolicyForBook,
//                         i);
//                ReflectionUtils.setField(
//                        ratePolicyForBook.getClass().getDeclaredField("minimumOrderAmount"),
//                        ratePolicyForBook,
//                        i);
//                ReflectionUtils.setField(
//                        ratePolicyForBook.getClass().getDeclaredField("maximumDiscountPrice"),
//                        ratePolicyForBook,
//                        i);
//                ReflectionUtils.setField(
//                        ratePolicyForBook.getClass().getDeclaredField("expirationPeriodStart"),
//                        ratePolicyForBook,
//                        LocalDateTime.of(2024,1,i,12,10));
//                ReflectionUtils.setField(
//                        ratePolicyForBook.getClass().getDeclaredField("expirationPeriodEnd"),
//                        ratePolicyForBook,
//                        LocalDateTime.of(2024,1,i,12,10));
//                ReflectionUtils.setField(
//                        ratePolicyForBook.getClass().getDeclaredField("name"),
//                        ratePolicyForBook,
//                        Integer.toString(i));
//                ReflectionUtils.setField(
//                        ratePolicyForBook.getClass().getDeclaredField("description"),
//                        ratePolicyForBook,
//                        Integer.toString(i));
//                ReflectionUtils.setField(
//                        ratePolicyForBook.getClass().getDeclaredField("book"),
//                        ratePolicyForBook,
//                        book);
//                ReflectionUtils.setField(
//                        ratePolicyForBook.getClass().getDeclaredField("policyStatus"),
//                        ratePolicyForBook,
//                        usedStatus);
//
//                list.add(ratePolicyForBook);
//            }
//
//        }
//
//        Page<RatePolicyForBook> page = new PageImpl<>(list);
//        Mockito.when(ratePoliciesForBookRepository.findAll(Mockito.any(Pageable.class)))
//                .thenReturn(page);
//        List<RatePolicyForBookResponse> expected = new ArrayList<>();
//        for(RatePolicyForBook ratePolicyForBook : page){
//            expected.add(RatePolicyForBookResponse.changeEntityToDto(ratePolicyForBook));
//        }
//
//        List<RatePolicyForBookResponse> actual  = policyService.getRatePoliciesForBook(1);
//
//        Mockito.verify(ratePoliciesForBookRepository,
//                Mockito.times(1))
//                .findAll(Mockito.any(Pageable.class));
//
//        Assertions.assertEquals(expected,actual);
//
//    }
//
//    @Test
//    @DisplayName("정률정책 for Category 전체조회 - pageable 사용")
//    void getRatePoliciesForCategoryTest() throws NoSuchFieldException {
//
//        List<RatePolicyForCategory> list = new ArrayList<>();
//
//        for(int i = 1 ; i <=10;i++){
//
//           RatePolicyForCategory ratePolicyForCategory = new RatePolicyForCategory();
//            for(Field field :ratePolicyForCategory.getClass().getDeclaredFields()){
//                field.setAccessible(true);
//                ReflectionUtils.setField(
//                       ratePolicyForCategory.getClass().getDeclaredField("ratePolicyForCategoryId"),
//                       ratePolicyForCategory,
//                        (long)i);
//                ReflectionUtils.setField(
//                       ratePolicyForCategory.getClass().getDeclaredField("discountRate"),
//                       ratePolicyForCategory,
//                        i);
//                ReflectionUtils.setField(
//                       ratePolicyForCategory.getClass().getDeclaredField("minimumOrderAmount"),
//                       ratePolicyForCategory,
//                        i);
//                ReflectionUtils.setField(
//                       ratePolicyForCategory.getClass().getDeclaredField("maximumDiscountPrice"),
//                       ratePolicyForCategory,
//                        i);
//                ReflectionUtils.setField(
//                       ratePolicyForCategory.getClass().getDeclaredField("expirationPeriodStart"),
//                       ratePolicyForCategory,
//                        LocalDateTime.of(2024,1,i,12,10));
//                ReflectionUtils.setField(
//                       ratePolicyForCategory.getClass().getDeclaredField("expirationPeriodEnd"),
//                       ratePolicyForCategory,
//                        LocalDateTime.of(2024,1,i,12,10));
//                ReflectionUtils.setField(
//                       ratePolicyForCategory.getClass().getDeclaredField("name"),
//                       ratePolicyForCategory,
//                        Integer.toString(i));
//                ReflectionUtils.setField(
//                       ratePolicyForCategory.getClass().getDeclaredField("description"),
//                       ratePolicyForCategory,
//                        Integer.toString(i));
//                ReflectionUtils.setField(
//                       ratePolicyForCategory.getClass().getDeclaredField("category"),
//                       ratePolicyForCategory,
//                        category);
//                ReflectionUtils.setField(
//                       ratePolicyForCategory.getClass().getDeclaredField("policyStatus"),
//                       ratePolicyForCategory,
//                        usedStatus);
//
//                list.add(ratePolicyForCategory);
//            }
//
//        }
//
//        Page<RatePolicyForCategory> page = new PageImpl<>(list);
//        Mockito.when(ratePoliciesForCategoryRepository.findAll(Mockito.any(Pageable.class)))
//                .thenReturn(page);
//        List<RatePolicyForCategoryResponse> expected = new ArrayList<>();
//        for(RatePolicyForCategory ratePolicyForCategory : page){
//            expected.add(RatePolicyForCategoryResponse.changeEntityToDto(ratePolicyForCategory));
//        }
//
//        List<RatePolicyForCategoryResponse> actual  = policyService.getRatePoliciesForCategory(1);
//
//        Mockito.verify(ratePoliciesForCategoryRepository,
//                        Mockito.times(1))
//                .findAll(Mockito.any(Pageable.class));
//
//        Assertions.assertEquals(expected,actual);
//    }
//
//    @Test
//    @DisplayName("정액정책 for Book 전체조회 - pageable 사용")
//    void getPricePoliciesForBookTest() throws NoSuchFieldException {
//
//        List<PricePolicyForBook> list = new ArrayList<>();
//
//        for(int i = 1 ; i <=10;i++){
//
//            PricePolicyForBook pricePolicyForBook = new PricePolicyForBook();
//            for(Field field : pricePolicyForBook.getClass().getDeclaredFields()){
//                field.setAccessible(true);
//                ReflectionUtils.setField(
//                        pricePolicyForBook.getClass().getDeclaredField("pricePolicyForBookId"),
//                        pricePolicyForBook,
//                        (long)i);
//                ReflectionUtils.setField(
//                        pricePolicyForBook.getClass().getDeclaredField("discountPrice"),
//                        pricePolicyForBook,
//                        i);
//                ReflectionUtils.setField(
//                        pricePolicyForBook.getClass().getDeclaredField("minimumOrderAmount"),
//                        pricePolicyForBook,
//                        i);
//                ReflectionUtils.setField(
//                        pricePolicyForBook.getClass().getDeclaredField("expirationPeriodStart"),
//                        pricePolicyForBook,
//                        LocalDateTime.of(2024,1,i,12,10));
//                ReflectionUtils.setField(
//                        pricePolicyForBook.getClass().getDeclaredField("expirationPeriodEnd"),
//                        pricePolicyForBook,
//                        LocalDateTime.of(2024,1,i,12,10));
//                ReflectionUtils.setField(
//                        pricePolicyForBook.getClass().getDeclaredField("name"),
//                        pricePolicyForBook,
//                        Integer.toString(i));
//                ReflectionUtils.setField(
//                        pricePolicyForBook.getClass().getDeclaredField("description"),
//                        pricePolicyForBook,
//                        Integer.toString(i));
//                ReflectionUtils.setField(
//                        pricePolicyForBook.getClass().getDeclaredField("book"),
//                        pricePolicyForBook,
//                        book);
//                ReflectionUtils.setField(
//                        pricePolicyForBook.getClass().getDeclaredField("policyStatus"),
//                        pricePolicyForBook,
//                        usedStatus);
//
//                list.add(pricePolicyForBook);
//            }
//
//        }
//
//        Page<PricePolicyForBook> page = new PageImpl<>(list);
//        Mockito.when(pricePoliciesForBookRepository.findAll(Mockito.any(Pageable.class)))
//                .thenReturn(page);
//        List<PricePolicyForBookResponse> expected = new ArrayList<>();
//        for(PricePolicyForBook pricePolicyForBook : page){
//            expected.add(PricePolicyForBookResponse.changeEntityToDto(pricePolicyForBook));
//        }
//
//        List<PricePolicyForBookResponse> actual  = policyService.getPricePoliciesForBook(1);
//
//        Mockito.verify(pricePoliciesForBookRepository,
//                        Mockito.times(1))
//                .findAll(Mockito.any(Pageable.class));
//
//        Assertions.assertEquals(expected,actual);
//    }
//
//    @Test
//    @DisplayName("정액정책 for Category 전체조회 - pageable 사용")
//    void getPricePoliciesForCategoryTest() throws NoSuchFieldException {
//
//        List<PricePolicyForCategory> list = new ArrayList<>();
//
//        for(int i = 1 ; i <=10;i++){
//
//            PricePolicyForCategory pricePolicyForCategory = new PricePolicyForCategory();
//            for(Field field : pricePolicyForCategory.getClass().getDeclaredFields()){
//                field.setAccessible(true);
//                ReflectionUtils.setField(
//                        pricePolicyForCategory.getClass().getDeclaredField("pricePolicyForCategoryId"),
//                        pricePolicyForCategory,
//                        (long)i);
//                ReflectionUtils.setField(
//                        pricePolicyForCategory.getClass().getDeclaredField("discountPrice"),
//                        pricePolicyForCategory,
//                        i);
//                ReflectionUtils.setField(
//                        pricePolicyForCategory.getClass().getDeclaredField("minimumOrderAmount"),
//                        pricePolicyForCategory,
//                        i);
//                ReflectionUtils.setField(
//                        pricePolicyForCategory.getClass().getDeclaredField("expirationPeriodStart"),
//                        pricePolicyForCategory,
//                        LocalDateTime.of(2024,1,i,12,10));
//                ReflectionUtils.setField(
//                        pricePolicyForCategory.getClass().getDeclaredField("expirationPeriodEnd"),
//                        pricePolicyForCategory,
//                        LocalDateTime.of(2024,1,i,12,10));
//                ReflectionUtils.setField(
//                        pricePolicyForCategory.getClass().getDeclaredField("name"),
//                        pricePolicyForCategory,
//                        Integer.toString(i));
//                ReflectionUtils.setField(
//                        pricePolicyForCategory.getClass().getDeclaredField("description"),
//                        pricePolicyForCategory,
//                        Integer.toString(i));
//                ReflectionUtils.setField(
//                        pricePolicyForCategory.getClass().getDeclaredField("category"),
//                        pricePolicyForCategory,
//                        category);
//                ReflectionUtils.setField(
//                        pricePolicyForCategory.getClass().getDeclaredField("policyStatus"),
//                        pricePolicyForCategory,
//                        usedStatus);
//
//                list.add(pricePolicyForCategory);
//            }
//
//        }
//
//        Page<PricePolicyForCategory> page = new PageImpl<>(list);
//        Mockito.when(pricePoliciesForCategoryRepository.findAll(Mockito.any(Pageable.class)))
//                .thenReturn(page);
//        List<PricePolicyForCategoryResponse> expected = new ArrayList<>();
//        for(PricePolicyForCategory pricePolicyForCategory : page){
//            expected.add(PricePolicyForCategoryResponse.changeEntityToDto(pricePolicyForCategory));
//        }
//
//        List<PricePolicyForCategoryResponse> actual  = policyService.getPricePoliciesForCategory(1);
//
//        Mockito.verify(pricePoliciesForCategoryRepository,
//                        Mockito.times(1))
//                .findAll(Mockito.any(Pageable.class));
//
//        Assertions.assertEquals(expected,actual);
//    }
//
//    @Test
//    @DisplayName("정률정책 for Book 개별조회 - 정상동작")
//    void getRatePolicyForBookTest() throws NoSuchFieldException {
//
//        RatePolicyForBook ratePolicyForBook = new RatePolicyForBook();
//        for(Field field : ratePolicyForBook.getClass().getDeclaredFields()){
//            field.setAccessible(true);
//        }
//        ReflectionUtils.setField(
//                ratePolicyForBook.getClass().getDeclaredField("ratePolicyForBookId"),
//                ratePolicyForBook,
//                0L);
//        ReflectionUtils.setField(
//                ratePolicyForBook.getClass().getDeclaredField("discountRate"),
//                ratePolicyForBook,
//                0);
//        ReflectionUtils.setField(
//                ratePolicyForBook.getClass().getDeclaredField("minimumOrderAmount"),
//                ratePolicyForBook,
//                10000);
//        ReflectionUtils.setField(
//                ratePolicyForBook.getClass().getDeclaredField("maximumDiscountPrice"),
//                ratePolicyForBook,
//                10);
//        ReflectionUtils.setField(
//                ratePolicyForBook.getClass().getDeclaredField("expirationPeriodStart"),
//                ratePolicyForBook,
//                LocalDateTime.of(2024,1,1,1,1));
//        ReflectionUtils.setField(
//                ratePolicyForBook.getClass().getDeclaredField("expirationPeriodEnd"),
//                ratePolicyForBook,
//                LocalDateTime.of(2024,1,10,1,1));
//        ReflectionUtils.setField(
//                ratePolicyForBook.getClass().getDeclaredField("name"),
//                ratePolicyForBook,
//                "테스트용 정률정책 for Book");
//        ReflectionUtils.setField(
//                ratePolicyForBook.getClass().getDeclaredField("description"),
//                ratePolicyForBook,
//                "테스트용 정률정책 for Book");
//        ReflectionUtils.setField(
//                ratePolicyForBook.getClass().getDeclaredField("book"),
//                ratePolicyForBook,
//                book);
//        ReflectionUtils.setField(
//                ratePolicyForBook.getClass().getDeclaredField("policyStatus"),
//                ratePolicyForBook,
//                usedStatus);
//
//        Mockito.when(ratePoliciesForBookRepository.findById(0L)).thenReturn(Optional.of(ratePolicyForBook));
//        RatePolicyForBookResponse expected = RatePolicyForBookResponse.changeEntityToDto(ratePolicyForBook);
//        RatePolicyForBookResponse actual = policyService.getRatePolicyForBook(0L);
//
//        Mockito.verify(ratePoliciesForBookRepository, Mockito.times(1) ).findById(0L);
//        Assertions.assertEquals(expected,actual);
//    }
//
//    @Test
//    @DisplayName("정률정책 for Book 개별조회 - 정책 ID가 존재하지 않을때")
//    void getRatePolicyForBookWhenPolicyNotFoundTest() throws NoSuchFieldException {
//
//        Mockito.when(ratePoliciesForBookRepository.findById(0L)).thenThrow(PolicyNotFoundException.class);
//
//        Assertions.assertThrows(PolicyNotFoundException.class, ()->{
//            policyService.getRatePolicyForBook(0L);
//        });
//
//        Mockito.verify(ratePoliciesForBookRepository, Mockito.times(1) ).findById(0L);
//    }
//
//    @Test
//    @DisplayName("정률정책 for Category 개별조회 - 정상동작")
//    void getRatePolicyForCategoryTest() throws NoSuchFieldException {
//
//        RatePolicyForCategory ratePolicyForCategory = new RatePolicyForCategory();
//        for(Field field : ratePolicyForCategory.getClass().getDeclaredFields()){
//            field.setAccessible(true);
//        }
//        ReflectionUtils.setField(
//                ratePolicyForCategory.getClass().getDeclaredField("ratePolicyForCategoryId"),
//                ratePolicyForCategory,
//                0L);
//        ReflectionUtils.setField(
//                ratePolicyForCategory.getClass().getDeclaredField("discountRate"),
//                ratePolicyForCategory,
//                0);
//        ReflectionUtils.setField(
//                ratePolicyForCategory.getClass().getDeclaredField("minimumOrderAmount"),
//                ratePolicyForCategory,
//                10000);
//        ReflectionUtils.setField(
//                ratePolicyForCategory.getClass().getDeclaredField("maximumDiscountPrice"),
//                ratePolicyForCategory,
//                10);
//        ReflectionUtils.setField(
//                ratePolicyForCategory.getClass().getDeclaredField("expirationPeriodStart"),
//                ratePolicyForCategory,
//                LocalDateTime.of(2024,1,1,1,1));
//        ReflectionUtils.setField(
//                ratePolicyForCategory.getClass().getDeclaredField("expirationPeriodEnd"),
//                ratePolicyForCategory,
//                LocalDateTime.of(2024,1,10,1,1));
//        ReflectionUtils.setField(
//                ratePolicyForCategory.getClass().getDeclaredField("name"),
//                ratePolicyForCategory,
//                "테스트용 정률정책 for Book");
//        ReflectionUtils.setField(
//                ratePolicyForCategory.getClass().getDeclaredField("description"),
//                ratePolicyForCategory,
//                "테스트용 정률정책 for Book");
//        ReflectionUtils.setField(
//                ratePolicyForCategory.getClass().getDeclaredField("category"),
//                ratePolicyForCategory,
//                category);
//        ReflectionUtils.setField(
//                ratePolicyForCategory.getClass().getDeclaredField("policyStatus"),
//                ratePolicyForCategory,
//                usedStatus);
//
//        Mockito.when(ratePoliciesForCategoryRepository.findById(0L)).thenReturn(Optional.of(ratePolicyForCategory));
//        RatePolicyForCategoryResponse expected = RatePolicyForCategoryResponse.changeEntityToDto(ratePolicyForCategory);
//        RatePolicyForCategoryResponse actual = policyService.getRatePolicyForCategory(0L);
//
//        Mockito.verify(ratePoliciesForCategoryRepository, Mockito.times(1) ).findById(0L);
//        Assertions.assertEquals(expected,actual);
//    }
//
//    @Test
//    @DisplayName("정률정책 for Category 개별조회 - 정책 ID가 존재하지 않을때")
//    void getRatePolicyForCategoryWhenPolicyNotFoundTest() throws NoSuchFieldException {
//
//        Mockito.when(ratePoliciesForCategoryRepository.findById(0L)).thenThrow(PolicyNotFoundException.class);
//
//        Assertions.assertThrows(PolicyNotFoundException.class, ()->{
//            policyService.getRatePolicyForCategory(0L);
//        });
//
//        Mockito.verify(ratePoliciesForCategoryRepository, Mockito.times(1) ).findById(0L);
//    }
//
//    @Test
//    @DisplayName("정액정책 for Book 개별조회 - 정상동작")
//    void getPricePolicyForBookTest() throws NoSuchFieldException {
//
//        PricePolicyForBook pricePolicyForBook = new PricePolicyForBook();
//        for(Field field : pricePolicyForBook.getClass().getDeclaredFields()){
//            field.setAccessible(true);
//        }
//        ReflectionUtils.setField(
//                pricePolicyForBook.getClass().getDeclaredField("pricePolicyForBookId"),
//                pricePolicyForBook,
//                0L);
//        ReflectionUtils.setField(
//                pricePolicyForBook.getClass().getDeclaredField("minimumOrderAmount"),
//                pricePolicyForBook,
//                10000);
//        ReflectionUtils.setField(
//                pricePolicyForBook.getClass().getDeclaredField("discountPrice"),
//                pricePolicyForBook,
//                1000);
//        ReflectionUtils.setField(
//                pricePolicyForBook.getClass().getDeclaredField("expirationPeriodStart"),
//                pricePolicyForBook,
//                LocalDateTime.of(2024,1,1,1,1));
//        ReflectionUtils.setField(
//                pricePolicyForBook.getClass().getDeclaredField("expirationPeriodEnd"),
//                pricePolicyForBook,
//                LocalDateTime.of(2024,1,10,1,1));
//        ReflectionUtils.setField(
//                pricePolicyForBook.getClass().getDeclaredField("name"),
//                pricePolicyForBook,
//                "테스트용 정률정책 for Book");
//        ReflectionUtils.setField(
//                pricePolicyForBook.getClass().getDeclaredField("description"),
//                pricePolicyForBook,
//                "테스트용 정률정책 for Book");
//        ReflectionUtils.setField(
//                pricePolicyForBook.getClass().getDeclaredField("book"),
//                pricePolicyForBook,
//                book);
//        ReflectionUtils.setField(
//                pricePolicyForBook.getClass().getDeclaredField("policyStatus"),
//                pricePolicyForBook,
//                usedStatus);
//
//        Mockito.when(pricePoliciesForBookRepository.findById(0L)).thenReturn(Optional.of(pricePolicyForBook));
//        PricePolicyForBookResponse expected = PricePolicyForBookResponse.changeEntityToDto(pricePolicyForBook);
//        PricePolicyForBookResponse actual = policyService.getPricePolicyForBook(0L);
//
//        Mockito.verify(pricePoliciesForBookRepository, Mockito.times(1) ).findById(0L);
//        Assertions.assertEquals(expected,actual);
//    }
//
//    @Test
//    @DisplayName("정액정책 for Book 개별조회 - 정책 ID가 존재하지 않을때")
//    void getPricePolicyForBookWhenPolicyNotFoundTest() throws NoSuchFieldException {
//
//        Mockito.when(pricePoliciesForBookRepository.findById(0L)).thenThrow(PolicyNotFoundException.class);
//
//        Assertions.assertThrows(PolicyNotFoundException.class, ()->{
//            policyService.getPricePolicyForBook(0L);
//        });
//
//        Mockito.verify(pricePoliciesForBookRepository, Mockito.times(1) ).findById(0L);
//    }
//
//    @Test
//    @DisplayName("정액정책 for Category 개별조회 - 정상동작")
//    void getPricePolicyForCategoryTest() throws NoSuchFieldException {
//
//        PricePolicyForCategory pricePolicyForCategory = new PricePolicyForCategory();
//        for(Field field : pricePolicyForCategory.getClass().getDeclaredFields()){
//            field.setAccessible(true);
//        }
//        ReflectionUtils.setField(
//                pricePolicyForCategory.getClass().getDeclaredField("pricePolicyForCategoryId"),
//                pricePolicyForCategory,
//                0L);
//        ReflectionUtils.setField(
//                pricePolicyForCategory.getClass().getDeclaredField("minimumOrderAmount"),
//                pricePolicyForCategory,
//                10000);
//        ReflectionUtils.setField(
//                pricePolicyForCategory.getClass().getDeclaredField("discountPrice"),
//                pricePolicyForCategory,
//                1000);
//        ReflectionUtils.setField(
//                pricePolicyForCategory.getClass().getDeclaredField("expirationPeriodStart"),
//                pricePolicyForCategory,
//                LocalDateTime.of(2024,1,1,1,1));
//        ReflectionUtils.setField(
//                pricePolicyForCategory.getClass().getDeclaredField("expirationPeriodEnd"),
//                pricePolicyForCategory,
//                LocalDateTime.of(2024,1,10,1,1));
//        ReflectionUtils.setField(
//                pricePolicyForCategory.getClass().getDeclaredField("name"),
//                pricePolicyForCategory,
//                "테스트용 정률정책 for Book");
//        ReflectionUtils.setField(
//                pricePolicyForCategory.getClass().getDeclaredField("description"),
//                pricePolicyForCategory,
//                "테스트용 정률정책 for Book");
//        ReflectionUtils.setField(
//                pricePolicyForCategory.getClass().getDeclaredField("category"),
//                pricePolicyForCategory,
//                category);
//        ReflectionUtils.setField(
//                pricePolicyForCategory.getClass().getDeclaredField("policyStatus"),
//                pricePolicyForCategory,
//                usedStatus);
//
//        Mockito.when(pricePoliciesForCategoryRepository.findById(0L)).thenReturn(Optional.of(pricePolicyForCategory));
//        PricePolicyForCategoryResponse expected = PricePolicyForCategoryResponse.changeEntityToDto(pricePolicyForCategory);
//        PricePolicyForCategoryResponse actual = policyService.getPricePolicyForCategory(0L);
//
//        Mockito.verify(pricePoliciesForCategoryRepository, Mockito.times(1) ).findById(0L);
//        Assertions.assertEquals(expected,actual);
//    }
//
//    @Test
//    @DisplayName("정액정책 for Category 개별조회 - 정책 ID가 존재하지 않을때")
//    void getPricePolicyForCategoryWhenPolicyNotFoundTest() throws NoSuchFieldException {
//
//        Mockito.when(pricePoliciesForCategoryRepository.findById(0L)).thenThrow(PolicyNotFoundException.class);
//
//        Assertions.assertThrows(PolicyNotFoundException.class, ()->{
//            policyService.getPricePolicyForCategory(0L);
//        });
//
//        Mockito.verify(pricePoliciesForCategoryRepository, Mockito.times(1) ).findById(0L);
//    }
//
//    @Test
//    @DisplayName("정률정책 for Book 삭제 - 사용된 정책 - 정상동작")
//    void deleteUsedRatePolicyForBookTest() throws NoSuchFieldException {
//
//        RatePolicyForBook ratePolicyForBook = new RatePolicyForBook();
//        for(Field field : ratePolicyForBook.getClass().getDeclaredFields()){
//            field.setAccessible(true);
//        }
//        ReflectionUtils.setField(
//                ratePolicyForBook.getClass().getDeclaredField("ratePolicyForBookId"),
//                ratePolicyForBook,
//                0L);
//        ReflectionUtils.setField(
//                ratePolicyForBook.getClass().getDeclaredField("discountRate"),
//                ratePolicyForBook,
//                10);
//        ReflectionUtils.setField(
//                ratePolicyForBook.getClass().getDeclaredField("minimumOrderAmount"),
//                ratePolicyForBook,
//                10000);
//        ReflectionUtils.setField(
//                ratePolicyForBook.getClass().getDeclaredField("maximumDiscountPrice"),
//                ratePolicyForBook,
//                2000);
//        ReflectionUtils.setField(
//                ratePolicyForBook.getClass().getDeclaredField("expirationPeriodStart"),
//                ratePolicyForBook,
//                LocalDateTime.of(2024,1,1,1,1));
//        ReflectionUtils.setField(
//                ratePolicyForBook.getClass().getDeclaredField("expirationPeriodEnd"),
//                ratePolicyForBook,
//                LocalDateTime.of(2024,1,10,1,1));
//        ReflectionUtils.setField(
//                ratePolicyForBook.getClass().getDeclaredField("name"),
//                ratePolicyForBook,
//                "테스트용 정률정책 for Book");
//        ReflectionUtils.setField(
//                ratePolicyForBook.getClass().getDeclaredField("description"),
//                ratePolicyForBook,
//                "테스트용 정률정책 for Book");
//        ReflectionUtils.setField(
//                ratePolicyForBook.getClass().getDeclaredField("book"),
//                ratePolicyForBook,
//                book);
//        ReflectionUtils.setField(
//                ratePolicyForBook.getClass().getDeclaredField("policyStatus"),
//                ratePolicyForBook,
//                usedStatus);
//
//        Mockito.when(ratePoliciesForBookRepository.findById(0L))
//                .thenReturn(Optional.of(ratePolicyForBook));
//
//        PolicyStatus deleteStatus = new PolicyStatus();
//        for(Field field :deleteStatus.getClass().getDeclaredFields()){
//            field.setAccessible(true);
//        }
//        ReflectionUtils.setField(
//                deleteStatus.getClass().getDeclaredField("policyStatusId"),
//                deleteStatus,
//                2
//                );
//        ReflectionUtils.setField(
//                deleteStatus.getClass().getDeclaredField("name"),
//                deleteStatus,
//                "삭제됨"
//        );
//        Mockito.when(policyStatusRepository.findByName("삭제됨"))
//                .thenReturn(deleteStatus);
//
//        RatePolicyForBookResponse expected = new RatePolicyForBookResponse(
//                0L,
//                10,
//                10000,
//                2000,
//                LocalDateTime.of(2024,1,1,1,1),
//                LocalDateTime.of(2024,1,10,1,1),
//                "테스트용 정률정책 for Book",
//                "테스트용 정률정책 for Book",
//                "테스트용 도서",
//                "테스트용 Isbn13",
//                "삭제됨"
//        );
//        RatePolicyForBookResponse actual = policyService.deleteRatePolicyForBook(0L);
//        Mockito.verify(ratePoliciesForBookRepository,Mockito.times(1)).findById(0L);
//        Mockito.verify(policyStatusRepository,Mockito.times(1)).findByName("삭제됨");
//
//        Assertions.assertEquals(expected,actual);
//    }
//
//    @Test
//    @DisplayName("정률정책 for Book 삭제 - 사용되지 않은 정책 - 정상동작")
//    void deleteUnusedRatePolicyForBookTest() throws NoSuchFieldException {
//
//        RatePolicyForBook ratePolicyForBook = new RatePolicyForBook();
//        for(Field field : ratePolicyForBook.getClass().getDeclaredFields()){
//            field.setAccessible(true);
//        }
//        ReflectionUtils.setField(
//                ratePolicyForBook.getClass().getDeclaredField("ratePolicyForBookId"),
//                ratePolicyForBook,
//                0L);
//        ReflectionUtils.setField(
//                ratePolicyForBook.getClass().getDeclaredField("discountRate"),
//                ratePolicyForBook,
//                10);
//        ReflectionUtils.setField(
//                ratePolicyForBook.getClass().getDeclaredField("minimumOrderAmount"),
//                ratePolicyForBook,
//                10000);
//        ReflectionUtils.setField(
//                ratePolicyForBook.getClass().getDeclaredField("maximumDiscountPrice"),
//                ratePolicyForBook,
//                2000);
//        ReflectionUtils.setField(
//                ratePolicyForBook.getClass().getDeclaredField("expirationPeriodStart"),
//                ratePolicyForBook,
//                LocalDateTime.of(2024,1,1,1,1));
//        ReflectionUtils.setField(
//                ratePolicyForBook.getClass().getDeclaredField("expirationPeriodEnd"),
//                ratePolicyForBook,
//                LocalDateTime.of(2024,1,10,1,1));
//        ReflectionUtils.setField(
//                ratePolicyForBook.getClass().getDeclaredField("name"),
//                ratePolicyForBook,
//                "테스트용 정률정책 for Book");
//        ReflectionUtils.setField(
//                ratePolicyForBook.getClass().getDeclaredField("description"),
//                ratePolicyForBook,
//                "테스트용 정률정책 for Book");
//        ReflectionUtils.setField(
//                ratePolicyForBook.getClass().getDeclaredField("book"),
//                ratePolicyForBook,
//                book);
//        ReflectionUtils.setField(
//                ratePolicyForBook.getClass().getDeclaredField("policyStatus"),
//                ratePolicyForBook,
//                unusedStatus);
//
//        Mockito.when(ratePoliciesForBookRepository.findById(0L))
//                .thenReturn(Optional.of(ratePolicyForBook));
//
//        PolicyStatus deleteStatus = new PolicyStatus();
//        for(Field field :deleteStatus.getClass().getDeclaredFields()){
//            field.setAccessible(true);
//        }
//        ReflectionUtils.setField(
//                deleteStatus.getClass().getDeclaredField("policyStatusId"),
//                deleteStatus,
//                2
//        );
//        ReflectionUtils.setField(
//                deleteStatus.getClass().getDeclaredField("name"),
//                deleteStatus,
//                "삭제됨"
//        );
//        Mockito.when(policyStatusRepository.findByName("삭제됨"))
//                .thenReturn(deleteStatus);
//
//        RatePolicyForBookResponse expected = new RatePolicyForBookResponse(
//                0L,
//                10,
//                10000,
//                2000,
//                LocalDateTime.of(2024,1,1,1,1),
//                LocalDateTime.of(2024,1,10,1,1),
//                "테스트용 정률정책 for Book",
//                "테스트용 정률정책 for Book",
//                "테스트용 도서",
//                "테스트용 Isbn13",
//                "삭제됨"
//        );
//
//        RatePolicyForBookResponse actual = policyService.deleteRatePolicyForBook(0L);
//        Mockito.verify(ratePoliciesForBookRepository,Mockito.times(1)).findById(0L);
//        Mockito.verify(ratePoliciesForBookRepository,Mockito.times(1)).delete(ratePolicyForBook);
//        Mockito.verify(policyStatusRepository,Mockito.times(1)).findByName("삭제됨");
//
//        Assertions.assertEquals(expected,actual);
//    }
//
//    @Test
//    @DisplayName("정률정책 for Book 삭제 - 해당하는 ID의 정책이 없을때")
//    void deleteRatePolicyForBookWhenPolicyNotFoundTest() throws NoSuchFieldException {
//
//        Mockito.when(ratePoliciesForBookRepository.findById(99999L)) // 존재하지 않는 id
//                .thenThrow(PolicyNotFoundException.class);
//
//        Assertions.assertThrows(PolicyNotFoundException.class,()->{
//            policyService.deleteRatePolicyForBook(99999L);
//        });
//    }
//
//    @Test
//    @DisplayName("정률정책 for Category 삭제 - 사용된 정책 - 정상동작")
//    void deleteUsedRatePolicyForCategoryTest() throws NoSuchFieldException {
//
//        RatePolicyForCategory ratePolicyForCategory = new RatePolicyForCategory();
//        for(Field field : ratePolicyForCategory.getClass().getDeclaredFields()){
//            field.setAccessible(true);
//        }
//        ReflectionUtils.setField(
//                ratePolicyForCategory.getClass().getDeclaredField("ratePolicyForCategoryId"),
//                ratePolicyForCategory,
//                0L);
//        ReflectionUtils.setField(
//                ratePolicyForCategory.getClass().getDeclaredField("discountRate"),
//                ratePolicyForCategory,
//                10);
//        ReflectionUtils.setField(
//                ratePolicyForCategory.getClass().getDeclaredField("minimumOrderAmount"),
//                ratePolicyForCategory,
//                10000);
//        ReflectionUtils.setField(
//                ratePolicyForCategory.getClass().getDeclaredField("maximumDiscountPrice"),
//                ratePolicyForCategory,
//                2000);
//        ReflectionUtils.setField(
//                ratePolicyForCategory.getClass().getDeclaredField("expirationPeriodStart"),
//                ratePolicyForCategory,
//                LocalDateTime.of(2024,1,1,1,1));
//        ReflectionUtils.setField(
//                ratePolicyForCategory.getClass().getDeclaredField("expirationPeriodEnd"),
//                ratePolicyForCategory,
//                LocalDateTime.of(2024,1,10,1,1));
//        ReflectionUtils.setField(
//                ratePolicyForCategory.getClass().getDeclaredField("name"),
//                ratePolicyForCategory,
//                "테스트용 정률정책 for Book");
//        ReflectionUtils.setField(
//                ratePolicyForCategory.getClass().getDeclaredField("description"),
//                ratePolicyForCategory,
//                "테스트용 정률정책 for Book");
//        ReflectionUtils.setField(
//                ratePolicyForCategory.getClass().getDeclaredField("category"),
//                ratePolicyForCategory,
//                category);
//        ReflectionUtils.setField(
//                ratePolicyForCategory.getClass().getDeclaredField("policyStatus"),
//                ratePolicyForCategory,
//                usedStatus);
//
//        Mockito.when(ratePoliciesForCategoryRepository.findById(0L))
//                .thenReturn(Optional.of(ratePolicyForCategory));
//
//        PolicyStatus deleteStatus = new PolicyStatus();
//        for(Field field :deleteStatus.getClass().getDeclaredFields()){
//            field.setAccessible(true);
//        }
//        ReflectionUtils.setField(
//                deleteStatus.getClass().getDeclaredField("policyStatusId"),
//                deleteStatus,
//                2
//        );
//        ReflectionUtils.setField(
//                deleteStatus.getClass().getDeclaredField("name"),
//                deleteStatus,
//                "삭제됨"
//        );
//        Mockito.when(policyStatusRepository.findByName("삭제됨"))
//                .thenReturn(deleteStatus);
//
//        RatePolicyForCategoryResponse expected = new RatePolicyForCategoryResponse(
//                0L,
//                10,
//                10000,
//                2000,
//                LocalDateTime.of(2024,1,1,1,1),
//                LocalDateTime.of(2024,1,10,1,1),
//                "테스트용 정률정책 for Book",
//                "테스트용 정률정책 for Book",
//                "테스트용 카테고리",
//                "삭제됨"
//        );
//        RatePolicyForCategoryResponse actual = policyService.deleteRatePolicyForCategory(0L);
//        Mockito.verify(ratePoliciesForCategoryRepository,Mockito.times(1)).findById(0L);
//        Mockito.verify(policyStatusRepository,Mockito.times(1)).findByName("삭제됨");
//
//        Assertions.assertEquals(expected,actual);
//    }
//
//    @Test
//    @DisplayName("정률정책 for Category 삭제 - 사용되지 않은 정책 - 정상동작")
//    void deleteUnusedRatePolicyForCategoryTest() throws NoSuchFieldException {
//
//        RatePolicyForCategory ratePolicyForCategory = new RatePolicyForCategory();
//        for(Field field : ratePolicyForCategory.getClass().getDeclaredFields()){
//            field.setAccessible(true);
//        }
//        ReflectionUtils.setField(
//                ratePolicyForCategory.getClass().getDeclaredField("ratePolicyForCategoryId"),
//                ratePolicyForCategory,
//                0L);
//        ReflectionUtils.setField(
//                ratePolicyForCategory.getClass().getDeclaredField("discountRate"),
//                ratePolicyForCategory,
//                10);
//        ReflectionUtils.setField(
//                ratePolicyForCategory.getClass().getDeclaredField("minimumOrderAmount"),
//                ratePolicyForCategory,
//                10000);
//        ReflectionUtils.setField(
//                ratePolicyForCategory.getClass().getDeclaredField("maximumDiscountPrice"),
//                ratePolicyForCategory,
//                2000);
//        ReflectionUtils.setField(
//                ratePolicyForCategory.getClass().getDeclaredField("expirationPeriodStart"),
//                ratePolicyForCategory,
//                LocalDateTime.of(2024,1,1,1,1));
//        ReflectionUtils.setField(
//                ratePolicyForCategory.getClass().getDeclaredField("expirationPeriodEnd"),
//                ratePolicyForCategory,
//                LocalDateTime.of(2024,1,10,1,1));
//        ReflectionUtils.setField(
//                ratePolicyForCategory.getClass().getDeclaredField("name"),
//                ratePolicyForCategory,
//                "테스트용 정률정책 for Book");
//        ReflectionUtils.setField(
//                ratePolicyForCategory.getClass().getDeclaredField("description"),
//                ratePolicyForCategory,
//                "테스트용 정률정책 for Book");
//        ReflectionUtils.setField(
//                ratePolicyForCategory.getClass().getDeclaredField("category"),
//                ratePolicyForCategory,
//                category);
//        ReflectionUtils.setField(
//                ratePolicyForCategory.getClass().getDeclaredField("policyStatus"),
//                ratePolicyForCategory,
//                unusedStatus);
//
//        Mockito.when(ratePoliciesForCategoryRepository.findById(0L))
//                .thenReturn(Optional.of(ratePolicyForCategory));
//
//        PolicyStatus deleteStatus = new PolicyStatus();
//        for(Field field :deleteStatus.getClass().getDeclaredFields()){
//            field.setAccessible(true);
//        }
//        ReflectionUtils.setField(
//                deleteStatus.getClass().getDeclaredField("policyStatusId"),
//                deleteStatus,
//                2
//        );
//        ReflectionUtils.setField(
//                deleteStatus.getClass().getDeclaredField("name"),
//                deleteStatus,
//                "삭제됨"
//        );
//        Mockito.when(policyStatusRepository.findByName("삭제됨"))
//                .thenReturn(deleteStatus);
//
//        RatePolicyForCategoryResponse expected = new RatePolicyForCategoryResponse(
//                0L,
//                10,
//                10000,
//                2000,
//                LocalDateTime.of(2024,1,1,1,1),
//                LocalDateTime.of(2024,1,10,1,1),
//                "테스트용 정률정책 for Book",
//                "테스트용 정률정책 for Book",
//                "테스트용 카테고리",
//                "삭제됨"
//        );
//        RatePolicyForCategoryResponse actual = policyService.deleteRatePolicyForCategory(0L);
//        Mockito.verify(ratePoliciesForCategoryRepository,Mockito.times(1)).delete(ratePolicyForCategory);
//        Mockito.verify(ratePoliciesForCategoryRepository,Mockito.times(1)).findById(0L);
//        Mockito.verify(policyStatusRepository,Mockito.times(1)).findByName("삭제됨");
//
//        Assertions.assertEquals(expected,actual);
//    }
//
//    @Test
//    @DisplayName("정률정책 for Category 삭제 - 해당하는 ID의 정책이 없을때")
//    void deleteRatePolicyForCategoryWhenPolicyNotFoundTest() throws NoSuchFieldException {
//
//        Mockito.when(ratePoliciesForCategoryRepository.findById(99999L)) // 존재하지 않는 id
//                .thenThrow(PolicyNotFoundException.class);
//
//        Assertions.assertThrows(PolicyNotFoundException.class,()->{
//            policyService.deleteRatePolicyForCategory(99999L);
//        });
//    }
//
//    @Test
//    @DisplayName("정액정책 for Book 삭제 - 사용된 정책 - 정상동작")
//    void deleteUsedPricePolicyForBookTest() throws NoSuchFieldException {
//
//        PricePolicyForBook pricePolicyForBook = new PricePolicyForBook();
//        for(Field field : pricePolicyForBook.getClass().getDeclaredFields()){
//            field.setAccessible(true);
//        }
//        ReflectionUtils.setField(
//                pricePolicyForBook.getClass().getDeclaredField("pricePolicyForBookId"),
//                pricePolicyForBook,
//                0L);
//        ReflectionUtils.setField(
//                pricePolicyForBook.getClass().getDeclaredField("discountPrice"),
//                pricePolicyForBook,
//                2000);
//        ReflectionUtils.setField(
//                pricePolicyForBook.getClass().getDeclaredField("minimumOrderAmount"),
//                pricePolicyForBook,
//                10000);
//        ReflectionUtils.setField(
//                pricePolicyForBook.getClass().getDeclaredField("expirationPeriodStart"),
//                pricePolicyForBook,
//                LocalDateTime.of(2024,1,1,1,1));
//        ReflectionUtils.setField(
//                pricePolicyForBook.getClass().getDeclaredField("expirationPeriodEnd"),
//                pricePolicyForBook,
//                LocalDateTime.of(2024,1,10,1,1));
//        ReflectionUtils.setField(
//                pricePolicyForBook.getClass().getDeclaredField("name"),
//                pricePolicyForBook,
//                "테스트용 정률정책 for Book");
//        ReflectionUtils.setField(
//                pricePolicyForBook.getClass().getDeclaredField("description"),
//                pricePolicyForBook,
//                "테스트용 정률정책 for Book");
//        ReflectionUtils.setField(
//                pricePolicyForBook.getClass().getDeclaredField("book"),
//                pricePolicyForBook,
//                book);
//        ReflectionUtils.setField(
//                pricePolicyForBook.getClass().getDeclaredField("policyStatus"),
//                pricePolicyForBook,
//                usedStatus);
//
//        Mockito.when(pricePoliciesForBookRepository.findById(0L))
//                .thenReturn(Optional.of(pricePolicyForBook));
//
//        PolicyStatus deleteStatus = new PolicyStatus();
//        for(Field field :deleteStatus.getClass().getDeclaredFields()){
//            field.setAccessible(true);
//        }
//        ReflectionUtils.setField(
//                deleteStatus.getClass().getDeclaredField("policyStatusId"),
//                deleteStatus,
//                2
//        );
//        ReflectionUtils.setField(
//                deleteStatus.getClass().getDeclaredField("name"),
//                deleteStatus,
//                "삭제됨"
//        );
//        Mockito.when(policyStatusRepository.findByName("삭제됨"))
//                .thenReturn(deleteStatus);
//
//        PricePolicyForBookResponse expected = new PricePolicyForBookResponse(
//                0L,
//                10000,
//                2000,
//                LocalDateTime.of(2024,1,1,1,1),
//                LocalDateTime.of(2024,1,10,1,1),
//                "테스트용 정률정책 for Book",
//                "테스트용 정률정책 for Book",
//                "테스트용 도서",
//                "테스트용 Isbn13",
//                "삭제됨"
//        );
//        PricePolicyForBookResponse actual = policyService.deletePricePolicyForBook(0L);
//        Mockito.verify(pricePoliciesForBookRepository,Mockito.times(1)).findById(0L);
//        Mockito.verify(policyStatusRepository,Mockito.times(1)).findByName("삭제됨");
//
//        Assertions.assertEquals(expected,actual);
//    }
//
//    @Test
//    @DisplayName("정액정책 for Book 삭제 - 사용되지 않은 정책 - 정상동작")
//    void deleteUnusedPricePolicyForBookTest() throws NoSuchFieldException {
//
//        PricePolicyForBook pricePolicyForBook = new PricePolicyForBook();
//        for(Field field : pricePolicyForBook.getClass().getDeclaredFields()){
//            field.setAccessible(true);
//        }
//        ReflectionUtils.setField(
//                pricePolicyForBook.getClass().getDeclaredField("pricePolicyForBookId"),
//                pricePolicyForBook,
//                0L);
//        ReflectionUtils.setField(
//                pricePolicyForBook.getClass().getDeclaredField("discountPrice"),
//                pricePolicyForBook,
//                2000);
//        ReflectionUtils.setField(
//                pricePolicyForBook.getClass().getDeclaredField("minimumOrderAmount"),
//                pricePolicyForBook,
//                10000);
//        ReflectionUtils.setField(
//                pricePolicyForBook.getClass().getDeclaredField("expirationPeriodStart"),
//                pricePolicyForBook,
//                LocalDateTime.of(2024,1,1,1,1));
//        ReflectionUtils.setField(
//                pricePolicyForBook.getClass().getDeclaredField("expirationPeriodEnd"),
//                pricePolicyForBook,
//                LocalDateTime.of(2024,1,10,1,1));
//        ReflectionUtils.setField(
//                pricePolicyForBook.getClass().getDeclaredField("name"),
//                pricePolicyForBook,
//                "테스트용 정률정책 for Book");
//        ReflectionUtils.setField(
//                pricePolicyForBook.getClass().getDeclaredField("description"),
//                pricePolicyForBook,
//                "테스트용 정률정책 for Book");
//        ReflectionUtils.setField(
//                pricePolicyForBook.getClass().getDeclaredField("book"),
//                pricePolicyForBook,
//                book);
//        ReflectionUtils.setField(
//                pricePolicyForBook.getClass().getDeclaredField("policyStatus"),
//                pricePolicyForBook,
//                unusedStatus);
//
//        Mockito.when(pricePoliciesForBookRepository.findById(0L))
//                .thenReturn(Optional.of(pricePolicyForBook));
//
//        PolicyStatus deleteStatus = new PolicyStatus();
//        for(Field field :deleteStatus.getClass().getDeclaredFields()){
//            field.setAccessible(true);
//        }
//        ReflectionUtils.setField(
//                deleteStatus.getClass().getDeclaredField("policyStatusId"),
//                deleteStatus,
//                2
//        );
//        ReflectionUtils.setField(
//                deleteStatus.getClass().getDeclaredField("name"),
//                deleteStatus,
//                "삭제됨"
//        );
//        Mockito.when(policyStatusRepository.findByName("삭제됨"))
//                .thenReturn(deleteStatus);
//
//        PricePolicyForBookResponse expected = new PricePolicyForBookResponse(
//                0L,
//                10000,
//                2000,
//                LocalDateTime.of(2024,1,1,1,1),
//                LocalDateTime.of(2024,1,10,1,1),
//                "테스트용 정률정책 for Book",
//                "테스트용 정률정책 for Book",
//                "테스트용 도서",
//                "테스트용 Isbn13",
//                "삭제됨"
//        );
//        PricePolicyForBookResponse actual = policyService.deletePricePolicyForBook(0L);
//
//        Mockito.verify(pricePoliciesForBookRepository,Mockito.times(1)).delete(pricePolicyForBook);
//        Mockito.verify(policyStatusRepository,Mockito.times(1)).findByName("삭제됨");
//        Mockito.verify(pricePoliciesForBookRepository,Mockito.times(1)).findById(0L);
//
//        Assertions.assertEquals(expected,actual);
//    }
//
//    @Test
//    @DisplayName("정액정책 for Book 삭제 - 해당 ID의 정책이 없을때")
//    void deletePricePolicyForBookWhenPolicyNotFoundTest() throws NoSuchFieldException {
//
//        Mockito.when(pricePoliciesForBookRepository.findById(99999L))
//                .thenThrow(PolicyNotFoundException.class);
//
//        Assertions.assertThrows(PolicyNotFoundException.class , ()->{
//            policyService.deletePricePolicyForBook(99999L);
//        });
//
//    }
//
//    @Test
//    @DisplayName("정액정책 for Category 삭제 - 사용된 정책 - 정상동작")
//    void deleteUsedPricePolicyForCategoryTest() throws NoSuchFieldException {
//
//        PricePolicyForCategory pricePolicyForCategory = new PricePolicyForCategory();
//        for(Field field : pricePolicyForCategory.getClass().getDeclaredFields()){
//            field.setAccessible(true);
//        }
//        ReflectionUtils.setField(
//                pricePolicyForCategory.getClass().getDeclaredField("pricePolicyForCategoryId"),
//                pricePolicyForCategory,
//                0L);
//        ReflectionUtils.setField(
//                pricePolicyForCategory.getClass().getDeclaredField("discountPrice"),
//                pricePolicyForCategory,
//                2000);
//        ReflectionUtils.setField(
//                pricePolicyForCategory.getClass().getDeclaredField("minimumOrderAmount"),
//                pricePolicyForCategory,
//                10000);
//        ReflectionUtils.setField(
//                pricePolicyForCategory.getClass().getDeclaredField("expirationPeriodStart"),
//                pricePolicyForCategory,
//                LocalDateTime.of(2024,1,1,1,1));
//        ReflectionUtils.setField(
//                pricePolicyForCategory.getClass().getDeclaredField("expirationPeriodEnd"),
//                pricePolicyForCategory,
//                LocalDateTime.of(2024,1,10,1,1));
//        ReflectionUtils.setField(
//                pricePolicyForCategory.getClass().getDeclaredField("name"),
//                pricePolicyForCategory,
//                "테스트용 정률정책 for Book");
//        ReflectionUtils.setField(
//                pricePolicyForCategory.getClass().getDeclaredField("description"),
//                pricePolicyForCategory,
//                "테스트용 정률정책 for Book");
//        ReflectionUtils.setField(
//                pricePolicyForCategory.getClass().getDeclaredField("category"),
//                pricePolicyForCategory,
//                category);
//        ReflectionUtils.setField(
//                pricePolicyForCategory.getClass().getDeclaredField("policyStatus"),
//                pricePolicyForCategory,
//                usedStatus);
//
//        Mockito.when(pricePoliciesForCategoryRepository.findById(0L))
//                .thenReturn(Optional.of(pricePolicyForCategory));
//
//        PolicyStatus deleteStatus = new PolicyStatus();
//        for(Field field :deleteStatus.getClass().getDeclaredFields()){
//            field.setAccessible(true);
//        }
//        ReflectionUtils.setField(
//                deleteStatus.getClass().getDeclaredField("policyStatusId"),
//                deleteStatus,
//                2
//        );
//        ReflectionUtils.setField(
//                deleteStatus.getClass().getDeclaredField("name"),
//                deleteStatus,
//                "삭제됨"
//        );
//        Mockito.when(policyStatusRepository.findByName("삭제됨"))
//                .thenReturn(deleteStatus);
//
//        PricePolicyForCategoryResponse expected = new PricePolicyForCategoryResponse(
//                0L,
//                10000,
//                2000,
//                LocalDateTime.of(2024,1,1,1,1),
//                LocalDateTime.of(2024,1,10,1,1),
//                "테스트용 정률정책 for Book",
//                "테스트용 정률정책 for Book",
//                "테스트용 카테고리",
//                "삭제됨"
//        );
//        PricePolicyForCategoryResponse actual = policyService.deletePricePolicyForCategory(0L);
//        Mockito.verify(pricePoliciesForCategoryRepository,Mockito.times(1)).findById(0L);
//        Mockito.verify(policyStatusRepository,Mockito.times(1)).findByName("삭제됨");
//
//        Assertions.assertEquals(expected,actual);
//    }
//
//    @Test
//    @DisplayName("정액정책 for Category 삭제 - 사용되지 않은 정책 - 정상동작")
//    void deleteUnusedPricePolicyForCategoryTest() throws NoSuchFieldException {
//
//        PricePolicyForCategory pricePolicyForCategory = new PricePolicyForCategory();
//        for(Field field : pricePolicyForCategory.getClass().getDeclaredFields()){
//            field.setAccessible(true);
//        }
//        ReflectionUtils.setField(
//                pricePolicyForCategory.getClass().getDeclaredField("pricePolicyForCategoryId"),
//                pricePolicyForCategory,
//                0L);
//        ReflectionUtils.setField(
//                pricePolicyForCategory.getClass().getDeclaredField("discountPrice"),
//                pricePolicyForCategory,
//                2000);
//        ReflectionUtils.setField(
//                pricePolicyForCategory.getClass().getDeclaredField("minimumOrderAmount"),
//                pricePolicyForCategory,
//                10000);
//        ReflectionUtils.setField(
//                pricePolicyForCategory.getClass().getDeclaredField("expirationPeriodStart"),
//                pricePolicyForCategory,
//                LocalDateTime.of(2024,1,1,1,1));
//        ReflectionUtils.setField(
//                pricePolicyForCategory.getClass().getDeclaredField("expirationPeriodEnd"),
//                pricePolicyForCategory,
//                LocalDateTime.of(2024,1,10,1,1));
//        ReflectionUtils.setField(
//                pricePolicyForCategory.getClass().getDeclaredField("name"),
//                pricePolicyForCategory,
//                "테스트용 정률정책 for Book");
//        ReflectionUtils.setField(
//                pricePolicyForCategory.getClass().getDeclaredField("description"),
//                pricePolicyForCategory,
//                "테스트용 정률정책 for Book");
//        ReflectionUtils.setField(
//                pricePolicyForCategory.getClass().getDeclaredField("category"),
//                pricePolicyForCategory,
//                category);
//        ReflectionUtils.setField(
//                pricePolicyForCategory.getClass().getDeclaredField("policyStatus"),
//                pricePolicyForCategory,
//                unusedStatus);
//
//        Mockito.when(pricePoliciesForCategoryRepository.findById(0L))
//                .thenReturn(Optional.of(pricePolicyForCategory));
//
//        PolicyStatus deleteStatus = new PolicyStatus();
//        for(Field field :deleteStatus.getClass().getDeclaredFields()){
//            field.setAccessible(true);
//        }
//        ReflectionUtils.setField(
//                deleteStatus.getClass().getDeclaredField("policyStatusId"),
//                deleteStatus,
//                2
//        );
//        ReflectionUtils.setField(
//                deleteStatus.getClass().getDeclaredField("name"),
//                deleteStatus,
//                "삭제됨"
//        );
//        Mockito.when(policyStatusRepository.findByName("삭제됨"))
//                .thenReturn(deleteStatus);
//
//        PricePolicyForCategoryResponse expected = new PricePolicyForCategoryResponse(
//                0L,
//                10000,
//                2000,
//                LocalDateTime.of(2024,1,1,1,1),
//                LocalDateTime.of(2024,1,10,1,1),
//                "테스트용 정률정책 for Book",
//                "테스트용 정률정책 for Book",
//                "테스트용 카테고리",
//                "삭제됨"
//        );
//        PricePolicyForCategoryResponse actual = policyService.deletePricePolicyForCategory(0L);
//        Mockito.verify(pricePoliciesForCategoryRepository,Mockito.times(1)).findById(0L);
//        Mockito.verify(policyStatusRepository,Mockito.times(1)).findByName("삭제됨");
//        Mockito.verify(pricePoliciesForCategoryRepository,Mockito.times(1)).delete(pricePolicyForCategory);
//        Assertions.assertEquals(expected,actual);
//    }
//
//    @Test
//    @DisplayName("정액정책 for Category 삭제 - 해당 ID의 정책이 없을때")
//    void deletePricePolicyForCategoryWhenPolicyNotFoundTest() throws NoSuchFieldException {
//
//        Mockito.when(pricePoliciesForCategoryRepository.findById(99999L))
//                .thenThrow(PolicyNotFoundException.class);
//
//        Assertions.assertThrows(PolicyNotFoundException.class , ()->{
//            policyService.deletePricePolicyForCategory(99999L);
//        });
//
//    }
//}