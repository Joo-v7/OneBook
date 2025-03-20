package com.nhnacademy.taskapi.payment.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "payment_methods")
public class PaymentMethod {
    @Id
    private String paymentKey;  // Toss 등에서 발급되는 고유 결제키

    @NotNull
    private String type;        // ex) TOSS, NAVER, PAYCO 등등..

    @Column(nullable = true)
    private String method;      // ex) "카드", "간편결제" 등등..

    // 카드 상세 정보
    private String cardOwnerType;       // ex) "개인", "법인"
    private String cardNumber;          // 마스킹된 카드번호 ex) "54092634****709*"
    private Integer cardAmount;         // ex) 50000
    private String cardIssuerCode;      // ex) "11"
    private String cardAcquirerCode;    // ex) "11"
    private Boolean cardIsInterestFree; // ex) false
    private String cardType;            // ex) "신용"
    private String cardApproveNo;       // ex) "00000000"
    private Integer cardInstallmentPlanMonths; // ex) 12
}
