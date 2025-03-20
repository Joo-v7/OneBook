package com.nhnacademy.taskapi.review.repository;

import com.nhnacademy.taskapi.book.domain.Book;
import com.nhnacademy.taskapi.member.domain.Member;
import com.nhnacademy.taskapi.review.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT AVG(r.grade) FROM Review r WHERE r.book.bookId = :bookId")
    Optional<Double> findAverageGradeByBookId(long bookId);

    Optional<Review> findByMemberAndBook(Member member, Book book); // 중복 리뷰 방지 체크

    Page<Review> findByBookBookId(long bookId, Pageable pageable);

    List<Review> findByMemberId(Long memberId);

    int countByBookBookId(long bookId);
}
