package com.nhnacademy.taskapi.coupon.service.policies;

import com.nhnacademy.taskapi.book.domain.Book;
import com.nhnacademy.taskapi.book.repository.BookRepository;
import com.nhnacademy.taskapi.book.exception.BookNotFoundException;
import com.nhnacademy.taskapi.category.domain.Category;
import com.nhnacademy.taskapi.category.exception.CategoryNameDuplicateException;
import com.nhnacademy.taskapi.coupon.domain.dto.policies.request.create.AddPricePolicyForCategoryRequest;
import com.nhnacademy.taskapi.coupon.domain.dto.policies.request.update.UpdatePricePolicyForBookRequest;
import com.nhnacademy.taskapi.coupon.domain.dto.policies.request.update.UpdatePricePolicyForCategoryRequest;
import com.nhnacademy.taskapi.coupon.domain.dto.policies.request.update.UpdateRatePolicyForBookRequest;
import com.nhnacademy.taskapi.coupon.domain.dto.policies.request.update.UpdateRatePolicyForCategoryRequest;
import com.nhnacademy.taskapi.coupon.domain.dto.policies.response.*;
import com.nhnacademy.taskapi.coupon.domain.entity.policies.*;
import com.nhnacademy.taskapi.category.exception.CategoryNotFoundException;
import com.nhnacademy.taskapi.category.repository.CategoryRepository;
import com.nhnacademy.taskapi.coupon.domain.dto.policies.request.create.AddPricePolicyForBookRequest;
import com.nhnacademy.taskapi.coupon.domain.dto.policies.request.create.AddRatePolicyForBookRequest;
import com.nhnacademy.taskapi.coupon.domain.dto.policies.request.create.AddRatePolicyForCategoryRequest;
import com.nhnacademy.taskapi.coupon.domain.entity.status.CouponStatus;
import com.nhnacademy.taskapi.coupon.domain.entity.status.PolicyStatus;
import com.nhnacademy.taskapi.coupon.exception.PolicyCannotUpdateException;
import com.nhnacademy.taskapi.coupon.exception.PolicyNotFoundException;
import com.nhnacademy.taskapi.coupon.exception.PolicyStatusNotFoundException;
import com.nhnacademy.taskapi.coupon.repository.coupons.CouponQueryRepository;
import com.nhnacademy.taskapi.coupon.repository.policies.*;
import com.nhnacademy.taskapi.coupon.repository.status.CouponStatusRepository;
import com.nhnacademy.taskapi.coupon.repository.status.PolicyStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PolicyService {

    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;
    private final PolicyStatusRepository policyStatusRepository;
    private final CouponStatusRepository couponStatusRepository;

    private final RatePoliciesForBookRepository ratePoliciesForBookRepository;
    private final RatePoliciesForCategoryRepository ratePoliciesForCategoryRepository;
    private final PricePoliciesForBookRepository pricePoliciesForBookRepository;
    private final PricePoliciesForCategoryRepository pricePoliciesForCategoryRepository;

    private final PoliciesQueryRepository policiesQueryRepository;
    private final CouponQueryRepository couponQueryRepository;


    // 정률정책 for Book save
    public RatePolicyForBookResponse addRatePolicyForBook(AddRatePolicyForBookRequest addRatePolicyForBookRequest){

        RatePolicyForBook ratePolicyForBook = RatePolicyForBook.createRatePolicyForBook(
                addRatePolicyForBookRequest,
                bookRepository.findByIsbn13(addRatePolicyForBookRequest.getBookIsbn13()),
                policyStatusRepository.findByName("미사용")
        );

                ratePoliciesForBookRepository.save(ratePolicyForBook);
                return RatePolicyForBookResponse.changeEntityToDto(ratePolicyForBook);
    }

    // 정률정책 for Category save
    public RatePolicyForCategoryResponse addRatePolicyForCategory(AddRatePolicyForCategoryRequest addRatePolicyForCategoryRequest){

        RatePolicyForCategory ratePolicyForCategory = RatePolicyForCategory.createRatePolicyForCategory(
                addRatePolicyForCategoryRequest,
                categoryRepository.findById(addRatePolicyForCategoryRequest.getCategoryId())
                        .orElseThrow(()->new CategoryNotFoundException("해당하는 ID의 카테고리를 찾을 수 없습니다")),
                policyStatusRepository.findByName("미사용"));


                ratePoliciesForCategoryRepository.save(ratePolicyForCategory);
                return RatePolicyForCategoryResponse.changeEntityToDto(ratePolicyForCategory);
    }

    // 정액정책 for Book save
    public PricePolicyForBookResponse addPricePolicyForBook(AddPricePolicyForBookRequest addPricePolicyForBookRequest){

        PricePolicyForBook pricePolicyForBook = PricePolicyForBook.createPricePolicyForBook(
                addPricePolicyForBookRequest,
                bookRepository.findByIsbn13(addPricePolicyForBookRequest.getBookIsbn13()),
                policyStatusRepository.findByName("미사용")
        );

                pricePoliciesForBookRepository.save(pricePolicyForBook);
                return PricePolicyForBookResponse.changeEntityToDto(pricePolicyForBook);
    }

    // 정액정책 for Category save
    public PricePolicyForCategoryResponse addPricePolicyForCategory(AddPricePolicyForCategoryRequest addPricePolicyForCategoryRequest){

        PricePolicyForCategory pricePolicyForCategory = PricePolicyForCategory.createPricePolicyForCategory(
                addPricePolicyForCategoryRequest,
                categoryRepository.findById(addPricePolicyForCategoryRequest.getCategoryId())
                        .orElseThrow(()->new CategoryNotFoundException("해당하는 ID의 카테고리를 찾을 수 없습니다")),
                policyStatusRepository.findByName("미사용"));

                pricePoliciesForCategoryRepository.save(pricePolicyForCategory);
                return PricePolicyForCategoryResponse.changeEntityToDto(pricePolicyForCategory);
    }

    // 정률정책 for Book read (all)
    public Page<RatePolicyForBookResponse> getRatePoliciesForBook(Pageable pageable){


//        Page<RatePolicyForBook> page = ratePoliciesForBookRepository.findAll(pageable);

        Page<RatePolicyForBook> page = ratePoliciesForBookRepository.
                findByPolicyStatus_NameOrPolicyStatus_Name("미사용","사용됨",pageable);

//        List<RatePolicyForBookResponse> result = new ArrayList<>();
//
//        for(RatePolicyForBook ratePolicyForBook : page){
//            result.add(RatePolicyForBookResponse.changeEntityToDto(ratePolicyForBook));
//        }

        return page.map(RatePolicyForBookResponse::changeEntityToDto);
    }

    // 정률정책 for Category read (all)
    public Page<RatePolicyForCategoryResponse> getRatePoliciesForCategory(Pageable pageable){

//        Page<RatePolicyForCategory> page = ratePoliciesForCategoryRepository.findAll(pageable);
        Page<RatePolicyForCategory> page = ratePoliciesForCategoryRepository.
                findByPolicyStatus_NameOrPolicyStatus_Name("미사용","사용됨",pageable);

//        List<RatePolicyForCategoryResponse> result = new ArrayList<>();
//        for(RatePolicyForCategory ratePolicyForCategory : page){
//            result.add(RatePolicyForCategoryResponse.changeEntityToDto(ratePolicyForCategory));
//        }
        return page.map(RatePolicyForCategoryResponse::changeEntityToDto);
    }

    // 정액정책 for Book read (all)
    public Page<PricePolicyForBookResponse> getPricePoliciesForBook(Pageable pageable){

//        Page<PricePolicyForBook> page = pricePoliciesForBookRepository.findAll(pageable);

        Page<PricePolicyForBook> page = pricePoliciesForBookRepository.
                findByPolicyStatus_NameOrPolicyStatus_Name("미사용","사용됨",pageable);

//        List<PricePolicyForBookResponse> result = new ArrayList<>();
//        for(PricePolicyForBook pricePolicyForBook : page){
//            result.add(PricePolicyForBookResponse.changeEntityToDto(pricePolicyForBook));
//        }

        return page.map(PricePolicyForBookResponse::changeEntityToDto);
    }

    // 정액정책 for Category read (all)
    public Page<PricePolicyForCategoryResponse> getPricePoliciesForCategory(Pageable pageable){

        Page<PricePolicyForCategory> page = pricePoliciesForCategoryRepository
                .findByPolicyStatus_NameOrPolicyStatus_Name("미사용","사용됨",pageable);
//        List<PricePolicyForCategoryResponse> result = new ArrayList<>();
//        for(PricePolicyForCategory pricePolicyForCategory : page){
//            result.add(PricePolicyForCategoryResponse.changeEntityToDto(pricePolicyForCategory));
//        }

        return page.map(PricePolicyForCategoryResponse::changeEntityToDto);
    }

    // 정률정책 for Book read (one)
    public RatePolicyForBookResponse getRatePolicyForBook(Long id){

        RatePolicyForBook ratePolicyForBook = ratePoliciesForBookRepository.findById(id)
                .orElseThrow(() ->new PolicyNotFoundException("해당하는 ID의 정책이 존재하지 않습니다"));
        return RatePolicyForBookResponse.changeEntityToDto(ratePolicyForBook);
    }

    // 정률정책 for Category read (one)
    public RatePolicyForCategoryResponse getRatePolicyForCategory(Long id){

        RatePolicyForCategory ratePolicyForCategory = ratePoliciesForCategoryRepository.findById(id)
                .orElseThrow(()->new PolicyNotFoundException("해당하는 ID의 정책이 존재하지 않습니다"));
        return RatePolicyForCategoryResponse.changeEntityToDto(ratePolicyForCategory);
    }

    // 정액정책 for Book read (one)
    public PricePolicyForBookResponse getPricePolicyForBook(Long id){

        PricePolicyForBook pricePolicyForBook = pricePoliciesForBookRepository.findById(id)
                .orElseThrow(()->new PolicyNotFoundException("해당하는 ID의 정책이 존재하지 않습니다"));
        return PricePolicyForBookResponse.changeEntityToDto(pricePolicyForBook);
    }

    // 정액정책 for Category read (one)
    public PricePolicyForCategoryResponse getPricePolicyForCategory(Long id){

        PricePolicyForCategory pricePolicyForCategory = pricePoliciesForCategoryRepository.findById(id)
                .orElseThrow(()->new PolicyNotFoundException("해당하는 ID의 정책이 존재하지 않습니다"));
        return PricePolicyForCategoryResponse.changeEntityToDto(pricePolicyForCategory);
    }

    // 정률정책 for Book update
    @Transactional
    public RatePolicyForBookResponse updateRatePolicyForBook(UpdateRatePolicyForBookRequest updateRatePolicyForBookRequest){

        RatePolicyForBook ratePolicyForBook = ratePoliciesForBookRepository.findById(updateRatePolicyForBookRequest.getId())
                .orElseThrow(()->new PolicyNotFoundException("해당하는 ID의 정책이 존재하지 않습니다"));

        if(ratePolicyForBook.getPolicyStatus().getName().equals("사용됨") || ratePolicyForBook.getPolicyStatus().getName().equals("삭제됨")){
            throw new PolicyCannotUpdateException("이미 쿠폰에 적용되어 사용중인 정책입니다. 수정할 수 없습니다");
        }

        Book book = bookRepository.findByIsbn13(updateRatePolicyForBookRequest.getBookIsbn13());
        ratePolicyForBook.updatePolicy(updateRatePolicyForBookRequest,book);

        return RatePolicyForBookResponse.changeEntityToDto(ratePolicyForBook);
    }

    // 정률정책 for Category update
    @Transactional
    public RatePolicyForCategoryResponse updateRatePolicyForCategory(UpdateRatePolicyForCategoryRequest updateRatePolicyForCategoryRequest){

        RatePolicyForCategory ratePolicyForCategory = ratePoliciesForCategoryRepository.findById(updateRatePolicyForCategoryRequest.getId())
                .orElseThrow(()->new PolicyNotFoundException("해당하는 ID의 정책이 존재하지 않습니다"));

        if(ratePolicyForCategory.getPolicyStatus().getName().equals("사용됨") || ratePolicyForCategory.getPolicyStatus().getName().equals("삭제됨")){
            throw new PolicyCannotUpdateException("이미 쿠폰에 적용되어 사용중인 정책입니다. 수정할 수 없습니다");
        }

        Category category = categoryRepository.findById(
                updateRatePolicyForCategoryRequest.getCategoryId()).orElseThrow(()->new CategoryNameDuplicateException("해당하는 ID의 카테고리가 존재하지 않습니다")
        );
        ratePolicyForCategory.updatePolicy(updateRatePolicyForCategoryRequest, category);
        return RatePolicyForCategoryResponse.changeEntityToDto(ratePolicyForCategory);
    }

    // 정액정책 for Book update
    @Transactional
    public PricePolicyForBookResponse updatePricePolicyForBook(UpdatePricePolicyForBookRequest updatePricePolicyForBookRequest){

        PricePolicyForBook pricePolicyForBook = pricePoliciesForBookRepository.findById(updatePricePolicyForBookRequest.getId())
                .orElseThrow(()->new PolicyNotFoundException("해당하는 ID의 정책이 존재하지 않습니다"));

        if(pricePolicyForBook.getPolicyStatus().getName().equals("사용됨") || pricePolicyForBook.getPolicyStatus().getName().equals("삭제됨")){
            throw new PolicyCannotUpdateException("이미 쿠폰에 적용되어 사용중인 정책입니다. 수정할 수 없습니다");
        }

        Book book = bookRepository.findByIsbn13(updatePricePolicyForBookRequest.getBookIsbn13());
        pricePolicyForBook.updatePolicy(updatePricePolicyForBookRequest,book);

        return PricePolicyForBookResponse.changeEntityToDto(pricePolicyForBook);
    }

    // 정액정책 for Category update
    @Transactional
    public PricePolicyForCategoryResponse updatePricePolicyForCategory(UpdatePricePolicyForCategoryRequest updatePricePolicyForCategoryRequest){

        PricePolicyForCategory pricePolicyForCategory = pricePoliciesForCategoryRepository.findById(updatePricePolicyForCategoryRequest.getId())
                .orElseThrow(()->new PolicyNotFoundException("해당하는 ID의 정책이 존재하지 않습니다"));

        if(pricePolicyForCategory.getPolicyStatus().getName().equals("사용됨") || pricePolicyForCategory.getPolicyStatus().getName().equals("삭제됨")){
            throw new PolicyCannotUpdateException("이미 쿠폰에 적용되어 사용중인 정책입니다. 수정할 수 없습니다");
        }

        Category category = categoryRepository.findById(
                updatePricePolicyForCategoryRequest.getCategoryId()).orElseThrow(()->new CategoryNameDuplicateException("해당하는 ID의 카테고리가 존재하지 않습니다")
        );
        pricePolicyForCategory.updatePolicy(updatePricePolicyForCategoryRequest, category);

        return PricePolicyForCategoryResponse.changeEntityToDto(pricePolicyForCategory);
    }



    // 정률정책 for Book delete
    @Transactional
    public RatePolicyForBookResponse deleteRatePolicyForBook(Long id){

        RatePolicyForBook ratePolicyForBook = ratePoliciesForBookRepository.findById(id)
                .orElseThrow(()->new PolicyNotFoundException("해당하는 ID의 정책이 존재하지 않습니다"));
        PolicyStatus deleteStatus = policyStatusRepository.findByName("삭제됨");

        if(ratePolicyForBook.getPolicyStatus().getName().equals("사용됨")){
            ratePolicyForBook.deletePolicy(deleteStatus);
        }
        else{
            ratePolicyForBook.deletePolicy(deleteStatus);
            ratePoliciesForBookRepository.delete(ratePolicyForBook);
        }
        return RatePolicyForBookResponse.changeEntityToDto(ratePolicyForBook);

    }

    // 정률정책 for Category delete
    @Transactional
    public RatePolicyForCategoryResponse deleteRatePolicyForCategory(Long id){

        RatePolicyForCategory ratePolicyForCategory = ratePoliciesForCategoryRepository.findById(id)
                .orElseThrow(()->new PolicyNotFoundException("해당하는 ID의 정책이 존재하지 않습니다"));

        PolicyStatus deleteStatus = policyStatusRepository.findByName("삭제됨");

        if(ratePolicyForCategory.getPolicyStatus().getName().equals("사용됨")){
            ratePolicyForCategory.deletePolicy(deleteStatus);
        }
        else{
            ratePolicyForCategory.deletePolicy(deleteStatus);
            ratePoliciesForCategoryRepository.delete(ratePolicyForCategory);
        }
        return RatePolicyForCategoryResponse.changeEntityToDto(ratePolicyForCategory);

    }

    // 정액정책 for Book delete
    @Transactional
    public PricePolicyForBookResponse deletePricePolicyForBook(Long id){

        PricePolicyForBook pricePolicyForBook = pricePoliciesForBookRepository.findById(id)
                .orElseThrow(()->new PolicyNotFoundException("해당하는 ID의 정책이 존재하지 않습니다"));

        PolicyStatus deleteStatus = policyStatusRepository.findByName("삭제됨");

        if(pricePolicyForBook.getPolicyStatus().getName().equals("사용됨")){
            pricePolicyForBook.deletePolicy(deleteStatus);
        }
        else{
            pricePolicyForBook.deletePolicy(deleteStatus);
            pricePoliciesForBookRepository.delete(pricePolicyForBook);
        }
        return PricePolicyForBookResponse.changeEntityToDto(pricePolicyForBook);

    }

    // 정액정책 for Category delete
    @Transactional
    public PricePolicyForCategoryResponse deletePricePolicyForCategory(Long id){

        PricePolicyForCategory pricePolicyForCategory = pricePoliciesForCategoryRepository.findById(id)
                .orElseThrow(()->new PolicyNotFoundException("해당하는 ID의 정책이 존재하지 않습니다"));

        PolicyStatus deleteStatus = policyStatusRepository.findByName("삭제됨");

        if(pricePolicyForCategory.getPolicyStatus().getName().equals("사용됨")){

            pricePolicyForCategory.deletePolicy(deleteStatus);

        }
        else{

            pricePolicyForCategory.deletePolicy(deleteStatus);
            pricePoliciesForCategoryRepository.delete(pricePolicyForCategory);
        }

        return PricePolicyForCategoryResponse.changeEntityToDto(pricePolicyForCategory);
    }

    public List<UsingPolicyResponse> getUsingPolicy(){

        PolicyStatus usingStatus = policyStatusRepository.findByName("사용됨");

        List<UsingPolicyResponse> usingPolicyResponseList = new ArrayList<>();

       List<RatePolicyForBook> usingRatePoliciesForBook = policiesQueryRepository.getUsingRatePolicyForBook(usingStatus);
       List<RatePolicyForCategory> usingRatePoliciesForCategory= policiesQueryRepository.getUsingRatePolicyForCategory(usingStatus);
       List<PricePolicyForBook> usingPricePoliciesForBook = policiesQueryRepository.getUsingPricePolicyForBook(usingStatus);
       List<PricePolicyForCategory> usingPricePoliciesForCategory = policiesQueryRepository.getUsingPricePolicyForCategory(usingStatus);

       CouponStatus unIssuedStatus = couponStatusRepository.findByName("미발급");

        for(RatePolicyForBook policy : usingRatePoliciesForBook){
            Long couponCount = getCouponCount(policy,unIssuedStatus);
            UsingPolicyResponse usingPolicyResponse = UsingPolicyResponse.changePolicyToPolicyResponse(policy,couponCount);
            usingPolicyResponseList.add(usingPolicyResponse);
        }

        for(RatePolicyForCategory policy : usingRatePoliciesForCategory){
            Long couponCount = getCouponCount(policy,unIssuedStatus);
            UsingPolicyResponse usingPolicyResponse = UsingPolicyResponse.changePolicyToPolicyResponse(policy,couponCount);
            usingPolicyResponseList.add(usingPolicyResponse);
        }

        for(PricePolicyForBook policy : usingPricePoliciesForBook){
            Long couponCount = getCouponCount(policy,unIssuedStatus);
            UsingPolicyResponse usingPolicyResponse = UsingPolicyResponse.changePolicyToPolicyResponse(policy,couponCount);
            usingPolicyResponseList.add(usingPolicyResponse);
        }

        for(PricePolicyForCategory policy : usingPricePoliciesForCategory){
            Long couponCount = getCouponCount(policy,unIssuedStatus);
            UsingPolicyResponse usingPolicyResponse = UsingPolicyResponse.changePolicyToPolicyResponse(policy,couponCount);
            usingPolicyResponseList.add(usingPolicyResponse);
        }

        return usingPolicyResponseList;
    }

    public Long getCouponCount(Policy policy, CouponStatus couponStatus){

        if(policy instanceof  RatePolicyForBook){
            return couponQueryRepository
                    .getValidCouponCountByRatePolicyForBook(((RatePolicyForBook) policy)
                            .getRatePolicyForBookId(),couponStatus);
        }else if(policy instanceof  RatePolicyForCategory){
            return couponQueryRepository.getValidCouponCountByRatePolicyForCategory(((RatePolicyForCategory) policy)
                    .getRatePolicyForCategoryId(),couponStatus);
        }else if(policy instanceof  PricePolicyForBook){
            return couponQueryRepository.getValidCouponCountByPricePolicyForBook(((PricePolicyForBook) policy)
                    .getPricePolicyForBookId(),couponStatus);
        }else{
            return couponQueryRepository
                    .getValidCouponCountByPricePolicyForCategory(((PricePolicyForCategory)policy)
                            .getPricePolicyForCategoryId(),couponStatus);
        }

    }

}
