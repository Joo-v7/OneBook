package com.nhnacademy.taskapi.coupon.repository.policies;

import com.nhnacademy.taskapi.coupon.domain.entity.policies.*;
import com.nhnacademy.taskapi.coupon.domain.entity.policies.QPricePolicyForBook;
import com.nhnacademy.taskapi.coupon.domain.entity.policies.QPricePolicyForCategory;
import com.nhnacademy.taskapi.coupon.domain.entity.policies.QRatePolicyForBook;
import com.nhnacademy.taskapi.coupon.domain.entity.policies.QRatePolicyForCategory;
import com.nhnacademy.taskapi.coupon.domain.entity.status.PolicyStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PoliciesQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<RatePolicyForBook> getUsingRatePolicyForBook(PolicyStatus policyStatus){

       QRatePolicyForBook ratePolicyForBook = QRatePolicyForBook.ratePolicyForBook;

        List<RatePolicyForBook> content = jpaQueryFactory.selectFrom(ratePolicyForBook)
                .where(
                        ratePolicyForBook.expirationPeriodStart.before(LocalDate.now().atStartOfDay()),
                        ratePolicyForBook.expirationPeriodEnd.after(LocalDate.now().atStartOfDay()),
                        ratePolicyForBook.policyStatus.eq(policyStatus))
                        .fetch();

        return content;
    }

    public List<RatePolicyForCategory> getUsingRatePolicyForCategory(PolicyStatus policyStatus){

        QRatePolicyForCategory ratePolicyForCategory = QRatePolicyForCategory.ratePolicyForCategory;

        List<RatePolicyForCategory> content = jpaQueryFactory.selectFrom(ratePolicyForCategory)
                .where(
                        ratePolicyForCategory.expirationPeriodStart.before(LocalDate.now().atStartOfDay()),
                        ratePolicyForCategory.expirationPeriodEnd.after(LocalDate.now().atStartOfDay()),
                        ratePolicyForCategory.policyStatus.eq(policyStatus))
                .fetch();

        return content;
    }

    public List<PricePolicyForBook> getUsingPricePolicyForBook(PolicyStatus policyStatus){

        QPricePolicyForBook pricePolicyForBook = QPricePolicyForBook.pricePolicyForBook;

        List<PricePolicyForBook> content = jpaQueryFactory.selectFrom(pricePolicyForBook)
                .where(
                        pricePolicyForBook.expirationPeriodStart.before(LocalDate.now().atStartOfDay()),
                        pricePolicyForBook.expirationPeriodEnd.after(LocalDate.now().atStartOfDay()),
                        pricePolicyForBook.policyStatus.eq(policyStatus))
                .fetch();

        return content;
    }

    public List<PricePolicyForCategory> getUsingPricePolicyForCategory(PolicyStatus policyStatus){

        QPricePolicyForCategory pricePolicyForCategory = QPricePolicyForCategory.pricePolicyForCategory;

        List<PricePolicyForCategory> content = jpaQueryFactory.selectFrom(pricePolicyForCategory)
                .where(
                        pricePolicyForCategory.expirationPeriodStart.before(LocalDate.now().atStartOfDay()),
                        pricePolicyForCategory.expirationPeriodEnd.after(LocalDate.now().atStartOfDay()),
                        pricePolicyForCategory.policyStatus.eq(policyStatus))
                .fetch();

        return content;
    }
}
