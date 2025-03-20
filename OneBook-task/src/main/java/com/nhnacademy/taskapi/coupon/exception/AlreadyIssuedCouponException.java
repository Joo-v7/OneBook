package com.nhnacademy.taskapi.coupon.exception;

public class AlreadyIssuedCouponException extends RuntimeException {
    public AlreadyIssuedCouponException(String message) {
        super(message);
    }
}
