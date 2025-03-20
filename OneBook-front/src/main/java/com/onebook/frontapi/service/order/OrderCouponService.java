package com.onebook.frontapi.service.order;

import com.onebook.frontapi.dto.book.BookDTO;
import com.onebook.frontapi.dto.coupon.response.coupon.IssuedCouponFeignResponse;
import com.onebook.frontapi.dto.coupon.response.coupon.IssuedCouponResponse;
import com.onebook.frontapi.dto.order.OrderCouponResponse;
import com.onebook.frontapi.feign.book.BookClient;
import com.onebook.frontapi.feign.coupon.CouponBoxClient;
import com.onebook.frontapi.service.book.BookService;
import com.onebook.frontapi.service.coupon.CouponBoxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderCouponService {

    private final CouponBoxService couponBoxService;
    private final BookClient bookClient;

    public List<OrderCouponResponse> getCoupon(Long bookId) {

        List<OrderCouponResponse> couponList = new ArrayList<>();

        List<IssuedCouponResponse> couponResponses = couponBoxService.getIssuedCouponsValidForBookByMemberId(bookId);
        if (couponResponses.isEmpty()) {
            return couponList;
        }

        for (IssuedCouponResponse couponResponse : couponResponses) {
            // 할인가격 결정
            int salePrice = bookClient.getBookById(bookId).getSalePrice();
            if (couponResponse.getCouponType().startsWith("ratePolicy")) {
                salePrice *= (couponResponse.getDiscountRate() * 0.0001);
                log.info("!!!!! : {}", salePrice);
                salePrice *= 100;
                log.info("!!!!! : {}", salePrice);
            } else if (couponResponse.getCategoryName().startsWith("pricePolicy")) {
                salePrice = couponResponse.getDiscountPrice();
            }
            couponList.add(new OrderCouponResponse(
                    couponResponse.getCouponNumber(),
                    couponResponse.getName(),
                    salePrice,
                    couponResponse.getExpirationPeriodEnd().toLocalDate()
            ));
        }

        return couponList;
    }
}
