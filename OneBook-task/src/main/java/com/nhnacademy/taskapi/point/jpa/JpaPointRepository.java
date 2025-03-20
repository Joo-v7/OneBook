package com.nhnacademy.taskapi.point.jpa;

import com.nhnacademy.taskapi.point.domain.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface JpaPointRepository extends JpaRepository<Point, Long> {
    Optional<Point> findByMember_Id(Long member_id);

    // 포인트를 업데이트하는 쿼리 작성
    // @Query와 @Modifying 어노테이션을 사용하여 직접 UPDATE 쿼리를 작성
    @Modifying
    @Transactional
    @Query("UPDATE Point p SET p.pointCurrent = :updatedAmount WHERE p.member.id = :member_id")
    void updateMemberPoints(Long member_id, int updatedAmount);
}