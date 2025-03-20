package com.nhnacademy.taskapi.config;

import com.nhnacademy.taskapi.address.exception.DefaultAddressCannotRemoveException;
import com.nhnacademy.taskapi.address.exception.MemberAddressLimitExceededException;
import com.nhnacademy.taskapi.cart.exception.CartIllegalArgumentException;
import com.nhnacademy.taskapi.cart.exception.CartNotFoundException;
import com.nhnacademy.taskapi.coupon.exception.AlreadyIssuedCouponException;
import com.nhnacademy.taskapi.exception.dto.ErrorResponse;
import com.nhnacademy.taskapi.grade.exception.GradeIllegalArgumentException;
import com.nhnacademy.taskapi.grade.exception.GradeNotFoundException;
import com.nhnacademy.taskapi.member.exception.MemberIllegalArgumentException;
import com.nhnacademy.taskapi.member.exception.MemberNotFoundException;
import com.nhnacademy.taskapi.order.exception.OrderNotFoundException;
import com.nhnacademy.taskapi.payment.exception.PaymentNotFoundException;
import com.nhnacademy.taskapi.roles.exception.RoleIllegalArgumentException;
import com.nhnacademy.taskapi.roles.exception.RoleNotFoundException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {

    // NotFoundException
    @ExceptionHandler({
            MemberNotFoundException.class,
            GradeNotFoundException.class,
            RoleNotFoundException.class,
            CartNotFoundException.class,
            OrderNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFoundException(ChangeSetPersister.NotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    // IllegalArgumentException
    @ExceptionHandler({
            MemberIllegalArgumentException.class,
            GradeIllegalArgumentException.class,
            RoleIllegalArgumentException.class,
            CartIllegalArgumentException.class,
    })
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // MethodArgumentNotValidException
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ErrorResponse errorResponse = new ErrorResponse("DTO 유효성 검사 실패", HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // 같은 쿠폰 중복 발급 요청시 발생하는 exception
    @ExceptionHandler(AlreadyIssuedCouponException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyIssuedCouponException(AlreadyIssuedCouponException e){
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // 기본 배송지를 삭제할때 발생하는 exception
    @ExceptionHandler(DefaultAddressCannotRemoveException.class)
    public ResponseEntity<ErrorResponse> handleDefaultAddressCannotRemoveException(DefaultAddressCannotRemoveException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // 배송지를 10개 이상 등록하려고 할때 발생하는 exception
    @ExceptionHandler(MemberAddressLimitExceededException.class)
    public ResponseEntity<ErrorResponse> handleMemberAddressLimitExceededException(MemberAddressLimitExceededException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<String> handlePaymentNotFoundException(PaymentNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

}
