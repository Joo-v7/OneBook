package com.nhnacademy.taskapi.cart.repository;

import com.nhnacademy.taskapi.book.domain.Book;
import com.nhnacademy.taskapi.cart.domain.Cart;
import com.nhnacademy.taskapi.cart.domain.CartItem;
import com.nhnacademy.taskapi.config.QuerydslConfig;
import com.nhnacademy.taskapi.grade.domain.Grade;
import com.nhnacademy.taskapi.member.domain.Member;
import com.nhnacademy.taskapi.member.repository.MemberRepository;
import com.nhnacademy.taskapi.publisher.domain.Publisher;
import com.nhnacademy.taskapi.roles.domain.Role;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
@Import(QuerydslConfig.class)

public class CartRepositoryTest {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private CartItemRepository cartItemRepository;

    private Book book1;
    private Book book2;

    @BeforeEach
    void setUp() {
        Grade grade = Grade.create("ROYAL", 10, "일반 등급");
        Grade savedGrade = entityManager.persist(grade);

        Role role = Role.createRole("MEMBER", "일반 회원");
        Role savedRole = entityManager.persist(role);

        Member member = Member.createNewMember(savedGrade, "김주혁", "joo", "jjjjjjjjjj", LocalDate.now(), Member.Gender.M, "helloworld@gmail.com", "010-1111-1111", savedRole);
        entityManager.persist(member);

        Publisher publisher = new Publisher();
        publisher.setName("joo");
        entityManager.persist(publisher);

        book1 = new Book();
        book1.setTitle("test");
        book1.setPrice(100);
        book1.setContent("test");
        book1.setAmount(10);
        book1.setDescription("test");
        book1.setPubdate(LocalDate.now());
        book1.setIsbn13("1111111111111");
        book1.setSalePrice(500);
        book1.setPublisher(publisher);
        entityManager.persist(book1);

        book2 = new Book();
        book2.setTitle("test2");
        book2.setPrice(100);
        book2.setContent("test2");
        book2.setAmount(10);
        book2.setDescription("test2");
        book2.setPubdate(LocalDate.now());
        book2.setIsbn13("1111111111112");
        book2.setSalePrice(500);
        book2.setPublisher(publisher);
        entityManager.persist(book2);

    }

    /**
     * CartItem 없이 Cart만 저장.
     */
    @Test
    @DisplayName("Save and Find Cart - without CartItem")
    void saveAndFindTest1() {
        // 비회원
        Cart nonMemberCart = new Cart("550e8400-e29b-41d4-a716-446655440000");
        Cart savedNonMemberCart = cartRepository.save(nonMemberCart);

        Cart nonMemberCartTarget = cartRepository.findById(nonMemberCart.getId()).get();
        assertThat(savedNonMemberCart.getId()).isEqualTo(nonMemberCartTarget.getId());

        // 회원
        Member member = memberRepository.findByLoginId("joo").get();
        Cart memberCart = new Cart("123e4567-e89b-12d3-a456-426614174000", member);
        Cart savedMemberCart = cartRepository.save(memberCart);

        Cart memberCartTarget = cartRepository.findById(memberCart.getId()).get();
        assertThat(savedMemberCart.getId()).isEqualTo(memberCartTarget.getId()); // cart 확인
        assertThat(savedMemberCart.getMember().getName()).isEqualTo(member.getName()); // member 확인
        assertThat(savedMemberCart.getMember().getLoginId()).isEqualTo(member.getLoginId());
    }

