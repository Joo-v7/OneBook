package com.nhnacademy.taskapi.order.dto;

public class OrdererResponseDto {
    /*
    주문자
        이름
        전화번호
    받는사람
        배송지별명
        이름
        전화번호
        상세주소
        요청사항
    */
    String ordererName;
    String ordererPhoneNumber;
    // 배송지 별명
    String recipientAlias;
    String recipientName;
    String recipientPhoneNumber;
    String recipientAddress;
    // 요청사항
    String recipientRequestedTerm;
}
