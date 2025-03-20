package com.nhnacademy.taskapi.coupon.domain.entity.coupons;

import com.nhnacademy.taskapi.member.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "coupon_box")
public class IssuedCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long issuedCouponId;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "coupon_id",nullable = false)
    private Coupon coupon;

    @ManyToOne
    @JoinColumn(name = "member_id",nullable = false)
    private Member member;

    @Column(nullable = false)
    private LocalDateTime issueDateTime;
    @Setter
    private LocalDateTime useDateTime;

    public IssuedCoupon(Coupon coupon, Member member, LocalDateTime issueDateTime) {
        this.coupon = coupon;
        this.member = member;
        this.issueDateTime = issueDateTime;
    }

    public static IssuedCoupon createIssuedCoupon(Coupon coupon, Member member){

        return new IssuedCoupon(coupon,member,LocalDateTime.now());
    }

    // TODO 쿠폰이 사용되었을때 , 사용된 시간을 기록해주는 메서드
    public void addUseDateTime(){
        this.useDateTime = LocalDateTime.now();
    }
}
