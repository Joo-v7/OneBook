package com.nhnacademy.taskapi.cart.service;

import com.nhnacademy.taskapi.book.domain.Book;
import com.nhnacademy.taskapi.book.repository.BookRepository;
import com.nhnacademy.taskapi.cart.domain.Cart;
import com.nhnacademy.taskapi.cart.domain.CartItem;
import com.nhnacademy.taskapi.cart.dto.CartItemRequestDto;
import com.nhnacademy.taskapi.cart.dto.CartRequestDto;
import com.nhnacademy.taskapi.cart.dto.CartResponseDto;
import com.nhnacademy.taskapi.cart.exception.CartIllegalArgumentException;
import com.nhnacademy.taskapi.cart.repository.CartRepository;
import com.nhnacademy.taskapi.cart.service.impl.CartServiceImpl;
import com.nhnacademy.taskapi.grade.domain.Grade;
import com.nhnacademy.taskapi.member.domain.Member;
import com.nhnacademy.taskapi.member.repository.MemberRepository;
import com.nhnacademy.taskapi.publisher.domain.Publisher;
import com.nhnacademy.taskapi.roles.domain.Role;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    @Test
    @DisplayName("Get Cart by Id")
    void getCartByIdTest() {
        Cart cart = new Cart("550e8400-e29b-41d4-a716-446655440000", null, null);
        Mockito.when(cartRepository.findById("550e8400-e29b-41d4-a716-446655440000")).thenReturn(Optional.of(cart));

        CartResponseDto result = cartService.getCartById("550e8400-e29b-41d4-a716-446655440000");

        Mockito.verify(cartRepository, Mockito.times(1)).findById("550e8400-e29b-41d4-a716-446655440000");

        Assertions.assertThat(result.cartId()).isEqualTo("550e8400-e29b-41d4-a716-446655440000");
        Assertions.assertThat(result.memberId()).isEqualTo(null);
        assertThrows(NoSuchElementException.class, () -> result.cartItemResponseDtoList().getFirst());
    }

    @Test
    @DisplayName("Get Cart by MemberId")
    void getCartByMemberIdTest() {
        Grade grade = Grade.create("ROYAL", 10, "일반 등급");
        Role role = Role.createRole("MEMBER", "일반 회원");
        Member member = Member.createNewMember(grade, "김주혁", "joo", "jjjjjjjjjj", LocalDate.now(), Member.Gender.M, "helloworld@gmail.com", "010-1111-1111", role);

        Publisher publisher = new Publisher();
        publisher.setName("joo");

        Book book1 = new Book();
        book1.setBookId(1L);
        book1.setTitle("test");
        book1.setPrice(100);
        book1.setContent("test");
        book1.setAmount(10);
        book1.setDescription("test");
        book1.setPubdate(LocalDate.now());
        book1.setIsbn13("1111111111111");
        book1.setSalePrice(500);
        book1.setPublisher(publisher);

        Cart cart = new Cart("550e8400-e29b-41d4-a716-446655440000", member);
        CartItem cartItem = new CartItem(cart, book1, 2);
        cart.setCartItems(Arrays.asList(cartItem));

        Mockito.when(cartRepository.findCartByMemberId(Mockito.anyLong())).thenReturn(Optional.of(cart));

        CartResponseDto result = cartService.getCartByMemberId(Mockito.anyLong());

        Mockito.verify(cartRepository, Mockito.times(1)).findCartByMemberId(Mockito.anyLong());

        Assertions.assertThat(result.cartId()).isEqualTo("550e8400-e29b-41d4-a716-446655440000");
        Assertions.assertThat(result.cartItemResponseDtoList().getFirst().bookId()).isEqualTo(1L);
        Assertions.assertThat(result.cartItemResponseDtoList().getFirst().quantity()).isEqualTo(2);

    }

    @Test
    @DisplayName("Register NonMember Cart")
    void registerNonMemberCartTest() {
        Publisher publisher = new Publisher();
        publisher.setName("joo");

        Book book1 = new Book();
        book1.setBookId(1L);
        book1.setTitle("test");
        book1.setPrice(100);
        book1.setContent("test");
        book1.setAmount(10);
        book1.setDescription("test");
        book1.setPubdate(LocalDate.now());
        book1.setIsbn13("1111111111111");
        book1.setSalePrice(500);
        book1.setPublisher(publisher);

        List<CartItemRequestDto> cartItemRequestDtoList = Arrays.asList(new CartItemRequestDto(1L, 2));
        CartRequestDto cartRequestDto = new CartRequestDto(cartItemRequestDtoList);

        Cart cart = new Cart("550e8400-e29b-41d4-a716-446655440000");

        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        Mockito.when(cartRepository.save(Mockito.any())).thenReturn(cart);

        CartResponseDto result = cartService.registerNonMemberCart("550e8400-e29b-41d4-a716-446655440000", cartRequestDto);

        Mockito.verify(cartRepository, Mockito.times(1)).save(Mockito.any());

        Assertions.assertThat(result.cartId()).isEqualTo("550e8400-e29b-41d4-a716-446655440000");
        Assertions.assertThat(result.memberId()).isEqualTo(null);
        Assertions.assertThat(result.cartItemResponseDtoList().getFirst().bookId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Register Member Cart")
    void registerMemberCartTest() {
        Grade grade = Grade.create("ROYAL", 10, "일반 등급");
        Role role = Role.createRole("MEMBER", "일반 회원");
        Member member = Member.createNewMember(grade, "김주혁", "joo", "jjjjjjjjjj", LocalDate.now(), Member.Gender.M, "helloworld@gmail.com", "010-1111-1111", role);

        Publisher publisher = new Publisher();
        publisher.setName("joo");

        Book book1 = new Book();
        book1.setBookId(1L);
        book1.setTitle("test");
        book1.setPrice(100);
        book1.setContent("test");
        book1.setAmount(10);
        book1.setDescription("test");
        book1.setPubdate(LocalDate.now());
        book1.setIsbn13("1111111111111");
        book1.setSalePrice(500);
        book1.setPublisher(publisher);

        Cart cart = new Cart("550e8400-e29b-41d4-a716-446655440000", member);

        List<CartItemRequestDto> cartItemRequestDtoList = Arrays.asList(new CartItemRequestDto(1L, 2));
        CartRequestDto cartRequestDto = new CartRequestDto(cartItemRequestDtoList);

        Mockito.when(memberRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(member));
        Mockito.when(cartRepository.save(Mockito.any())).thenReturn(cart);
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
//        Mockito.when(cart.getMember().getId()).thenReturn(11L);

        CartResponseDto result = cartService.registerMemberCart("550e8400-e29b-41d4-a716-446655440000", Mockito.anyLong(), cartRequestDto);

        Mockito.verify(memberRepository, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(cartRepository, Mockito.times(1)).save(Mockito.any());

//        Assertions.assertThat(result.memberId()).isEqualTo(11L);
        Assertions.assertThat(result.cartId()).isEqualTo("550e8400-e29b-41d4-a716-446655440000");
        Assertions.assertThat(result.cartItemResponseDtoList().getFirst().bookId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Modify Cart(update cartItems)")
    void modifyCartTest() {
        Publisher publisher = new Publisher();
        publisher.setName("joo");

        Book book1 = new Book();
        book1.setBookId(1L);
        book1.setTitle("test");
        book1.setPrice(100);
        book1.setContent("test");
        book1.setAmount(10);
        book1.setDescription("test");
        book1.setPubdate(LocalDate.now());
        book1.setIsbn13("1111111111111");
        book1.setSalePrice(500);
        book1.setPublisher(publisher);

        Book book2 = new Book();
        book2.setBookId(10L);
        book2.setTitle("test");
        book2.setPrice(100);
        book2.setContent("test");
        book2.setAmount(10);
        book2.setDescription("test");
        book2.setPubdate(LocalDate.now());
        book2.setIsbn13("1111111111111");
        book2.setSalePrice(500);
        book2.setPublisher(publisher);

        Cart cart = new Cart("550e8400-e29b-41d4-a716-446655440000");
        CartItem cartItem = new CartItem(cart, book1, 2);
        cart.setCartItems(Arrays.asList(cartItem));

        List<CartItemRequestDto> cartItemRequestDtoList = Arrays.asList(new CartItemRequestDto(10L, 10));
        CartRequestDto cartRequestDto = new CartRequestDto(cartItemRequestDtoList);

        Mockito.when(cartRepository.findById("550e8400-e29b-41d4-a716-446655440000")).thenReturn(Optional.of(cart));
        Mockito.when(bookRepository.findById(10L)).thenReturn(Optional.of(book2));

        CartResponseDto result = cartService.modifyCart("550e8400-e29b-41d4-a716-446655440000", cartRequestDto);

        Mockito.verify(cartRepository, Mockito.times(1)).findById("550e8400-e29b-41d4-a716-446655440000");
        Mockito.verify(bookRepository, Mockito.times(1)).findById(10L);

        Assertions.assertThat(result.cartId()).isEqualTo("550e8400-e29b-41d4-a716-446655440000");
        Assertions.assertThat(result.cartItemResponseDtoList().getFirst().bookId()).isEqualTo(10L);
        Assertions.assertThat(result.cartItemResponseDtoList().getFirst().quantity()).isEqualTo(10);
    }

    @Test
    @DisplayName("Remove Cart")
    void removeCartTest() {
        Publisher publisher = new Publisher();
        publisher.setName("joo");

        Book book1 = new Book();
        book1.setBookId(1L);
        book1.setTitle("test");
        book1.setPrice(100);
        book1.setContent("test");
        book1.setAmount(10);
        book1.setDescription("test");
        book1.setPubdate(LocalDate.now());
        book1.setIsbn13("1111111111111");
        book1.setSalePrice(500);
        book1.setPublisher(publisher);

        Cart cart = new Cart("550e8400-e29b-41d4-a716-446655440000");
        CartItem cartItem = new CartItem(cart, book1, 2);
        cart.setCartItems(Arrays.asList(cartItem));

        Mockito.when(cartRepository.existsById("550e8400-e29b-41d4-a716-446655440000")).thenReturn(true);

        cartService.removeCart("550e8400-e29b-41d4-a716-446655440000");

    }

    @Test
    @DisplayName("Remove Cart error")
    void removeCartErrorTest() {
        Mockito.when(cartRepository.existsById("hello")).thenReturn(false);

        assertThrows(CartIllegalArgumentException.class, ()->cartService.removeCart("hello"));
    }

}
