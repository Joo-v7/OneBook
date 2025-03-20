package com.nhnacademy.taskapi.cart.controller;

import com.nhnacademy.taskapi.cart.dto.CartItemRequestDto;
import com.nhnacademy.taskapi.cart.dto.CartItemResponseDto;
import com.nhnacademy.taskapi.cart.dto.CartRequestDto;
import com.nhnacademy.taskapi.cart.dto.CartResponseDto;
import com.nhnacademy.taskapi.cart.service.CartService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(controllers= CartController.class)
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @Test
    @DisplayName("GET Cart by memberId")
    void getCartByMemberIdTest() throws Exception {
        CartItemResponseDto cartItemResponseDto1 = new CartItemResponseDto(1L, 1);
        CartItemResponseDto cartItemResponseDto2 = new CartItemResponseDto(2L, 2);
        List<CartItemResponseDto> cartItemResponseDtoList = Arrays.asList(cartItemResponseDto1, cartItemResponseDto2);

        CartResponseDto cartResponseDto = new CartResponseDto("cart-id", 100L, cartItemResponseDtoList);

        Mockito.when(cartService.getCartByMemberId(100L)).thenReturn(cartResponseDto);

        mockMvc.perform(get("/task/carts")
                .header("X-MEMBER-ID", 100L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartId").value("cart-id"))
                .andExpect(jsonPath("$.memberId").value(100L))
                .andExpect(jsonPath("$.cartItemResponseDtoList[0].bookId").value(1L))
                .andExpect(jsonPath("$.cartItemResponseDtoList[0].quantity").value(1))
                .andExpect(jsonPath("$.cartItemResponseDtoList[1].bookId").value(2L))
                .andExpect(jsonPath("$.cartItemResponseDtoList[1].quantity").value(2));
    }

    @Test
    @DisplayName("GET Cart by memberId Failed - nonMember")
    void getCartByMemberIdErrorTest() throws Exception {
        mockMvc.perform(get("/task/carts"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("GET Cart by cartId")
    void getCartTest() throws Exception {
        CartItemResponseDto cartItemResponseDto1 = new CartItemResponseDto(1L, 1);
        CartItemResponseDto cartItemResponseDto2 = new CartItemResponseDto(2L, 2);
        List<CartItemResponseDto> cartItemResponseDtoList = Arrays.asList(cartItemResponseDto1, cartItemResponseDto2);

        CartResponseDto cartResponseDto = new CartResponseDto("cart-id", 100L, cartItemResponseDtoList);

        Mockito.when(cartService.getCartById("cart-id")).thenReturn(cartResponseDto);

        mockMvc.perform(get("/task/carts/cart-id"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartId").value("cart-id"))
                .andExpect(jsonPath("$.memberId").value(100L))
                .andExpect(jsonPath("$.cartItemResponseDtoList[0].bookId").value(1L))
                .andExpect(jsonPath("$.cartItemResponseDtoList[0].quantity").value(1))
                .andExpect(jsonPath("$.cartItemResponseDtoList[1].bookId").value(2L))
                .andExpect(jsonPath("$.cartItemResponseDtoList[1].quantity").value(2));
    }

    @Test
    @DisplayName("POST Cart 1 - member")
    void registerCartTest1() throws Exception {
        CartItemResponseDto cartItemResponseDto1 = new CartItemResponseDto(1L, 1);
        CartItemResponseDto cartItemResponseDto2 = new CartItemResponseDto(2L, 2);
        List<CartItemResponseDto> cartItemResponseDtoList = Arrays.asList(cartItemResponseDto1, cartItemResponseDto2);
        CartResponseDto cartResponseDto = new CartResponseDto("cart-id", 100L, cartItemResponseDtoList);

        CartItemRequestDto cartItemRequestDto1 = new CartItemRequestDto(1L, 1);
        CartItemRequestDto cartItemRequestDto2 = new CartItemRequestDto(2L, 2);
        List<CartItemRequestDto> cartItemRequestDtoList = Arrays.asList(cartItemRequestDto1, cartItemRequestDto2);
        CartRequestDto cartRequestDto = new CartRequestDto(cartItemRequestDtoList);

        Mockito.when(cartService.registerMemberCart("cart-id", 100L ,cartRequestDto)).thenReturn(cartResponseDto);

        mockMvc.perform(post("/task/carts/{id}", "cart-id")
                        .header("X-MEMBER-ID", 100L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"cartItemRequestDtoList\": [\n" +
                                "    {\n" +
                                "      \"bookId\": 1,\n" +
                                "      \"quantity\": 1\n" +
                                "    },\n" +
                                "    {\n" +
                                "      \"bookId\": 2,\n" +
                                "      \"quantity\": 2\n" +
                                "    }\n" +
                                "  ]\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartId").value("cart-id"))
                .andExpect(jsonPath("$.memberId").value(100L))
                .andExpect(jsonPath("$.cartItemResponseDtoList[0].bookId").value(1L));
    }

    @Test
    @DisplayName("POST Cart 2 - nonMember")
    void registerCartTest2() throws Exception {
        CartItemResponseDto cartItemResponseDto1 = new CartItemResponseDto(1L, 1);
        CartItemResponseDto cartItemResponseDto2 = new CartItemResponseDto(2L, 2);
        List<CartItemResponseDto> cartItemResponseDtoList = Arrays.asList(cartItemResponseDto1, cartItemResponseDto2);
        CartResponseDto cartResponseDto = new CartResponseDto("cart-id", 100L, cartItemResponseDtoList);

        CartItemRequestDto cartItemRequestDto1 = new CartItemRequestDto(1L, 1);
        CartItemRequestDto cartItemRequestDto2 = new CartItemRequestDto(2L, 2);
        List<CartItemRequestDto> cartItemRequestDtoList = Arrays.asList(cartItemRequestDto1, cartItemRequestDto2);
        CartRequestDto cartRequestDto = new CartRequestDto(cartItemRequestDtoList);

        Mockito.when(cartService.registerNonMemberCart("cart-id", cartRequestDto)).thenReturn(cartResponseDto);

        mockMvc.perform(post("/task/carts/{id}", "cart-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"cartItemRequestDtoList\": [\n" +
                                "    {\n" +
                                "      \"bookId\": 1,\n" +
                                "      \"quantity\": 1\n" +
                                "    },\n" +
                                "    {\n" +
                                "      \"bookId\": 2,\n" +
                                "      \"quantity\": 2\n" +
                                "    }\n" +
                                "  ]\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartId").value("cart-id"))
                .andExpect(jsonPath("$.cartItemResponseDtoList[0].bookId").value(1L));
    }

    @Test
    @DisplayName("PUT Cart")
    void modifyCartTest() throws Exception {
        CartItemRequestDto cartItemRequestDto1 = new CartItemRequestDto(1L, 1);
        CartItemRequestDto cartItemRequestDto2 = new CartItemRequestDto(2L, 2);
        List<CartItemRequestDto> cartItemRequestDtoList = Arrays.asList(cartItemRequestDto1, cartItemRequestDto2);
        CartRequestDto cartRequestDto = new CartRequestDto(cartItemRequestDtoList);

        CartItemResponseDto cartItemResponseDto1 = new CartItemResponseDto(1L, 1);
        CartItemResponseDto cartItemResponseDto2 = new CartItemResponseDto(2L, 2);
        List<CartItemResponseDto> cartItemResponseDtoList = Arrays.asList(cartItemResponseDto1, cartItemResponseDto2);
        CartResponseDto cartResponseDto = new CartResponseDto("cart-id", 100L, cartItemResponseDtoList);


        Mockito.when(cartService.modifyCart("cart-id", cartRequestDto)).thenReturn(cartResponseDto);

        mockMvc.perform(put("/task/carts/{id}", "cart-id")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"cartItemRequestDtoList\": [\n" +
                        "    {\n" +
                        "      \"bookId\": 1,\n" +
                        "      \"quantity\": 1\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"bookId\": 2,\n" +
                        "      \"quantity\": 2\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartId").value("cart-id"))
                .andExpect(jsonPath("$.cartItemResponseDtoList[0].bookId").value(1L));
    }

    @Test
    @DisplayName("DELETE Cart")
    void removeCartTest() throws Exception {
        mockMvc.perform(delete("/task/carts/{id}", "cart-id"))
                .andExpect(status().is2xxSuccessful());

        Mockito.verify(cartService, Mockito.times(1)).removeCart("cart-id");
    }

}
