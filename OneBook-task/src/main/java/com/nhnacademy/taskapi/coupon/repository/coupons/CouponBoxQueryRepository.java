package com.nhnacademy.taskapi.coupon.repository.coupons;

import com.nhnacademy.taskapi.book.domain.Book;
import com.nhnacademy.taskapi.book.domain.BookCategory;
import com.nhnacademy.taskapi.book.domain.QBookCategory;
import com.nhnacademy.taskapi.category.domain.Category;
import com.nhnacademy.taskapi.coupon.domain.entity.coupons.Coupon;
import com.nhnacademy.taskapi.coupon.domain.entity.coupons.IssuedCoupon;
import com.nhnacademy.taskapi.coupon.domain.entity.coupons.QIssuedCoupon;
import com.nhnacademy.taskapi.coupon.domain.entity.policies.*;
import com.nhnacademy.taskapi.coupon.domain.entity.status.CouponStatus;
import com.nhnacademy.taskapi.coupon.exception.CouponHasNoPolicyExceptioin;
import com.nhnacademy.taskapi.member.domain.Member;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@Repository
@RequiredArgsConstructor
public class CouponBoxQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Page<IssuedCoupon> getValidIssuedCoupon(Long memberId, CouponStatus couponStatus, Pageable pageable){

        QIssuedCoupon issuedCoupon = QIssuedCoupon.issuedCoupon;

        List<IssuedCoupon> content = jpaQueryFactory
                .selectFrom(issuedCoupon)
                .where(
                        issuedCoupon.member.id.eq(memberId),
                        issuedCoupon.useDateTime.isNull(),
                        issuedCoupon.coupon.couponStatus.ne(couponStatus)
                ).offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .selectFrom(issuedCoupon)
                .where(
                        issuedCoupon.member.id.eq(memberId),
                        issuedCoupon.useDateTime.isNull(),
                        issuedCoupon.coupon.couponStatus.ne(couponStatus)
                ).offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .stream().count();

        return new PageImpl<>(content,pageable,total);
    }

    public List<IssuedCoupon> getIssuedCouponForValidForBookByMemberId(Member member, Book book, CouponStatus couponStatus){

        // 사용자가 가지고있는 발급-사용가능 쿠폰중에서

        // 주문한 책에 대해서
        // 이책을 대상으로 하는 정률쿠폰
        // 이책을 대상으로 하는 정액쿠폰
        // 이책의 카테고리를 대상으로 하는 정률 쿠폰
        // 이책의 카테고리를 대상으로 하는 정률 쿠폰

        // 을 모두 가져와야한다

        QIssuedCoupon issuedCoupon = QIssuedCoupon.issuedCoupon;
        QBookCategory bookCategory = QBookCategory.bookCategory;

        List<BookCategory> bookCategories = jpaQueryFactory
                .selectFrom(bookCategory)
                .where(
                        bookCategory.book.eq(book)
                ).fetch();

        List<Category> categories = new ArrayList<>();
        for(BookCategory element : bookCategories){
            categories.add(element.getCategory());
        }

        // 사용자가 가지고 있는 발급-사용가능 쿠폰 긁어오기
        List<IssuedCoupon> allIssuedCoupons = jpaQueryFactory
                .selectFrom(issuedCoupon)
                .where(
                        issuedCoupon.member.eq(member),
                        issuedCoupon.useDateTime.isNull(),
                        issuedCoupon.coupon.couponStatus.eq(couponStatus)
                ).fetch();

        List<IssuedCoupon> selectedCoupons = new ArrayList<>();

        for(IssuedCoupon element : allIssuedCoupons){
            if(Objects.nonNull(element.getCoupon().getRatePolicyForBook())){
                if(element.getCoupon().getRatePolicyForBook().getBook().equals(book)){
                   selectedCoupons.add(element);
                }
            }
            else if(Objects.nonNull(element.getCoupon().getRatePolicyForCategory())){
                for(Category category : categories){
                    if(element.getCoupon().getRatePolicyForCategory().getCategory().equals(category)){
                        selectedCoupons.add(element);
                    }
                }
            }
            else if(Objects.nonNull(element.getCoupon().getPricePolicyForBook())){
                if(element.getCoupon().getPricePolicyForBook().getBook().equals(book)){
                    selectedCoupons.add(element);
                }
            }
            else if(Objects.nonNull(element.getCoupon().getPricePolicyForCategory())){
                for(Category category : categories){
                    if(element.getCoupon().getPricePolicyForCategory().getCategory().equals(category)){
                        selectedCoupons.add(element);
                    }
                }
            }else{
                throw new CouponHasNoPolicyExceptioin("쿠폰에 정책이 없습니다, 잘못된 쿠폰입니다");
            }

        }

        return selectedCoupons;
    }



    public boolean checkDuplicatedIssueRateCouponForBook(Member member , Policy policy){

        QIssuedCoupon issuedCoupon = QIssuedCoupon.issuedCoupon;

        long count = jpaQueryFactory
                .selectFrom(issuedCoupon)
                .where(
                        issuedCoupon.member.eq(member),
                        issuedCoupon.coupon.ratePolicyForBook.isNotNull(),
                        issuedCoupon.coupon.ratePolicyForBook.eq((RatePolicyForBook) policy)
                ).stream().count();

        return count <= 0 ;
    }

    public boolean checkDuplicatedIssueRateCouponForCategory(Member member , Policy policy){

        QIssuedCoupon issuedCoupon = QIssuedCoupon.issuedCoupon;

        long count = jpaQueryFactory
                .selectFrom(issuedCoupon)
                .where(
                        issuedCoupon.member.eq(member),
                        issuedCoupon.coupon.ratePolicyForCategory.isNotNull(),
                        issuedCoupon.coupon.ratePolicyForCategory.eq((RatePolicyForCategory) policy)
                ).stream().count();

        return count <= 0 ;
    }

    public boolean checkDuplicatedIssuePriceCouponForBook(Member member , Policy policy){

        QIssuedCoupon issuedCoupon = QIssuedCoupon.issuedCoupon;

        long count = jpaQueryFactory
                .selectFrom(issuedCoupon)
                .where(
                        issuedCoupon.member.eq(member),
                        issuedCoupon.coupon.pricePolicyForBook.isNotNull(),
                        issuedCoupon.coupon.pricePolicyForBook.eq((PricePolicyForBook) policy)
                ).stream().count();

        return count <= 0 ;
    }

    public boolean checkDuplicatedIssuePriceCouponForCategory(Member member , Policy policy){

        QIssuedCoupon issuedCoupon = QIssuedCoupon.issuedCoupon;

        long count = jpaQueryFactory
                .selectFrom(issuedCoupon)
                .where(
                        issuedCoupon.member.eq(member),
                        issuedCoupon.coupon.pricePolicyForCategory.isNotNull(),
                        issuedCoupon.coupon.pricePolicyForCategory.eq((PricePolicyForCategory) policy)
                ).stream().count();

        return count <= 0 ;
    }
}
