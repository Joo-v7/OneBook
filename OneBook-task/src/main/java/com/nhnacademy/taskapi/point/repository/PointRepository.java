package com.nhnacademy.taskapi.point.repository;

import com.nhnacademy.taskapi.point.domain.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PointRepository extends JpaRepository<Point, Long> {
    // memberId를 기준으로 Point를 찾는 쿼리
    @Query("SELECT p FROM Point p WHERE p.member.id = :memberId")
    Optional<Point> findByMemberId(Long memberId);
}
