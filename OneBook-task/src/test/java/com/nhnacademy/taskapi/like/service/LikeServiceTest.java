package com.nhnacademy.taskapi.like.service;

import com.nhnacademy.taskapi.book.domain.Book;
import com.nhnacademy.taskapi.book.exception.BookNotFoundException;
import com.nhnacademy.taskapi.book.service.BookService;
import com.nhnacademy.taskapi.book.service.Impl.BookServiceImpl;
import com.nhnacademy.taskapi.grade.domain.Grade;
import com.nhnacademy.taskapi.like.domain.Like;
import com.nhnacademy.taskapi.like.dto.LikePlusMinusDTO;
import com.nhnacademy.taskapi.like.exception.LikeNotFoundException;
import com.nhnacademy.taskapi.like.repository.LikeRepository;
import com.nhnacademy.taskapi.like.service.Impl.LikeServiceImpl;
import com.nhnacademy.taskapi.member.domain.Member;
import com.nhnacademy.taskapi.member.exception.MemberNotFoundException;
import com.nhnacademy.taskapi.member.repository.MemberRepository;
import com.nhnacademy.taskapi.member.service.impl.MemberServiceImpl;
import com.nhnacademy.taskapi.roles.domain.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LikeServiceTest {

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private MemberServiceImpl memberService;

    /**
     * 수정일: 2024/12/31
     * 수정자: 김주혁
     * 수정 내용: memberRepository 추가.
     */
    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BookServiceImpl bookService;

    @InjectMocks
    private LikeServiceImpl likeService;

//    @Test
//    @DisplayName("plusLike_Success")
//    void plusLike_Success(){
//        Book book = new Book();
//        book.setBookId(1L);
//
//        Grade grade = Grade.create("ROYAL", 10, "일반 등급");
//        Role role = Role.createRole("MEMBER", "일반 회원");
//        Member member = Member.createNewMember(grade, "김주혁", "joo", "jjjjjjjjjj", LocalDate.now(), Member.Gender.M, "helloworld@gmail.com", "010-1111-1111", role);
//
//        when(bookService.getBook(any(Long.class))).thenReturn(book);
//        /**
//         * 수정일: 2024/12/31
//         * 수정자: 김주혁
//         * 수정 내용: 기존 내용 주석처리, memberRepository로 member return.
//         */
//        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.of(member));
////        when(memberService.getMemberById(any(Long.class))).thenReturn(member);
//
//        Like likes = new Like();
//        likes.setBook(book);
//        likes.setMember(member);
//
//        when(likeRepository.save(any(Like.class))).thenReturn(likes);
//
//        LikePlusMinusDTO dto = new LikePlusMinusDTO();
//        dto.setBookId(1L);
//        dto.setMemberId(1L);
//
//        Like result = likeService.plusLike(dto);
//        Assertions.assertNotNull(result);
//
//        verify(likeRepository).save(any(Like.class));
//    }

//    @Test
//    @DisplayName("plusLike_Fail_BookNotFound")
//    void plusLike_Fail_BookNotFound(){
//        when(bookService.getBook(any(Long.class))).thenReturn(null);
//        Assertions.assertThrows(BookNotFoundException.class, () -> likeService.plusLike(new LikePlusMinusDTO()) );
//    }

//    @Test
//    @DisplayName("plusLike_Fail_MemberNotFound")
//    void plusLike_Fail_MemberNotFound(){
//        Book book = new Book();
//        book.setBookId(1L);
//
//        when(bookService.getBook(any(Long.class))).thenReturn(book);
//        /**
//         * 수정일: 2024/12/31
//         * 수정자: 김주혁
//         * 수정 내용: 기존 내용 주석 처리, memberService.getMemberById() -> memberRepository.findById()
//         */
////        when(memberService.getMemberById(any(Long.class))).thenReturn(null);
//        when(memberRepository.findById(any(Long.class))).thenThrow(MemberNotFoundException.class);
//
//        Assertions.assertThrows(MemberNotFoundException.class, () -> likeService.plusLike(new LikePlusMinusDTO()) );
//    }

    @Test
    @DisplayName("minusLike_Success")
    void minusLike_Success(){
        Like like = new Like();

        when(likeRepository.findByBook_BookIdAndMember_Id(any(Long.class), any(Long.class))).thenReturn(like);

        likeService.minusLike(new LikePlusMinusDTO());

        verify(likeRepository).delete(like);
    }

    @Test
    @DisplayName("minusLike_Fail_NotFound")
    void minusLike_Fail_NotFound(){
        when(likeRepository.findByBook_BookIdAndMember_Id(any(Long.class), any(Long.class))).thenReturn(null);

        Assertions.assertThrows(LikeNotFoundException.class, () -> likeService.minusLike(new LikePlusMinusDTO()) );
    }
}
