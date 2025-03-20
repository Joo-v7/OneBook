package com.nhnacademy.taskapi.coupon.service.coupons;

import com.nhnacademy.taskapi.category.domain.Category;
import com.nhnacademy.taskapi.category.repository.CategoryRepository;
import com.nhnacademy.taskapi.coupon.domain.dto.coupons.request.CreateCouponRequest;
import com.nhnacademy.taskapi.coupon.domain.dto.coupons.response.CouponResponse;
import com.nhnacademy.taskapi.coupon.domain.dto.policies.request.create.AddRatePolicyForCategoryRequest;
import com.nhnacademy.taskapi.coupon.domain.entity.coupons.Coupon;
import com.nhnacademy.taskapi.coupon.domain.entity.policies.PricePolicyForBook;
import com.nhnacademy.taskapi.coupon.domain.entity.policies.PricePolicyForCategory;
import com.nhnacademy.taskapi.coupon.domain.entity.policies.RatePolicyForBook;
import com.nhnacademy.taskapi.coupon.domain.entity.policies.RatePolicyForCategory;
import com.nhnacademy.taskapi.coupon.domain.entity.status.CouponStatus;
import com.nhnacademy.taskapi.coupon.domain.entity.status.PolicyStatus;
import com.nhnacademy.taskapi.coupon.exception.CouponCannotDeleteException;
import com.nhnacademy.taskapi.coupon.exception.CouponNotFoundException;
import com.nhnacademy.taskapi.coupon.exception.PolicyNotFoundException;
import com.nhnacademy.taskapi.coupon.repository.coupons.CouponRepository;
import com.nhnacademy.taskapi.coupon.repository.policies.PricePoliciesForBookRepository;
import com.nhnacademy.taskapi.coupon.repository.policies.PricePoliciesForCategoryRepository;
import com.nhnacademy.taskapi.coupon.repository.policies.RatePoliciesForBookRepository;
import com.nhnacademy.taskapi.coupon.repository.policies.RatePoliciesForCategoryRepository;
import com.nhnacademy.taskapi.coupon.repository.status.CouponStatusRepository;
import com.nhnacademy.taskapi.coupon.repository.status.PolicyStatusRepository;
import com.nhnacademy.taskapi.coupon.service.policies.PolicyService;
import com.nhnacademy.taskapi.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Window;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponService {

    private final CouponRepository couponRepository;
    private final CouponStatusRepository couponStatusRepository;
    private final RatePoliciesForBookRepository ratePoliciesForBookRepository;
    private final RatePoliciesForCategoryRepository ratePoliciesForCategoryRepository;
    private final PricePoliciesForBookRepository pricePoliciesForBookRepository;
    private final PricePoliciesForCategoryRepository pricePoliciesForCategoryRepository;

    private final CategoryRepository categoryRepository;
    private final PolicyStatusRepository policyStatusRepository;

    public List<CouponResponse> createRateCouponForBook(CreateCouponRequest createCouponRequest){

        RatePolicyForBook ratePolicyForBook = ratePoliciesForBookRepository.findById(createCouponRequest.getPolicyId())
                .orElseThrow(()-> new PolicyNotFoundException("해당하는 ID의 정책을 찾을 수 없습니다"));

        PolicyStatus policyStatus = policyStatusRepository.findByName("사용됨");
        ratePolicyForBook.usePolicy(policyStatus);

        CouponStatus unUsedStatus = couponStatusRepository.findByName("미발급");
        List<CouponResponse> couponCreateResponses = new ArrayList<>();
        LocalDateTime creationTime = LocalDateTime.now();

        for(int i = 0; i < createCouponRequest.getCount(); i++){
            Coupon coupon = couponRepository.save(Coupon.createRateCouponForBook(ratePolicyForBook, unUsedStatus,creationTime));
            CouponResponse couponResponse = CouponResponse.changeEntityToDto(coupon);
            couponCreateResponses.add(couponResponse);
        }

        return couponCreateResponses;

    }

    public List<CouponResponse> createRateCouponForCategory(CreateCouponRequest createCouponRequest){

        RatePolicyForCategory ratePolicyForCategory = ratePoliciesForCategoryRepository.findById(createCouponRequest.getPolicyId())
                .orElseThrow(()-> new PolicyNotFoundException("해당하는 ID의 정책을 찾을 수 없습니다"));

        PolicyStatus policyStatus = policyStatusRepository.findByName("사용됨");
        ratePolicyForCategory.usePolicy(policyStatus);

        CouponStatus unUsedStatus = couponStatusRepository.findByName("미발급");
        List<CouponResponse> couponCreateResponses = new ArrayList<>();
        LocalDateTime creationTime = LocalDateTime.now();

        for(int i = 0; i < createCouponRequest.getCount(); i++){
            Coupon coupon = couponRepository.save(Coupon.createRateCouponForCategory(ratePolicyForCategory, unUsedStatus,creationTime));
            CouponResponse couponResponse = CouponResponse.changeEntityToDto(coupon);
            couponCreateResponses.add(couponResponse);
        }

        return couponCreateResponses;
    }

    public List<CouponResponse> createPriceCouponForBook(CreateCouponRequest createCouponRequest){

        PricePolicyForBook pricePolicyForBook = pricePoliciesForBookRepository.findById(createCouponRequest.getPolicyId())
                .orElseThrow(()-> new PolicyNotFoundException("해당하는 ID의 정책을 찾을 수 없습니다"));

        PolicyStatus policyStatus = policyStatusRepository.findByName("사용됨");
        pricePolicyForBook.usePolicy(policyStatus);

        CouponStatus unUsedStatus = couponStatusRepository.findByName("미발급");
        List<CouponResponse> couponCreateResponses = new ArrayList<>();
        LocalDateTime creationTime = LocalDateTime.now();

        for(int i = 0; i < createCouponRequest.getCount(); i++){
            Coupon coupon = couponRepository.save(Coupon.createPriceCouponForBook(pricePolicyForBook, unUsedStatus,creationTime));
            CouponResponse couponResponse = CouponResponse.changeEntityToDto(coupon);
            couponCreateResponses.add(couponResponse);
        }

        return couponCreateResponses;
    }

    public List<CouponResponse> createPriceCouponForCategory(CreateCouponRequest createCouponRequest){

        PricePolicyForCategory pricePolicyForCategory = pricePoliciesForCategoryRepository.findById(createCouponRequest.getPolicyId())
                .orElseThrow(()-> new PolicyNotFoundException("해당하는 ID의 정책을 찾을 수 없습니다"));

        PolicyStatus policyStatus = policyStatusRepository.findByName("사용됨");
        pricePolicyForCategory.usePolicy(policyStatus);

        CouponStatus unUsedStatus = couponStatusRepository.findByName("미발급");
        List<CouponResponse> couponCreateResponses = new ArrayList<>();
        LocalDateTime creationTime = LocalDateTime.now();

        for(int i = 0; i < createCouponRequest.getCount(); i++){
            Coupon coupon = couponRepository.save(Coupon.createPriceCouponForCategory(pricePolicyForCategory, unUsedStatus,creationTime));
            CouponResponse couponResponse = CouponResponse.changeEntityToDto(coupon);
            couponCreateResponses.add(couponResponse);
        }

        return couponCreateResponses;
    }

    public Page<CouponResponse> getAllCoupons(Pageable pageable){

        Page<Coupon> coupons = couponRepository.findAll(pageable);
        return coupons.map(CouponResponse::changeEntityToDto);
    }

    public List<CouponResponse> getRateCouponsForBook(Long policyId){

        RatePolicyForBook ratePolicyForBook = ratePoliciesForBookRepository.findById(policyId)
                .orElseThrow(()->new PolicyNotFoundException("해당하는 ID의 정책을 찾을 수 없습니다"));

        CouponStatus couponStatus = couponStatusRepository.findByName("미발급");

        List<Coupon> coupons = couponRepository.findByRatePolicyForBookAndCouponStatus
                (ratePolicyForBook,couponStatus);
        return coupons.stream().map(CouponResponse::changeEntityToDto).toList();
    }

    public List<CouponResponse> getRateCouponsForCategory(Long policyId){

        RatePolicyForCategory ratePolicyForCategory = ratePoliciesForCategoryRepository.findById(policyId)
                .orElseThrow(()->new PolicyNotFoundException("해당하는 ID의 정책을 찾을 수 없습니다"));

        CouponStatus couponStatus = couponStatusRepository.findByName("미발급");

        List<Coupon> coupons = couponRepository.findByRatePolicyForCategoryAndCouponStatus
                (ratePolicyForCategory,couponStatus);

        return coupons.stream().map(CouponResponse::changeEntityToDto).toList();
    }

    public List<CouponResponse> getPriceCouponsForBook(Long policyId){

        PricePolicyForBook pricePolicyForBook = pricePoliciesForBookRepository.findById(policyId)
                .orElseThrow(()->new PolicyNotFoundException("해당하는 ID의 정책을 찾을 수 없습니다"));

        CouponStatus couponStatus = couponStatusRepository.findByName("미발급");

        List<Coupon> coupons = couponRepository.
                findByPricePolicyForBookAndCouponStatus(pricePolicyForBook,couponStatus);

        return coupons.stream().map(CouponResponse::changeEntityToDto).toList();
    }

    public List<CouponResponse> getPriceCouponsForCategory(Long policyId){

        PricePolicyForCategory pricePolicyForCategory = pricePoliciesForCategoryRepository.findById(policyId)
                .orElseThrow(()->new PolicyNotFoundException("해당하는 ID의 정책을 찾을 수 없습니다"));

        CouponStatus couponStatus = couponStatusRepository.findByName("미발급");

        List<Coupon> coupons = couponRepository
                .findByPricePolicyForCategoryAndCouponStatus(pricePolicyForCategory,couponStatus);

        return coupons.stream().map(CouponResponse::changeEntityToDto).toList();
    }

    public CouponResponse getCouponByCouponNumber(String couponNumber){

        Coupon coupon = couponRepository.findByCouponNumber(couponNumber)
                .orElseThrow(()->new CouponNotFoundException("해당하는 쿠폰넘버의 쿠폰이 존재하지 않습니다"));

        return CouponResponse.changeEntityToDto(coupon);

    }

    public CouponResponse deleteCoupon(String couponNumber){
        Coupon coupon = couponRepository.findByCouponNumber(couponNumber).
                orElseThrow(()->new CouponNotFoundException("해당하는 쿠폰넘버의 쿠폰이 존재하지 않습니다"));

        if(coupon.getCouponStatus().getName().equals("발급-삭제불가")){
            throw new CouponCannotDeleteException("해당 쿠폰은 이미 발급되고 사용되어 삭제할 수 없습니다");
        }

        couponRepository.delete(coupon);
        return CouponResponse.changeEntityToDto(coupon);
    }

    public CouponResponse createWelcomeCoupon(){

        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusDays(30);
        PolicyStatus policyStatus = policyStatusRepository.findByName("사용됨");
        CouponStatus couponStatus = couponStatusRepository.findByName("발급-삭제가능");
        Category category = categoryRepository.findByName("국내도서");

        RatePolicyForCategory ratePolicyForCategory =
                ratePoliciesForCategoryRepository.save(
                        new RatePolicyForCategory(
                                10,
                                10000,
                                50000,
                                startDate,
                                endDate,
                                "신규 회원 Welcome Coupon",
                                "신규 회원을 대상으로 한 Welcome Coupon 입니다",
                                category,
                                policyStatus
                        )
                );


        Coupon welcomeCoupon = couponRepository.save(Coupon.createRateCouponForCategory(
                ratePolicyForCategory,
                couponStatus,
                startDate
        ));

        return CouponResponse.changeEntityToDto(welcomeCoupon);
    }

    /**
     * 수정자 : 김선준
     * 수정일 : 2025.01.22(수)
     * 수정내용 : 쿠폰 사용 시 쿠폰 상태 변경하는 기능 추가
     */
    public void updateCoupon(String couponNumber) {
        CouponStatus couponStatus = couponStatusRepository.findByName("발급-삭제불가");

        Coupon coupon = couponRepository.findByCouponNumber(couponNumber)
                .orElseThrow(() -> new CouponNotFoundException(couponNumber));

        coupon.couponStatusChangeToUsed(couponStatus);
    }

}
