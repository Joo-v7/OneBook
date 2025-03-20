package com.onebook.frontapi.controller.payment;

import com.onebook.frontapi.dto.payment.CheckoutInfoResponseDto;
import com.onebook.frontapi.dto.payment.PaymentRequestDto;
import com.onebook.frontapi.dto.payment.PaymentResponseDto;
import com.onebook.frontapi.dto.payment.toss.TossConfirmRequestDto;
import com.onebook.frontapi.dto.payment.toss.TossConfirmResponseDto;
import com.onebook.frontapi.feign.payment.PaymentClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 */
@RestController
@RequestMapping("/front/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentFrontController {

    private final PaymentClient paymentClient;

    /**
     * [GET] 결제 전에 필요한 정보 (주문금액, 포인트 등)
     */
    @GetMapping("/checkout-info/{orderId}")
    public ResponseEntity<CheckoutInfoResponseDto> getCheckoutInfo(
            @PathVariable String orderId
    ) {
        log.info("[Front] getCheckoutInfo orderId={}", orderId);
        CheckoutInfoResponseDto dto = paymentClient.getCheckoutInfo(orderId);
        return ResponseEntity.ok(dto);
    }

    /**
     * [POST] 결제 생성(READY)
     */
    @PostMapping("/{orderId}")
    public ResponseEntity<PaymentResponseDto> createPayment(
            @PathVariable String orderId,
            @RequestBody PaymentRequestDto requestDto
    ) {
        log.info("[Front] Create Payment for orderId={}, usedPoint={}",
                orderId, requestDto.getUsedPoint());

        PaymentResponseDto response = paymentClient.createPayment(orderId, requestDto);
        return ResponseEntity.ok(response);
    }

    /**
     * [GET] 결제 단건 조회
     */
    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponseDto> getPayment(@PathVariable Long paymentId) {
        PaymentResponseDto response = paymentClient.getPayment(paymentId);
        return ResponseEntity.ok(response);
    }

    /**
     * [POST] 토스 결제 승인
     */
    @PostMapping("/toss/confirm")
    public ResponseEntity<TossConfirmResponseDto> confirmToss(
            @RequestBody TossConfirmRequestDto dto
    ) {
        log.info("[Front] Toss confirm request: paymentKey={}, orderId={}, amount={}",
                 dto.getPaymentKey(), dto.getOrderId(), dto.getAmount());

        TossConfirmResponseDto result = paymentClient.confirmTossPayment(dto);

        return ResponseEntity.ok(result);
    }
}
