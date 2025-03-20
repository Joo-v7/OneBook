package com.nhnacademy.taskapi.review.service.impl;

import com.nhnacademy.taskapi.book.domain.Book;
import com.nhnacademy.taskapi.image.service.ImageService;
import com.nhnacademy.taskapi.member.domain.Member;
import com.nhnacademy.taskapi.member.repository.MemberRepository;
import com.nhnacademy.taskapi.order.entity.Order;
import com.nhnacademy.taskapi.order.entity.OrderDetail;
import com.nhnacademy.taskapi.payment.domain.Payment;
import com.nhnacademy.taskapi.payment.repository.PaymentRepository;
import com.nhnacademy.taskapi.review.domain.Review;
import com.nhnacademy.taskapi.review.exception.InvalidReviewException;
import com.nhnacademy.taskapi.review.repository.ReviewRepository;
import com.nhnacademy.taskapi.review.service.PendingReviewService;
import com.nhnacademy.taskapi.review.dto.BookReviewableResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PendingReviewServiceImpl implements PendingReviewService {

    private final PaymentRepository paymentRepository;
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final ImageService imageService;

    @Override
    public List<BookReviewableResponse> getPendingReviews(Long memberId) {

        // 1. 회원 검증
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new InvalidReviewException("존재하지 않는 회원입니다."));

        // 2. 결제 테이블에서 status == 'DONE' && memberId 조회
        List<Payment> donePayments = paymentRepository.findByOrder_Member_IdAndStatus(memberId, "DONE");
        if(donePayments.isEmpty()) {
            log.info("결제 완료된 내역이 없습니다. memberId = {}", memberId);
            return Collections.emptyList();
        }

        // 3. Payment -> Order 추출
        Set<Order> orders = donePayments.stream()
                .map(Payment::getOrder)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // 4. 최종 결과를 담을 리스트 및 중복 방지를 위한 Set
        List<BookReviewableResponse> result = new ArrayList<>();
        Set<Long> seenBookIds = new HashSet<>();

        // 5. 각 Order의 OrderDetail에서 BookId를 꺼내서 리뷰 유무 확인
        for (Order order : orders) {
            if(order.getOrderDetailList() == null) {
                continue;
            }

            for (OrderDetail detail : order.getOrderDetailList()) {
                Book book = detail.getBook();

                if (book == null) {
                    continue;
                }

                // 이미 처리된 책인지 확인
                if (seenBookIds.contains(book.getBookId())) {
                    continue;
                }

                // 리뷰 작성 유무 확인
                Optional<Review> checkReview = reviewRepository.findByMemberAndBook(member, book);
                if (checkReview.isEmpty()) {
                    // 리뷰를 안 썼으면 BookReviewableResponse 에 담음
                    BookReviewableResponse dto = new BookReviewableResponse(
                            book.getBookId(),
                            book.getTitle(),
                            imageService.getImage(book.getBookId()).getUrl(),
                            order.getDateTime() // 추후 배송 완료일로 변경 가능
                    );

                    result.add(dto);

                    // 똑같은 책을 2번 샀을경우
                    // 리뷰 작성하지 않은 도서목록에
                    // 도서가 중복으로 뜨는 현상 해결
                    seenBookIds.add(book.getBookId());
                }
            }
        }
        return result;
    }
}
