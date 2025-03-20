package com.onebook.frontapi.dto.myhome;

public record MyOrderStatusResponse(
        /*
        * 결제 대기, 배송전, 배송중, 배송완료, 취소/교환/환불, 구매확정
        * */
    int pendingPayment,
    int beforeDelivery,
    int shipping,
    int deliveryComplete,
    int cancellationExchangeRefund,
    int purchaseConfirmation
) {
}
