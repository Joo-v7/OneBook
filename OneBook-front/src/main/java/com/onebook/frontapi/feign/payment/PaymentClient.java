package com.onebook.frontapi.feign.payment;

import com.onebook.frontapi.dto.payment.CheckoutInfoResponseDto;
import com.onebook.frontapi.dto.payment.PaymentRequestDto;
import com.onebook.frontapi.dto.payment.PaymentResponseDto;
import com.onebook.frontapi.dto.payment.toss.TossConfirmRequestDto;
import com.onebook.frontapi.dto.payment.toss.TossConfirmResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "PaymentClient", url = "${onebook.gatewayUrl}")
public interface PaymentClient {

    /** 결제 생성 (READY) */
    @PostMapping("/task/payments/{orderId}")
    PaymentResponseDto createPayment(@PathVariable("orderId") String orderId,
                                     @RequestBody PaymentRequestDto requestDto);


    @GetMapping("/task/payments/checkout-info/{orderId}")
    CheckoutInfoResponseDto getCheckoutInfo(@PathVariable("orderId") String orderId);

    /** 결제 단건 조회 */
    @GetMapping("/task/payments/{paymentId}")
    PaymentResponseDto getPayment(@PathVariable("paymentId") Long paymentId);

    /** Toss 결제 최종 승인 */
    @PostMapping("/task/payments/toss/confirm")
    TossConfirmResponseDto confirmTossPayment(@RequestBody TossConfirmRequestDto requestDto);
}
