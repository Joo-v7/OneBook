package com.nhnacademy.taskapi.book.repository;

import com.nhnacademy.taskapi.Tag.domain.QTag;
import com.nhnacademy.taskapi.author.domain.QAuthor;
import com.nhnacademy.taskapi.book.domain.Book;
import com.nhnacademy.taskapi.book.domain.QBook;
import com.nhnacademy.taskapi.book.domain.QBookAuthor;
import com.nhnacademy.taskapi.book.domain.QBookTag;
import com.nhnacademy.taskapi.book.dto.BookRecommendDto;
import com.nhnacademy.taskapi.book.dto.BookSearchAllResponse;
import com.nhnacademy.taskapi.like.domain.QLike;
import com.nhnacademy.taskapi.publisher.domain.QPublisher;
import com.nhnacademy.taskapi.review.domain.QReview;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BookRepositoryCustomImpl implements BookRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QBook book = QBook.book;
    QAuthor author = QAuthor.author;
    QBookAuthor bookAuthor = QBookAuthor.bookAuthor;
    QBookTag bookTag = QBookTag.bookTag;
    QTag tag = QTag.tag;
    QPublisher publisher = QPublisher.publisher;
    QLike like = QLike.like;
    QReview review = QReview.review;

    @Override
    public List<BookSearchAllResponse> findBookByTitle(String searchString) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(book.title.containsIgnoreCase(searchString));
        builder.or(book.description.containsIgnoreCase(searchString));
        builder.or(bookAuthor.author.name.containsIgnoreCase(searchString));
        builder.or(bookTag.tag.name.containsIgnoreCase(searchString));
        builder.or(publisher.name.containsIgnoreCase(searchString));

        return queryFactory
                .select(Projections.constructor(
                        BookSearchAllResponse.class,
                        book.bookId,
                        book.title,
                        book.publisher.name,
                        book.description,
                        book.price,
                        book.salePrice,
                        book.amount,
                        book.pubdate,
                        book.status
                ))
                .from(book)
                .leftJoin(bookAuthor).on(bookAuthor.book.eq(book))
                .leftJoin(bookAuthor.author, author)
                .leftJoin(bookTag).on(bookTag.book.eq(book))
                .leftJoin(bookTag.tag, tag)
                .leftJoin(publisher).on(book.publisher.eq(publisher))
                .where(builder)
                .fetch();

    }

    @Override
    public List<BookRecommendDto> reconmmendBooks() {
        return queryFactory.select(Projections.constructor(
                BookRecommendDto.class,
                book.bookId,
                book.title
                ))
                .from(book)
                .leftJoin(like).on(like.book.eq(book))
                .leftJoin(review).on(review.book.eq(book))
                .groupBy(book.bookId)
                .orderBy(
                        like.count().desc(),
                        review.count().desc()
                )
                .limit(4)
                .fetch();
    }
}