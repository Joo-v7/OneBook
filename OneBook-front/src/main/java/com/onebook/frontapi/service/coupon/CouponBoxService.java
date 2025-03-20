package com.onebook.frontapi.service.coupon;

import com.onebook.frontapi.dto.coupon.request.coupon.FindCouponsByPolicyIdRequest;
import com.onebook.frontapi.dto.coupon.request.coupon.IssueCouponToMemberRequest;
import com.onebook.frontapi.dto.coupon.response.coupon.CouponResponse;
import com.onebook.frontapi.dto.coupon.response.coupon.IssuedCouponFeignResponse;
import com.onebook.frontapi.dto.coupon.response.coupon.IssuedCouponResponse;
import com.onebook.frontapi.dto.coupon.response.couponPolicy.PricePolicyForBookResponse;
import com.onebook.frontapi.dto.coupon.response.couponPolicy.PricePolicyForCategoryResponse;
import com.onebook.frontapi.dto.coupon.response.couponPolicy.RatePolicyForBookResponse;
import com.onebook.frontapi.dto.coupon.response.couponPolicy.RatePolicyForCategoryResponse;
import com.onebook.frontapi.feign.coupon.CouponBoxClient;
import com.onebook.frontapi.feign.coupon.CouponClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CouponBoxService {

    private final CouponBoxClient couponBoxClient;
    private final CouponClient couponClient;
    private final CouponPolicyService couponPolicyService;
    private final CouponService couponService;

    private final Integer PAGE_SIZE = 10;

    public Page<IssuedCouponResponse> getIssuedCouponsByMemberId(int pageNo){

        Pageable pageable = PageRequest.of(pageNo, PAGE_SIZE, Sort.by("issue_date_time").descending());

        Page<IssuedCouponFeignResponse> page = couponBoxClient.getIssuedCouponsByMemberId(pageable).getBody();

        List<IssuedCouponResponse> issuedCouponResponseList = new ArrayList<>();

        for(IssuedCouponFeignResponse issuedCouponFeignResponse :  page.getContent()){

            // 단순 IssuedCouponResponse들을 정책 정보까지 담은 정보로 변환해줌
            IssuedCouponResponse issuedCouponResponse = makeIssuedCouponResponseContainingPolicyInfo(issuedCouponFeignResponse);
            issuedCouponResponseList.add(issuedCouponResponse);
        }


        Page<IssuedCouponResponse> issuedCouponResponsePage =
                new PageImpl<>(issuedCouponResponseList,pageable,page.getTotalPages());

            return issuedCouponResponsePage;
    }


    public IssuedCouponResponse issueCoupon(FindCouponsByPolicyIdRequest findCouponsByPolicyIdRequest){

        // 쿠폰 번호가 필요함 (정책id로 해당하는 쿠폰들 쫙 받아온다음에, 그중에 하나 랜덤으로 사용자에게 발급)
        String couponNumber;

        if(findCouponsByPolicyIdRequest.getCouponType().equals("정률정책/책")){
            List<CouponResponse> rateCouponsForBook = couponService.getRateCouponsForBookByPolicyId(findCouponsByPolicyIdRequest.getPolicyId());
            Collections.shuffle(rateCouponsForBook);
            couponNumber = rateCouponsForBook.getFirst().getCouponNumber();

        }
        else if(findCouponsByPolicyIdRequest.getCouponType().equals("정률정책/카테고리")){
            List<CouponResponse> rateCouponsForCategory = couponService.getRateCouponsForCategoryByPolicyId(findCouponsByPolicyIdRequest.getPolicyId());
            Collections.shuffle(rateCouponsForCategory);
            couponNumber = rateCouponsForCategory.getFirst().getCouponNumber();
        }
        else if(findCouponsByPolicyIdRequest.getCouponType().equals("정액정책/책")){
            List<CouponResponse> priceCouponsForBook = couponService.getPriceCouponsForBookByPolicyId(findCouponsByPolicyIdRequest.getPolicyId());
            Collections.shuffle(priceCouponsForBook);
            couponNumber = priceCouponsForBook.getFirst().getCouponNumber();

        }else if(findCouponsByPolicyIdRequest.getCouponType().equals("정액정책/카테고리")){
            List<CouponResponse> priceCouponsForCategory = couponService.getPriceCouponsForCategoryByPolicyId(findCouponsByPolicyIdRequest.getPolicyId());
            Collections.shuffle(priceCouponsForCategory);
            couponNumber = priceCouponsForCategory.getFirst().getCouponNumber();
        }
        else{
            throw new RuntimeException("해당하는 타입의 쿠폰은 존재하지 않습니다");
        }
        // 정률정책이면, 정액정책이면...

        IssueCouponToMemberRequest issueCouponToMemberRequest = new IssueCouponToMemberRequest(couponNumber);

        return couponBoxClient.issueCouponToMember(issueCouponToMemberRequest).getBody();
    }

    //TODO 구매할때 쿠폰적용하려고 쓰는 기능!! (해당 책에 적용가능 and 사용자가 가지고 있는 and 사용 가능한 쿠폰목록 가지고옴)
    public List<IssuedCouponResponse> getIssuedCouponsValidForBookByMemberId(Long bookId){

        List<IssuedCouponFeignResponse> issuedCouponResponseList =
                couponBoxClient.getIssuedCouponsValidForBookByMemberId(bookId).getBody();

        if(issuedCouponResponseList == null || issuedCouponResponseList.isEmpty()){
            // 조건에 맞는 사용자의 쿠폰이 없다면 빈 arrayList 리턴
            return new ArrayList<>();
        }
        else{
         return issuedCouponResponseList.stream().map(this::makeIssuedCouponResponseContainingPolicyInfo).toList();
        }
    }



    public CouponResponse getCouponByCouponNumber(String couponNumber){
        return couponClient.getCouponByCouponNumber(couponNumber).getBody();
    }

    private IssuedCouponResponse makeIssuedCouponResponseContainingPolicyInfo(IssuedCouponFeignResponse issuedCouponFeignResponse){


        IssuedCouponResponse issuedCouponResponse = new IssuedCouponResponse();

        issuedCouponResponse.setCouponNumber(issuedCouponFeignResponse.getCouponNumber());
        issuedCouponResponse.setIssuedCouponId(issuedCouponFeignResponse.getIssuedCouponId());
        issuedCouponResponse.setMemberId(issuedCouponFeignResponse.getMemberId());
        issuedCouponResponse.setIssueDateTime(issuedCouponFeignResponse.getIssueDateTime());
        issuedCouponResponse.setUseDateTime(issuedCouponFeignResponse.getUseDateTime());

        CouponResponse couponResponse = getCouponByCouponNumber(issuedCouponFeignResponse.getCouponNumber());

        // 쿠폰의 정책 id로 -> 정책을 찾아옴
        if(Objects.nonNull(couponResponse.getPricePolicyForBookId())){

            PricePolicyForBookResponse pricePolicyForBookResponse =
                    couponPolicyService.getPricePolicyForBook(couponResponse.getPricePolicyForBookId());

            issuedCouponResponse.setMinimumOrderAmount(pricePolicyForBookResponse.getMinimumOrderAmount());
            issuedCouponResponse.setDiscountPrice(pricePolicyForBookResponse.getDiscountPrice());
            issuedCouponResponse.setExpirationPeriodStart(pricePolicyForBookResponse.getExpirationPeriodStart());
            issuedCouponResponse.setExpirationPeriodEnd(pricePolicyForBookResponse.getExpirationPeriodEnd());
            issuedCouponResponse.setName(pricePolicyForBookResponse.getName());
            issuedCouponResponse.setDescription(pricePolicyForBookResponse.getDescription());
            issuedCouponResponse.setBookName(pricePolicyForBookResponse.getBookName());
            issuedCouponResponse.setBookIsbn13(pricePolicyForBookResponse.getBookIsbn13());

            issuedCouponResponse.setCouponType("pricePolicyForBook");

        }

        if(Objects.nonNull(couponResponse.getPricePolicyForCategoryId())){

            PricePolicyForCategoryResponse pricePolicyForCategoryResponse =
                    couponPolicyService.getPricePolicyForCategory(couponResponse.getPricePolicyForCategoryId());

            issuedCouponResponse.setMinimumOrderAmount(pricePolicyForCategoryResponse.getMinimumOrderAmount());
            issuedCouponResponse.setDiscountPrice(pricePolicyForCategoryResponse.getDiscountPrice());
            issuedCouponResponse.setExpirationPeriodStart(pricePolicyForCategoryResponse.getExpirationPeriodStart());
            issuedCouponResponse.setExpirationPeriodEnd(pricePolicyForCategoryResponse.getExpirationPeriodEnd());
            issuedCouponResponse.setName(pricePolicyForCategoryResponse.getName());
            issuedCouponResponse.setDescription(pricePolicyForCategoryResponse.getDescription());
            issuedCouponResponse.setCategoryName(pricePolicyForCategoryResponse.getCategoryName());

            issuedCouponResponse.setCouponType("pricePolicyForCategory");

        }

        if(Objects.nonNull(couponResponse.getRatePolicyForBookId())){

            RatePolicyForBookResponse ratePolicyForBookResponse =
                    couponPolicyService.getRatePolicyForBook(couponResponse.getRatePolicyForBookId());

            issuedCouponResponse.setMinimumOrderAmount(ratePolicyForBookResponse.getMinimumOrderAmount());
            issuedCouponResponse.setDiscountRate(ratePolicyForBookResponse.getDiscountRate());
            issuedCouponResponse.setMaximumDiscountPrice(ratePolicyForBookResponse.getMaximumDiscountPrice());
            issuedCouponResponse.setExpirationPeriodStart(ratePolicyForBookResponse.getExpirationPeriodStart());
            issuedCouponResponse.setExpirationPeriodEnd(ratePolicyForBookResponse.getExpirationPeriodEnd());
            issuedCouponResponse.setName(ratePolicyForBookResponse.getName());
            issuedCouponResponse.setDescription(ratePolicyForBookResponse.getDescription());
            issuedCouponResponse.setBookName(ratePolicyForBookResponse.getBookName());
            issuedCouponResponse.setBookIsbn13(ratePolicyForBookResponse.getBookIsbn13());

            issuedCouponResponse.setCouponType("ratePolicyForBook");

        }

        if(Objects.nonNull(couponResponse.getRatePolicyForCategoryId())){

            RatePolicyForCategoryResponse ratePolicyForCategoryResponse =
                    couponPolicyService.getRatePolicyForCategory(couponResponse.getRatePolicyForCategoryId());

            issuedCouponResponse.setMinimumOrderAmount(ratePolicyForCategoryResponse.getMinimumOrderAmount());
            issuedCouponResponse.setDiscountRate(ratePolicyForCategoryResponse.getDiscountRate());
            issuedCouponResponse.setMaximumDiscountPrice(ratePolicyForCategoryResponse.getMaximumDiscountPrice());
            issuedCouponResponse.setExpirationPeriodStart(ratePolicyForCategoryResponse.getExpirationPeriodStart());
            issuedCouponResponse.setExpirationPeriodEnd(ratePolicyForCategoryResponse.getExpirationPeriodEnd());
            issuedCouponResponse.setName(ratePolicyForCategoryResponse.getName());
            issuedCouponResponse.setDescription(ratePolicyForCategoryResponse.getDescription());
            issuedCouponResponse.setCategoryName(ratePolicyForCategoryResponse.getCategoryName());

            issuedCouponResponse.setCouponType("ratePolicyForCategory");

        }

        return issuedCouponResponse;
    }

}