    /**
     * Cart + CartItem 저장.
     */
    @Test
    @DisplayName("Save and Find Cart - with CartItem")
    void saveAndFindTest2() {
        /**
         * 비회원
         */

        // 비회원 카트 생성 후 저장.
        Cart nonMemberCart = new Cart("550e8400-e29b-41d4-a716-446655440000");
        Cart savedNonMemberCart = cartRepository.save(nonMemberCart);

        // CartItem 생성.
        CartItem cartItem = new CartItem(savedNonMemberCart, book1, 1);

        // cart에 CartItem set.
        savedNonMemberCart.setCartItems(Arrays.asList(cartItem));

        Cart nonMemberCartTarget = cartRepository.findById(nonMemberCart.getId()).get();
        assertThat(savedNonMemberCart.getId()).isEqualTo(nonMemberCartTarget.getId()); // cart 확인
        assertThat(savedNonMemberCart.getCartItems().size()).isEqualTo(1); // cartItem 확인



        assertThat(savedNonMemberCart.getCartItems().get(0).getBook().getBookId()).isEqualTo(book1.getBookId());
        assertThat(savedNonMemberCart.getCartItems().get(0).getQuantity()).isEqualTo(1);

        /**
         * 회원
         */

        // 회원 카트 생성 후 저장.
        Member member = memberRepository.findByLoginId("joo").get();
        Cart memberCart = new Cart("123e4567-e89b-12d3-a456-426614174000", member);
        Cart savedMemberCart = cartRepository.save(memberCart);

        // CartItem 생성.
        CartItem cartItem2 = new CartItem(savedNonMemberCart, book2, 2);

        // cart에 CartItem set.
        savedMemberCart.setCartItems(Arrays.asList(cartItem2));

        Cart memberCartTarget = cartRepository.findById(memberCart.getId()).get();
        assertThat(savedMemberCart.getId()).isEqualTo(memberCartTarget.getId()); // cart 확인
        assertThat(savedMemberCart.getMember().getName()).isEqualTo(member.getName()); // member 확인
        assertThat(savedMemberCart.getMember().getLoginId()).isEqualTo(member.getLoginId());
        assertThat(savedMemberCart.getCartItems().size()).isEqualTo(1); // cartItem 확인
        assertThat(savedMemberCart.getCartItems().get(0).getBook().getBookId()).isEqualTo(book2.getBookId());
        assertThat(savedMemberCart.getCartItems().get(0).getQuantity()).isEqualTo(2);
    }

    @Test
    @DisplayName("Delete Cart - without CartItem")
    void deleteCart1() {
        // 비회원
        Cart nonMemberCart = new Cart("550e8400-e29b-41d4-a716-446655440000");
        Cart savedNonMemberCart = cartRepository.save(nonMemberCart);

        cartRepository.delete(savedNonMemberCart);

        Assertions.assertThrows(NoSuchElementException.class, ()-> cartRepository.findById(savedNonMemberCart.getId()).get());

        // 회원
        Member member = memberRepository.findByLoginId("joo").get();
        Cart memberCart = new Cart("123e4567-e89b-12d3-a456-426614174000", member);
        Cart savedMemberCart = cartRepository.save(memberCart);

        cartRepository.delete(savedMemberCart);
        Assertions.assertThrows(NoSuchElementException.class, () -> cartRepository.findById(savedMemberCart.getId()).get());
    }

    @Test
    @DisplayName("Delete Cart - with CartItem")
    void deleteCart2() {
        // 비회원
        Cart nonMemberCart = new Cart("550e8400-e29b-41d4-a716-446655440000");
        Cart savedNonMemberCart = cartRepository.save(nonMemberCart);

        // CartItem 생성.
        CartItem cartItem = new CartItem(savedNonMemberCart, book1, 1);

        // cart에 CartItem set.
        savedNonMemberCart.setCartItems(Arrays.asList(cartItem));

        cartRepository.delete(savedNonMemberCart);
        Assertions.assertThrows(NoSuchElementException.class, ()-> cartRepository.findById(savedNonMemberCart.getId()).get());
        Assertions.assertThrows(NoSuchElementException.class,
                ()-> cartItemRepository.findCartBooksByCartId(savedNonMemberCart.getId()).getFirst()
        );


        // 회원
        // 회원 카트 생성 후 저장.
        Member member = memberRepository.findByLoginId("joo").get();
        Cart memberCart = new Cart("123e4567-e89b-12d3-a456-426614174000", member);
        Cart savedMemberCart = cartRepository.save(memberCart);

        // CartItem 생성.
        CartItem cartItem2 = new CartItem(savedNonMemberCart, book2, 2);

        // cart에 CartItem set.
        savedMemberCart.setCartItems(Arrays.asList(cartItem2));

        cartRepository.delete(savedMemberCart);
        Assertions.assertThrows(NoSuchElementException.class, () -> cartRepository.findById(savedMemberCart.getId()).get());
        Assertions.assertThrows(NoSuchElementException.class,
                ()->cartItemRepository.findCartBooksByCartId(savedMemberCart.getId()).getFirst());

    }
}
