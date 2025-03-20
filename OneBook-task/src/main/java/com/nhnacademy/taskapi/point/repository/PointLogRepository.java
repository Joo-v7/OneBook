package com.nhnacademy.taskapi.point.repository;

import com.nhnacademy.taskapi.point.domain.PointLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PointLogRepository extends JpaRepository<PointLog, Long> {
    // 기존의 페이지네이션 지원 메서드
    Page<PointLog> findByPoint_Member_IdOrderByPointLogUpdatedAtDesc(@Param("member_id") Long member_id, Pageable pageable);

    // 페이징 없이 모든 로그를 반환하는 메서드 추가
    @Query("SELECT pl FROM PointLog pl WHERE pl.point.member.id = :member_id")
    List<PointLog> findByPoint_Member_IdOrderByPointLogUpdatedAtDesc(@Param("member_id") Long member_id);
}


//PointLog와 Member가 Point를 통해 연결되어 있기 때문