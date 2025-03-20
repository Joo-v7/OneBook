package com.nhnacademy.taskapi.coupon.service.coupons;

import com.nhnacademy.taskapi.book.domain.Book;
import com.nhnacademy.taskapi.book.exception.BookNotFoundException;
import com.nhnacademy.taskapi.book.repository.BookRepository;
import com.nhnacademy.taskapi.coupon.domain.dto.coupons.request.IssueCouponToMemberRequest;
import com.nhnacademy.taskapi.coupon.domain.dto.coupons.response.CouponResponse;
import com.nhnacademy.taskapi.coupon.domain.dto.coupons.response.IssuedCouponResponse;
import com.nhnacademy.taskapi.coupon.domain.entity.coupons.Coupon;
import com.nhnacademy.taskapi.coupon.domain.entity.coupons.IssuedCoupon;
import com.nhnacademy.taskapi.coupon.domain.entity.status.CouponStatus;
import com.nhnacademy.taskapi.coupon.exception.AlreadyIssuedCouponException;
import com.nhnacademy.taskapi.coupon.exception.CouponHasNoPolicyExceptioin;
import com.nhnacademy.taskapi.coupon.exception.CouponNotFoundException;
import com.nhnacademy.taskapi.coupon.repository.coupons.CouponBoxQueryRepository;
import com.nhnacademy.taskapi.coupon.repository.coupons.CouponBoxRepository;
import com.nhnacademy.taskapi.coupon.repository.coupons.CouponRepository;
import com.nhnacademy.taskapi.coupon.repository.status.CouponStatusRepository;
import com.nhnacademy.taskapi.coupon.service.policies.PolicyService;
import com.nhnacademy.taskapi.member.domain.Member;
import com.nhnacademy.taskapi.member.exception.MemberNotFoundException;
import com.nhnacademy.taskapi.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CouponBoxService {

    private final CouponBoxRepository couponBoxRepository;
    private final CouponBoxQueryRepository couponBoxQueryRepository;
    private final CouponRepository couponRepository;
    private final MemberRepository memberRepository;

    private final CouponService couponService;
    private final CouponStatusRepository couponStatusRepository;

    private final PolicyService policyService;
    private final BookRepository bookRepository;

    @Transactional
    public IssuedCouponResponse issueCouponToMember(Long memberId,
                                                    IssueCouponToMemberRequest issueCouponToMemberRequest){

        Member member = memberRepository.
                findById(memberId).orElseThrow
                        (()->new MemberNotFoundException("해당하는 ID의 멤버가 존재하지 않습니다"));

        Coupon coupon = couponRepository.findByCouponNumber(
                issueCouponToMemberRequest.getCouponNumber())
                .orElseThrow(()->new CouponNotFoundException("해당하는 번호의 쿠폰을 찾을 수 없습니다"));

        if(checkDuplicatedIssue(coupon,member)){
            IssuedCoupon issuedCoupon = couponBoxRepository.save(IssuedCoupon.createIssuedCoupon(coupon,member));
            CouponStatus issuedStatus = couponStatusRepository.findByName("발급-삭제가능");
            coupon.changeIssuedStatus(issuedStatus);
            return IssuedCouponResponse.changeEntityToDto(issuedCoupon);
        }
        else{
            throw new AlreadyIssuedCouponException("해당 사용자는 이미 동일한 정책의 쿠폰을 발급받았습니다");
        }
    }

    @Transactional
    public IssuedCouponResponse issueWelcomeCouponToMember(String loginId){

        CouponResponse couponResponse = couponService.createWelcomeCoupon();

        Coupon welcomeCoupon = couponRepository
                .findByCouponNumber(couponResponse.getCouponNumber())
                .orElseThrow(()->new CouponNotFoundException("해당하는 번호의 쿠폰을 찾을 수 없습니다"));

        Member newMember = memberRepository.findByLoginId(loginId)
                .orElseThrow(()->new MemberNotFoundException("해당하는 로그인 아이디의 회원을 찾을 수 없습니다"));

        IssuedCoupon issuedCoupon =
                couponBoxRepository.save(IssuedCoupon.createIssuedCoupon(welcomeCoupon,newMember));

        policyService.deleteRatePolicyForCategory(welcomeCoupon.getRatePolicyForCategory().getRatePolicyForCategoryId());

        return IssuedCouponResponse.changeEntityToDto(issuedCoupon);
    }

    @Transactional
    public Page<IssuedCouponResponse> getIssuedCouponsByMemberId(Pageable pageable, Long memberId){

        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new MemberNotFoundException("해당하는 로그인 아이디의 회원을 찾을 수 없습니다"));

        CouponStatus couponStatus = couponStatusRepository.findByName("발급-삭제불가");

        Page<IssuedCoupon> couponsOfMember = couponBoxQueryRepository.getValidIssuedCoupon(member.getId(),couponStatus,pageable);

        return couponsOfMember.map(IssuedCouponResponse::changeEntityToDto);
    }

    @Transactional
    public List<IssuedCouponResponse> getIssuedCouponsValidForBookByMemberId(Long memberId, Long bookId){

        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new MemberNotFoundException("해당하는 로그인 아이디의 회원을 찾을 수 없습니다"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("해당하는 아이디의 book을 찾을 수 없습니다"));

        CouponStatus couponStatus = couponStatusRepository.findByName("발급-삭제가능");

        List<IssuedCoupon> couponsValidForBookOfMember
                = couponBoxQueryRepository.getIssuedCouponForValidForBookByMemberId(member,book,couponStatus);

        return couponsValidForBookOfMember.stream().map(IssuedCouponResponse::changeEntityToDto).toList();

    }

    private boolean checkDuplicatedIssue(Coupon coupon, Member member){
        if(coupon.getRatePolicyForBook() != null){
           return couponBoxQueryRepository.checkDuplicatedIssueRateCouponForBook(member,coupon.getRatePolicyForBook());
        }
        if(coupon.getRatePolicyForCategory() != null){
            return couponBoxQueryRepository.checkDuplicatedIssueRateCouponForCategory(member,coupon.getRatePolicyForCategory());
        }
        if(coupon.getPricePolicyForBook() != null){
            return couponBoxQueryRepository.checkDuplicatedIssuePriceCouponForBook(member,coupon.getPricePolicyForBook());
        }
        if(coupon.getPricePolicyForCategory() != null){
            return couponBoxQueryRepository.checkDuplicatedIssuePriceCouponForCategory(member,coupon.getPricePolicyForCategory());
        }

        throw new CouponHasNoPolicyExceptioin("쿠폰이 어떠한 정책도 가지고 있지 않습니다, 잘못된 쿠폰입니다");
    }

    /**
     * 수정자 : 김선준
     * 수정일 : 2025.01.21(화)
     * 수정내용 : IssuedCoupon 에 setter 추가.
     *          updateIssuedCoupon 메서드 추가.
    */
    @Transactional
    public void updateIssuedCoupon(String couponNumber){
        IssuedCoupon issuedCoupon = couponBoxRepository.findByCoupon_CouponNumber(couponNumber);
        issuedCoupon.setUseDateTime(LocalDateTime.now());
    }
}
