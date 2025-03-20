//package com.nhnacademy.taskapi.coupon.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.nhnacademy.taskapi.coupon.domain.dto.policies.request.create.AddPricePolicyForBookRequest;
//import com.nhnacademy.taskapi.coupon.domain.dto.policies.request.create.AddPricePolicyForCategoryRequest;
//import com.nhnacademy.taskapi.coupon.domain.dto.policies.request.create.AddRatePolicyForBookRequest;
//import com.nhnacademy.taskapi.coupon.domain.dto.policies.request.create.AddRatePolicyForCategoryRequest;
//import com.nhnacademy.taskapi.coupon.domain.dto.policies.response.PricePolicyForBookResponse;
//import com.nhnacademy.taskapi.coupon.domain.dto.policies.response.PricePolicyForCategoryResponse;
//import com.nhnacademy.taskapi.coupon.domain.dto.policies.response.RatePolicyForBookResponse;
//import com.nhnacademy.taskapi.coupon.domain.dto.policies.response.RatePolicyForCategoryResponse;
//import com.nhnacademy.taskapi.coupon.service.policies.PolicyService;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
//import org.springframework.data.util.ReflectionUtils;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.lang.reflect.Field;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//
//
//@WebMvcTest(PolicyController.class)
//@MockBean(JpaMetamodelMappingContext.class)
//class PolicyControllerTest {
//
//    @MockBean
//    private PolicyService policyService;
//
//    private int port = 8080;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    private static AddRatePolicyForBookRequest addRatePolicyForBookRequest;
//    private static AddRatePolicyForCategoryRequest addRatePolicyForCategoryRequest;
//    private static RatePolicyForBookResponse addRatePolicyForBookResponse;
//    private static RatePolicyForCategoryResponse addRatePolicyForCategoryResponse;
//
//    private static AddPricePolicyForBookRequest addPricePolicyForBookRequest;
//    private static AddPricePolicyForCategoryRequest addPricePolicyForCategoryRequest;
//    private static PricePolicyForBookResponse addPricePolicyForBookResponse;
//    private static PricePolicyForCategoryResponse addPricePolicyForCategoryResponse;
//
//    @BeforeAll
//    static void setUp() throws NoSuchFieldException {
//
//        // 정률정책 for Book 등록 요청
//        addRatePolicyForBookRequest = new AddRatePolicyForBookRequest();
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
//        // 정률정책 for Book 등록 응답
//        addRatePolicyForBookResponse = new RatePolicyForBookResponse(
//                1L,
//                10,
//                30000,
//                10000,
//                LocalDateTime.of(2024,1,1,12,0),
//                LocalDateTime.of(2024,1,10,12,0),
//                "테스트용 정책",
//                "테스트용 정책 설명",
//                "테스트용 도서",
//                "테스트용 Isbn13",
//                "사용됨"
//        );
//
//        // 정률정책 for category 등록 요청
//        addRatePolicyForCategoryRequest = new AddRatePolicyForCategoryRequest();
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
//        // 정률정책 for Category 등록 응답
//        addRatePolicyForCategoryResponse = new RatePolicyForCategoryResponse(
//                1L,
//                10,
//                30000,
//                10000,
//                LocalDateTime.of(2024,1,1,12,0),
//                LocalDateTime.of(2024,1,10,12,0),
//                "테스트용 정책",
//                "테스트용 정책 설명",
//                "테스트용 카테고리",
//                "미사용"
//        );
//
//        // 정액정책 for Book 등록 요청
//        addPricePolicyForBookRequest = new AddPricePolicyForBookRequest();
//        for(Field field : addPricePolicyForBookRequest.getClass().getDeclaredFields()){
//            field.setAccessible(true);
//        }
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
//                ,0);
//
//        // 정액정책 for Book 등록 응답
//        addPricePolicyForBookResponse = new PricePolicyForBookResponse(
//                    1L,
//                        20000,
//                        5000,
//                        LocalDateTime.of(2024,1,1,12,0),
//                        LocalDateTime.of(2024,1,10,12,0),
//                        "테스트용 정액정책 for Book",
//                        "테스트용 정액정책 for Book 설명",
//                        "테스트용 도서",
//                        "테스트용 Isbn13",
//                        "미사용"
//                );
//
//        // 정액정책 for Category 등록 요청
//        addPricePolicyForCategoryRequest = new AddPricePolicyForCategoryRequest();
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
//        // 정액정책 for Category 등록 응답
//        addPricePolicyForCategoryResponse = new PricePolicyForCategoryResponse(
//                1L,
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
//    }
//
//
//    @Test
//    @DisplayName("정룰정책 for Book 등록 요청시 - 200과 응답이 잘 돌아오는지 확인")
//    void addRatePolicyForBookTest() throws Exception {
//
//        Mockito.when(policyService.addRatePolicyForBook(Mockito.any(AddRatePolicyForBookRequest.class)))
//                .thenReturn(addRatePolicyForBookResponse);
//
//        String url = "http://localhost:"+port +"/task/policies/rate/book";
//
//        String req = objectMapper.writeValueAsString(addRatePolicyForBookRequest);
//
//        mockMvc.perform(post(url).header("X-MEMBER-ID",1L)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(req))
//                .andExpect(jsonPath("$.discountRate").value(10))
//                .andExpect(jsonPath("$.minimumOrderAmount").value(30000))
//                .andExpect(jsonPath("$.maximumDiscountPrice").value(10000))
//                .andExpect(jsonPath("$.expirationPeriodStart")
//                        .value("2024-01-01T12:00:00"))
//                .andExpect(jsonPath("$.expirationPeriodEnd")
//                        .value("2024-01-10T12:00:00"))
//                .andExpect(jsonPath("$.name").value("테스트용 정책"))
//                .andExpect(jsonPath("$.description").value("테스트용 정책 설명"))
//                .andExpect(jsonPath("$.bookName").value("테스트용 도서"))
//                .andExpect(jsonPath("$.bookIsbn13").value("테스트용 Isbn13"))
//                .andExpect(jsonPath("$.policyStatusName").value("사용됨"));
//    }
//
//    @Test
//    @DisplayName("정룰정책 for Category 등록 요청시 - 200과 응답이 잘 돌아오는지 확인")
//    void addRatePolicyForCategoryTest() throws Exception {
//
//        Mockito.when(policyService.addRatePolicyForCategory(Mockito.any(AddRatePolicyForCategoryRequest.class)))
//                .thenReturn(addRatePolicyForCategoryResponse);
//
//        String url = "http://localhost:"+port +"/task/policies/rate/category";
//
//        String req = objectMapper.writeValueAsString(addRatePolicyForCategoryRequest);
//
//        mockMvc.perform(post(url).header("X-MEMBER-ID",1L)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(req))
//                .andExpect(jsonPath("$.discountRate").value(10))
//                .andExpect(jsonPath("$.minimumOrderAmount").value(30000))
//                .andExpect(jsonPath("$.maximumDiscountPrice").value(10000))
//                .andExpect(jsonPath("$.expirationPeriodStart")
//                        .value("2024-01-01T12:00:00"))
//                .andExpect(jsonPath("$.expirationPeriodEnd")
//                        .value("2024-01-10T12:00:00"))
//                .andExpect(jsonPath("$.name").value("테스트용 정책"))
//                .andExpect(jsonPath("$.description").value("테스트용 정책 설명"))
//                .andExpect(jsonPath("$.categoryName").value("테스트용 카테고리"))
//                .andExpect(jsonPath("$.policyStatusName").value("미사용"));
//    }
//
//    @Test
//    @DisplayName("정액정책 for Book 등록 요청시 - 200과 응답이 잘 돌아오는지 확인")
//    void addPricePolicyForBookTest() throws Exception {
//
//        Mockito.when(policyService.addPricePolicyForBook(Mockito.any(AddPricePolicyForBookRequest.class)))
//                .thenReturn(addPricePolicyForBookResponse);
//
//        String url = "http://localhost:"+port +"/task/policies/price/book";
//
//        String req = objectMapper.writeValueAsString(addPricePolicyForBookRequest);
//
//        mockMvc.perform(post(url).header("X-MEMBER-ID",1L)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(req))
//                .andExpect(jsonPath("$.minimumOrderAmount").value(20000))
//                .andExpect(jsonPath("$.discountPrice").value(5000))
//                .andExpect(jsonPath("$.expirationPeriodStart")
//                        .value("2024-01-01T12:00:00"))
//                .andExpect(jsonPath("$.expirationPeriodEnd")
//                        .value("2024-01-10T12:00:00"))
//                .andExpect(jsonPath("$.name").value("테스트용 정액정책 for Book"))
//                .andExpect(jsonPath("$.description").value("테스트용 정액정책 for Book 설명"))
//                .andExpect(jsonPath("$.bookName").value("테스트용 도서"))
//                .andExpect(jsonPath("$.bookIsbn13").value("테스트용 Isbn13"))
//                .andExpect(jsonPath("$.policyStatusName").value("미사용"));
//    }
//
//    @Test
//    @DisplayName("정액정책 for Category 등록 요청시 - 200과 응답이 잘 돌아오는지 확인")
//    void addPricePolicyForCategoryTest() throws Exception {
//
//        Mockito.when(policyService.addPricePolicyForCategory(Mockito.any(AddPricePolicyForCategoryRequest.class)))
//                .thenReturn(addPricePolicyForCategoryResponse);
//
//        String url = "http://localhost:"+port +"/task/policies/price/category";
//
//        String req = objectMapper.writeValueAsString(addPricePolicyForCategoryRequest);
//
//        mockMvc.perform(post(url).header("X-MEMBER-ID",1L)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(req))
//                .andExpect(jsonPath("$.minimumOrderAmount").value(20000))
//                .andExpect(jsonPath("$.discountPrice").value(5000))
//                .andExpect(jsonPath("$.expirationPeriodStart")
//                        .value("2024-01-01T12:00:00"))
//                .andExpect(jsonPath("$.expirationPeriodEnd")
//                        .value("2024-01-10T12:00:00"))
//                .andExpect(jsonPath("$.name").value("테스트용 정액정책 for Category"))
//                .andExpect(jsonPath("$.description").value("테스트용 정액정책 for Category 설명"))
//                .andExpect(jsonPath("$.categoryName").value("테스트용 카테고리"))
//                .andExpect(jsonPath("$.policyStatusName").value("사용됨"));
//    }
//
//
//    @Test
//    @DisplayName("관리자가 - 등록되어있는 - 정률정책 for Book들을 - Pagenation 해서 받아오기")
//    void getRatePoliciesForBookTest() throws Exception {
//
//        List<RatePolicyForBookResponse> list = new ArrayList<>();
//
//        for(int i = 1; i <= 10; i++){
//            RatePolicyForBookResponse ratePolicyForBookResponse = new RatePolicyForBookResponse();
//            for(Field field : ratePolicyForBookResponse.getClass().getDeclaredFields()){
//                field.setAccessible(true);
//            }
//
//            ReflectionUtils.setField(
//                    ratePolicyForBookResponse.getClass().getDeclaredField("id"),
//                    ratePolicyForBookResponse,
//                    (long)i);
//            ReflectionUtils.setField(
//                    ratePolicyForBookResponse.getClass().getDeclaredField("discountRate"),
//                    ratePolicyForBookResponse,
//                    i);
//            ReflectionUtils.setField(
//                    ratePolicyForBookResponse.getClass().getDeclaredField("minimumOrderAmount"),
//                    ratePolicyForBookResponse,
//                    i);
//            ReflectionUtils.setField(
//                    ratePolicyForBookResponse.getClass().getDeclaredField("maximumDiscountPrice"),
//                    ratePolicyForBookResponse,
//                    i);
//            ReflectionUtils.setField(
//                    ratePolicyForBookResponse.getClass().getDeclaredField("expirationPeriodStart"),
//                    ratePolicyForBookResponse,
//                    LocalDateTime.of(2024,1,i,12,0));
//            ReflectionUtils.setField(
//                    ratePolicyForBookResponse.getClass().getDeclaredField("expirationPeriodEnd"),
//                    ratePolicyForBookResponse,
//                    LocalDateTime.of(2024,1,i,12,0));
//            ReflectionUtils.setField(
//                    ratePolicyForBookResponse.getClass().getDeclaredField("name"),
//                    ratePolicyForBookResponse,
//                    Integer.toString(i));
//            ReflectionUtils.setField(
//                    ratePolicyForBookResponse.getClass().getDeclaredField("description"),
//                    ratePolicyForBookResponse,
//                    Integer.toString(i));
//            ReflectionUtils.setField(
//                    ratePolicyForBookResponse.getClass().getDeclaredField("bookName"),
//                    ratePolicyForBookResponse,
//                    Integer.toString(i));
//            ReflectionUtils.setField(
//                    ratePolicyForBookResponse.getClass().getDeclaredField("policyStatusName"),
//                    ratePolicyForBookResponse,
//                    Integer.toString(i));
//
//            list.add(ratePolicyForBookResponse);
//        }
//
//        Mockito.when(policyService.getRatePoliciesForBook(1)).thenReturn(list);
//        String url = "http://localhost:"+port +"/task/policies/rate/book";
//        mockMvc.perform(get(url).param("page","1"))
//                .andExpect(jsonPath("$[0].id").value(1))
//                .andExpect(jsonPath("$[1].id").value(2))
//                .andExpect(jsonPath("$[2].id").value(3))
//                .andExpect(jsonPath("$[3].id").value(4))
//                .andExpect(jsonPath("$[4].id").value(5))
//                .andExpect(jsonPath("$[5].id").value(6))
//                .andExpect(jsonPath("$[6].id").value(7))
//                .andExpect(jsonPath("$[7].id").value(8))
//                .andExpect(jsonPath("$[8].id").value(9))
//                .andExpect(jsonPath("$[9].id").value(10));
//    }
//
//    @Test
//    @DisplayName("관리자가 - 등록되어있는 - 정률정책 for Category들을 - Pagenation 해서 받아오기")
//    void getRatePoliciesForCategoryTest() throws Exception {
//
//        List<RatePolicyForCategoryResponse> list = new ArrayList<>();
//
//        for(int i = 1; i <= 10; i++){
//            RatePolicyForCategoryResponse ratePolicyForCategoryResponse = new RatePolicyForCategoryResponse();
//            for(Field field : ratePolicyForCategoryResponse.getClass().getDeclaredFields()){
//                field.setAccessible(true);
//            }
//
//            ReflectionUtils.setField(
//                    ratePolicyForCategoryResponse.getClass().getDeclaredField("id"),
//                    ratePolicyForCategoryResponse,
//                    (long)i);
//            ReflectionUtils.setField(
//                    ratePolicyForCategoryResponse.getClass().getDeclaredField("discountRate"),
//                    ratePolicyForCategoryResponse,
//                    i);
//            ReflectionUtils.setField(
//                    ratePolicyForCategoryResponse.getClass().getDeclaredField("minimumOrderAmount"),
//                    ratePolicyForCategoryResponse,
//                    i);
//            ReflectionUtils.setField(
//                    ratePolicyForCategoryResponse.getClass().getDeclaredField("maximumDiscountPrice"),
//                    ratePolicyForCategoryResponse,
//                    i);
//            ReflectionUtils.setField(
//                    ratePolicyForCategoryResponse.getClass().getDeclaredField("expirationPeriodStart"),
//                    ratePolicyForCategoryResponse,
//                    LocalDateTime.of(2024,1,i,12,0));
//            ReflectionUtils.setField(
//                    ratePolicyForCategoryResponse.getClass().getDeclaredField("expirationPeriodEnd"),
//                    ratePolicyForCategoryResponse,
//                    LocalDateTime.of(2024,1,i,12,0));
//            ReflectionUtils.setField(
//                    ratePolicyForCategoryResponse.getClass().getDeclaredField("name"),
//                    ratePolicyForCategoryResponse,
//                    Integer.toString(i));
//            ReflectionUtils.setField(
//                    ratePolicyForCategoryResponse.getClass().getDeclaredField("description"),
//                    ratePolicyForCategoryResponse,
//                    Integer.toString(i));
//            ReflectionUtils.setField(
//                    ratePolicyForCategoryResponse.getClass().getDeclaredField("categoryName"),
//                    ratePolicyForCategoryResponse,
//                    Integer.toString(i));
//            ReflectionUtils.setField(
//                    ratePolicyForCategoryResponse.getClass().getDeclaredField("policyStatusName"),
//                    ratePolicyForCategoryResponse,
//                    Integer.toString(i));
//
//            list.add(ratePolicyForCategoryResponse);
//        }
//
//        Mockito.when(policyService.getRatePoliciesForCategory(1)).thenReturn(list);
//        String url = "http://localhost:"+port +"/task/policies/rate/category";
//        mockMvc.perform(get(url).param("page","1"))
//                .andExpect(jsonPath("$[0].id").value(1))
//                .andExpect(jsonPath("$[1].id").value(2))
//                .andExpect(jsonPath("$[2].id").value(3))
//                .andExpect(jsonPath("$[3].id").value(4))
//                .andExpect(jsonPath("$[4].id").value(5))
//                .andExpect(jsonPath("$[5].id").value(6))
//                .andExpect(jsonPath("$[6].id").value(7))
//                .andExpect(jsonPath("$[7].id").value(8))
//                .andExpect(jsonPath("$[8].id").value(9))
//                .andExpect(jsonPath("$[9].id").value(10));
//    }
//
//    @Test
//    @DisplayName("관리자가 - 등록되어있는 - 정액정책 for Book들을 - Pagenation 해서 받아오기")
//    void getPricePoliciesForBookTest() throws Exception {
//
//        List<PricePolicyForBookResponse> list = new ArrayList<>();
//
//        for(int i = 1; i <= 10; i++){
//            PricePolicyForBookResponse pricePolicyForBookResponse = new PricePolicyForBookResponse();
//            for(Field field : pricePolicyForBookResponse.getClass().getDeclaredFields()){
//                field.setAccessible(true);
//            }
//
//            ReflectionUtils.setField(
//                    pricePolicyForBookResponse.getClass().getDeclaredField("id"),
//                    pricePolicyForBookResponse,
//                    (long)i);
//            ReflectionUtils.setField(
//                    pricePolicyForBookResponse.getClass().getDeclaredField("minimumOrderAmount"),
//                    pricePolicyForBookResponse,
//                    i);
//            ReflectionUtils.setField(
//                    pricePolicyForBookResponse.getClass().getDeclaredField("discountPrice"),
//                    pricePolicyForBookResponse,
//                    i);
//            ReflectionUtils.setField(
//                    pricePolicyForBookResponse.getClass().getDeclaredField("expirationPeriodStart"),
//                    pricePolicyForBookResponse,
//                    LocalDateTime.of(2024,1,i,12,0));
//            ReflectionUtils.setField(
//                    pricePolicyForBookResponse.getClass().getDeclaredField("expirationPeriodEnd"),
//                    pricePolicyForBookResponse,
//                    LocalDateTime.of(2024,1,i,12,0));
//            ReflectionUtils.setField(
//                    pricePolicyForBookResponse.getClass().getDeclaredField("name"),
//                    pricePolicyForBookResponse,
//                    Integer.toString(i));
//            ReflectionUtils.setField(
//                    pricePolicyForBookResponse.getClass().getDeclaredField("description"),
//                    pricePolicyForBookResponse,
//                    Integer.toString(i));
//            ReflectionUtils.setField(
//                    pricePolicyForBookResponse.getClass().getDeclaredField("bookName"),
//                    pricePolicyForBookResponse,
//                    Integer.toString(i));
//            ReflectionUtils.setField(
//                    pricePolicyForBookResponse.getClass().getDeclaredField("policyStatusName"),
//                    pricePolicyForBookResponse,
//                    Integer.toString(i));
//
//            list.add(pricePolicyForBookResponse);
//        }
//
//        Mockito.when(policyService.getPricePoliciesForBook(1)).thenReturn(list);
//        String url = "http://localhost:"+port +"/task/policies/price/book";
//        mockMvc.perform(get(url).param("page","1"))
//                .andExpect(jsonPath("$[0].id").value(1))
//                .andExpect(jsonPath("$[1].id").value(2))
//                .andExpect(jsonPath("$[2].id").value(3))
//                .andExpect(jsonPath("$[3].id").value(4))
//                .andExpect(jsonPath("$[4].id").value(5))
//                .andExpect(jsonPath("$[5].id").value(6))
//                .andExpect(jsonPath("$[6].id").value(7))
//                .andExpect(jsonPath("$[7].id").value(8))
//                .andExpect(jsonPath("$[8].id").value(9))
//                .andExpect(jsonPath("$[9].id").value(10));
//    }
//
//    @Test
//    @DisplayName("관리자가 - 등록되어있는 - 정액정책 for Category들을 - Pagenation 해서 받아오기")
//    void getPricePoliciesForCategoryTest() throws Exception {
//
//        List<PricePolicyForCategoryResponse> list = new ArrayList<>();
//
//        for(int i = 1; i <= 10; i++){
//            PricePolicyForCategoryResponse pricePolicyForCategoryResponse = new PricePolicyForCategoryResponse();
//            for(Field field : pricePolicyForCategoryResponse.getClass().getDeclaredFields()){
//                field.setAccessible(true);
//            }
//
//            ReflectionUtils.setField(
//                    pricePolicyForCategoryResponse.getClass().getDeclaredField("id"),
//                    pricePolicyForCategoryResponse,
//                    (long)i);
//            ReflectionUtils.setField(
//                    pricePolicyForCategoryResponse.getClass().getDeclaredField("minimumOrderAmount"),
//                    pricePolicyForCategoryResponse,
//                    i);
//            ReflectionUtils.setField(
//                    pricePolicyForCategoryResponse.getClass().getDeclaredField("discountPrice"),
//                    pricePolicyForCategoryResponse,
//                    i);
//            ReflectionUtils.setField(
//                    pricePolicyForCategoryResponse.getClass().getDeclaredField("expirationPeriodStart"),
//                    pricePolicyForCategoryResponse,
//                    LocalDateTime.of(2024,1,i,12,0));
//            ReflectionUtils.setField(
//                    pricePolicyForCategoryResponse.getClass().getDeclaredField("expirationPeriodEnd"),
//                    pricePolicyForCategoryResponse,
//                    LocalDateTime.of(2024,1,i,12,0));
//            ReflectionUtils.setField(
//                    pricePolicyForCategoryResponse.getClass().getDeclaredField("name"),
//                    pricePolicyForCategoryResponse,
//                    Integer.toString(i));
//            ReflectionUtils.setField(
//                    pricePolicyForCategoryResponse.getClass().getDeclaredField("description"),
//                    pricePolicyForCategoryResponse,
//                    Integer.toString(i));
//            ReflectionUtils.setField(
//                    pricePolicyForCategoryResponse.getClass().getDeclaredField("categoryName"),
//                    pricePolicyForCategoryResponse,
//                    Integer.toString(i));
//            ReflectionUtils.setField(
//                    pricePolicyForCategoryResponse.getClass().getDeclaredField("policyStatusName"),
//                    pricePolicyForCategoryResponse,
//                    Integer.toString(i));
//
//            list.add(pricePolicyForCategoryResponse);
//        }
//
//        Mockito.when(policyService.getPricePoliciesForCategory(1)).thenReturn(list);
//        String url = "http://localhost:"+port +"/task/policies/price/category";
//        mockMvc.perform(get(url).param("page","1"))
//                .andExpect(jsonPath("$[0].id").value(1))
//                .andExpect(jsonPath("$[1].id").value(2))
//                .andExpect(jsonPath("$[2].id").value(3))
//                .andExpect(jsonPath("$[3].id").value(4))
//                .andExpect(jsonPath("$[4].id").value(5))
//                .andExpect(jsonPath("$[5].id").value(6))
//                .andExpect(jsonPath("$[6].id").value(7))
//                .andExpect(jsonPath("$[7].id").value(8))
//                .andExpect(jsonPath("$[8].id").value(9))
//                .andExpect(jsonPath("$[9].id").value(10));
//    }
//
//    @Test
//    @DisplayName("관리자가 - 등록되어있는 - 정률정책 for Book 하나를 - ID로 받아오기")
//    void getRatePolicyForBookTest() throws Exception {
//
//        RatePolicyForBookResponse ratePolicyForBookResponse =
//                new RatePolicyForBookResponse(
//                        1L,
//                        10,
//                        10000,
//                        2000,
//                        LocalDateTime.of(2024,1,1,1,1),
//                        LocalDateTime.of(2024,1,10,1,1),
//                        "테스트용 정률정책 for Book",
//                        "테스트용 정률정책 for Book",
//                        "테스트용 도서",
//                        "테스트용 Isbn13",
//                        "테스트용 정책상태"
//                );
//        Mockito.when(policyService.getRatePolicyForBook(1L)).thenReturn(ratePolicyForBookResponse);
//
//        mockMvc.perform(get("/task/policies/rate/book/{id}", 1))
//                .andExpect(jsonPath("$.id").value(1L))
//                .andExpect(jsonPath("$.discountRate").value(10))
//                .andExpect(jsonPath("$.minimumOrderAmount").value(10000))
//                .andExpect(jsonPath("$.maximumDiscountPrice").value(2000))
//                .andExpect(jsonPath("$.expirationPeriodStart")
//                        .value("2024-01-01T01:01:00"))
//                .andExpect(jsonPath("$.expirationPeriodEnd")
//                        .value("2024-01-10T01:01:00"))
//                .andExpect(jsonPath("$.name").value("테스트용 정률정책 for Book"))
//                .andExpect(jsonPath("$.description").value("테스트용 정률정책 for Book"))
//                .andExpect(jsonPath("$.bookName").value("테스트용 도서"))
//                .andExpect(jsonPath("$.policyStatusName").value("테스트용 정책상태"));
//    }
//
//    @Test
//    @DisplayName("관리자가 - 등록되어있는 - 정률정책 for Category 하나를 - ID로 받아오기")
//    void getRatePolicyForCategoryTest() throws Exception {
//
//        RatePolicyForCategoryResponse ratePolicyForCategoryResponse =
//                new RatePolicyForCategoryResponse(
//                        1L,
//                        10,
//                        10000,
//                        2000,
//                        LocalDateTime.of(2024,1,1,1,1),
//                        LocalDateTime.of(2024,1,10,1,1),
//                        "테스트용 정률정책 for Category",
//                        "테스트용 정률정책 for Category",
//                        "테스트용 카테고리",
//                        "테스트용 정책상태"
//                );
//        Mockito.when(policyService.getRatePolicyForCategory(1L)).thenReturn(ratePolicyForCategoryResponse);
//
//        mockMvc.perform(get("/task/policies/rate/category/{id}", 1))
//                .andExpect(jsonPath("$.id").value(1L))
//                .andExpect(jsonPath("$.discountRate").value(10))
//                .andExpect(jsonPath("$.minimumOrderAmount").value(10000))
//                .andExpect(jsonPath("$.maximumDiscountPrice").value(2000))
//                .andExpect(jsonPath("$.expirationPeriodStart")
//                        .value("2024-01-01T01:01:00"))
//                .andExpect(jsonPath("$.expirationPeriodEnd")
//                        .value("2024-01-10T01:01:00"))
//                .andExpect(jsonPath("$.name").value("테스트용 정률정책 for Category"))
//                .andExpect(jsonPath("$.description").value("테스트용 정률정책 for Category"))
//                .andExpect(jsonPath("$.categoryName").value("테스트용 카테고리"))
//                .andExpect(jsonPath("$.policyStatusName").value("테스트용 정책상태"));
//    }
//
//    @Test
//    @DisplayName("관리자가 - 등록되어있는 - 정액정책 for Book 하나를 - ID로 받아오기")
//    void getPricePolicyForBookTest() throws Exception {
//
//        PricePolicyForBookResponse pricePolicyForBookResponse =
//                new PricePolicyForBookResponse(
//                        1L,
//                        10000,
//                        1000,
//                        LocalDateTime.of(2024,1,1,1,1),
//                        LocalDateTime.of(2024,1,10,1,1),
//                        "테스트용 정액정책 for Book",
//                        "테스트용 정액정책 for Book",
//                        "테스트용 도서",
//                        "테스트용 Isbn13",
//                        "테스트용 정책상태"
//                );
//        Mockito.when(policyService.getPricePolicyForBook(1L)).thenReturn(pricePolicyForBookResponse);
//
//        mockMvc.perform(get("/task/policies/price/book/{id}", 1))
//                .andExpect(jsonPath("$.id").value(1L))
//                .andExpect(jsonPath("$.minimumOrderAmount").value(10000))
//                .andExpect(jsonPath("$.discountPrice").value(1000))
//                .andExpect(jsonPath("$.expirationPeriodStart")
//                        .value("2024-01-01T01:01:00"))
//                .andExpect(jsonPath("$.expirationPeriodEnd")
//                        .value("2024-01-10T01:01:00"))
//                .andExpect(jsonPath("$.name").value("테스트용 정액정책 for Book"))
//                .andExpect(jsonPath("$.description").value("테스트용 정액정책 for Book"))
//                .andExpect(jsonPath("$.bookName").value("테스트용 도서"))
//                .andExpect(jsonPath("$.policyStatusName").value("테스트용 정책상태"));
//    }
//
//    @Test
//    @DisplayName("관리자가 - 등록되어있는 - 정액정책 for Category 하나를 - ID로 받아오기")
//    void getPricePolicyForCategoryTest() throws Exception {
//
//        PricePolicyForCategoryResponse pricePolicyForCategoryResponse =
//                new PricePolicyForCategoryResponse(
//                        1L,
//                        10000,
//                        1000,
//                        LocalDateTime.of(2024,1,1,1,1),
//                        LocalDateTime.of(2024,1,10,1,1),
//                        "테스트용 정액정책 for Category",
//                        "테스트용 정액정책 for Category",
//                        "테스트용 도서",
//                        "테스트용 정책상태"
//                );
//        Mockito.when(policyService.getPricePolicyForCategory(1L)).thenReturn(pricePolicyForCategoryResponse);
//
//        mockMvc.perform(get("/task/policies/price/category/{id}", 1))
//                .andExpect(jsonPath("$.id").value(1L))
//                .andExpect(jsonPath("$.minimumOrderAmount").value(10000))
//                .andExpect(jsonPath("$.discountPrice").value(1000))
//                .andExpect(jsonPath("$.expirationPeriodStart")
//                        .value("2024-01-01T01:01:00"))
//                .andExpect(jsonPath("$.expirationPeriodEnd")
//                        .value("2024-01-10T01:01:00"))
//                .andExpect(jsonPath("$.name").value("테스트용 정액정책 for Category"))
//                .andExpect(jsonPath("$.description").value("테스트용 정액정책 for Category"))
//                .andExpect(jsonPath("$.categoryName").value("테스트용 도서"))
//                .andExpect(jsonPath("$.policyStatusName").value("테스트용 정책상태"));
//    }
//
//    @Test
//    @DisplayName("관리자가 - 등록되어있는 - 정률정책 for Book 하나를 - 삭제하기")
//    void deleteRatePolicyForBookTest() throws Exception {
//        RatePolicyForBookResponse ratePolicyForBookResponse =
//                new RatePolicyForBookResponse(
//                        1L,
//                        10,
//                        10000,
//                        2000,
//                        LocalDateTime.of(2024,1,1,1,1),
//                        LocalDateTime.of(2024,1,10,1,1),
//                        "테스트용 정률정책 for Book",
//                        "테스트용 정률정책 for Book",
//                        "테스트용 도서",
//                        "테스트용 Isbn13",
//                        "삭제됨"
//                );
//        Mockito.when(policyService.deleteRatePolicyForBook(1L)).thenReturn(ratePolicyForBookResponse);
//
//        mockMvc.perform(delete("/task/policies/rate/book/{id}",1L))
//                .andExpect(jsonPath("$.id").value(1L))
//                .andExpect(jsonPath("$.discountRate").value(10))
//                .andExpect(jsonPath("$.minimumOrderAmount").value(10000))
//                .andExpect(jsonPath("$.maximumDiscountPrice").value(2000))
//                .andExpect(jsonPath("$.expirationPeriodStart")
//                        .value("2024-01-01T01:01:00"))
//                .andExpect(jsonPath("$.expirationPeriodEnd")
//                        .value("2024-01-10T01:01:00"))
//                .andExpect(jsonPath("$.name").value("테스트용 정률정책 for Book"))
//                .andExpect(jsonPath("$.description").value("테스트용 정률정책 for Book"))
//                .andExpect(jsonPath("$.bookName").value("테스트용 도서"))
//                .andExpect(jsonPath("$.bookIsbn13").value("테스트용 Isbn13"))
//                .andExpect(jsonPath("$.policyStatusName").value("삭제됨"));
//    }
//
//    @Test
//    @DisplayName("관리자가 - 등록되어있는 - 정률정책 for Category 하나를 - 삭제하기")
//    void deleteRatePolicyForCategoryTest() throws Exception {
//        RatePolicyForCategoryResponse ratePolicyForCategoryResponse =
//                new RatePolicyForCategoryResponse(
//                        1L,
//                        10,
//                        10000,
//                        2000,
//                        LocalDateTime.of(2024,1,1,1,1),
//                        LocalDateTime.of(2024,1,10,1,1),
//                        "테스트용 정률정책 for Category",
//                        "테스트용 정률정책 for Category",
//                        "테스트용 카테고리",
//                        "삭제됨"
//                );
//        Mockito.when(policyService.deleteRatePolicyForCategory(1L)).thenReturn(ratePolicyForCategoryResponse);
//
//        mockMvc.perform(delete("/task/policies/rate/category/{id}",1L))
//                .andExpect(jsonPath("$.id").value(1L))
//                .andExpect(jsonPath("$.discountRate").value(10))
//                .andExpect(jsonPath("$.minimumOrderAmount").value(10000))
//                .andExpect(jsonPath("$.maximumDiscountPrice").value(2000))
//                .andExpect(jsonPath("$.expirationPeriodStart")
//                        .value("2024-01-01T01:01:00"))
//                .andExpect(jsonPath("$.expirationPeriodEnd")
//                        .value("2024-01-10T01:01:00"))
//                .andExpect(jsonPath("$.name").value("테스트용 정률정책 for Category"))
//                .andExpect(jsonPath("$.description").value("테스트용 정률정책 for Category"))
//                .andExpect(jsonPath("$.categoryName").value("테스트용 카테고리"))
//                .andExpect(jsonPath("$.policyStatusName").value("삭제됨"));
//    }
//
//    @Test
//    @DisplayName("관리자가 - 등록되어있는 - 정액정책 for Book 하나를 - 삭제하기")
//    void deletePricePolicyForBookTest() throws Exception {
//        PricePolicyForBookResponse pricePolicyForBookResponse =
//                new PricePolicyForBookResponse(
//                        1L,
//                        10000,
//                        2000,
//                        LocalDateTime.of(2024,1,1,1,1),
//                        LocalDateTime.of(2024,1,10,1,1),
//                        "테스트용 정액정책 for Book",
//                        "테스트용 정액정책 for Book",
//                        "테스트용 도서",
//                        "테스트용 Isbn13",
//                        "삭제됨"
//                );
//        Mockito.when(policyService.deletePricePolicyForBook(1L)).thenReturn(pricePolicyForBookResponse);
//        mockMvc.perform(delete("/task/policies/price/book/{id}",1L))
//                .andExpect(jsonPath("$.id").value(1L))
//                .andExpect(jsonPath("$.discountPrice").value(2000))
//                .andExpect(jsonPath("$.minimumOrderAmount").value(10000))
//                .andExpect(jsonPath("$.expirationPeriodStart")
//                        .value("2024-01-01T01:01:00"))
//                .andExpect(jsonPath("$.expirationPeriodEnd")
//                        .value("2024-01-10T01:01:00"))
//                .andExpect(jsonPath("$.name").value("테스트용 정액정책 for Book"))
//                .andExpect(jsonPath("$.description").value("테스트용 정액정책 for Book"))
//                .andExpect(jsonPath("$.bookName").value("테스트용 도서"))
//                .andExpect(jsonPath("$.bookIsbn13").value("테스트용 Isbn13"))
//                .andExpect(jsonPath("$.policyStatusName").value("삭제됨"));
//    }
//
//    @Test
//    @DisplayName("관리자가 - 등록되어있는 - 정액정책 for Category 하나를 - 삭제하기")
//    void deletePricePolicyForCategoryTest() throws Exception {
//        PricePolicyForCategoryResponse pricePolicyForCategoryResponse =
//                new PricePolicyForCategoryResponse(
//                        1L,
//                        10000,
//                        2000,
//                        LocalDateTime.of(2024,1,1,1,1),
//                        LocalDateTime.of(2024,1,10,1,1),
//                        "테스트용 정액정책 for Category",
//                        "테스트용 정액정책 for Category",
//                        "테스트용 카테고리",
//                        "삭제됨"
//                );
//        Mockito.when(policyService.deletePricePolicyForCategory(1L)).thenReturn(pricePolicyForCategoryResponse);
//        mockMvc.perform(delete("/task/policies/price/category/{id}",1L))
//                .andExpect(jsonPath("$.id").value(1L))
//                .andExpect(jsonPath("$.discountPrice").value(2000))
//                .andExpect(jsonPath("$.minimumOrderAmount").value(10000))
//                .andExpect(jsonPath("$.expirationPeriodStart")
//                        .value("2024-01-01T01:01:00"))
//                .andExpect(jsonPath("$.expirationPeriodEnd")
//                        .value("2024-01-10T01:01:00"))
//                .andExpect(jsonPath("$.name").value("테스트용 정액정책 for Category"))
//                .andExpect(jsonPath("$.description").value("테스트용 정액정책 for Category"))
//                .andExpect(jsonPath("$.categoryName").value("테스트용 카테고리"))
//                .andExpect(jsonPath("$.policyStatusName").value("삭제됨"));
//    }
//}