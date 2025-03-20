package com.onebook.frontapi.dto.point.response;

import lombok.Getter;

/**
 * 포인트 변경 타입을 정의하는 Enum 클래스입니다.
 * 회원가입, 결제, 환불, 리뷰 작성 등 다양한 포인트 로그 업데이트 유형을 관리합니다.
 */
@Getter
public enum PointLogUpdatedType {
    REGISTRATION("회원 가입"),
    PAYMENT("포인트 결제"),
    REFUND("포인트 환불"),
    REVIEW("리뷰 작성"),
    PURCHASE("도서 구매");

    private final String description;

    PointLogUpdatedType(String description) {
        this.description = description;
    }
}