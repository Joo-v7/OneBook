package com.nhnacademy.taskapi.point.service.impl;

import com.nhnacademy.taskapi.member.domain.Member;
import com.nhnacademy.taskapi.member.repository.MemberRepository;
import com.nhnacademy.taskapi.point.domain.Point;
import com.nhnacademy.taskapi.point.domain.PointLog;
import com.nhnacademy.taskapi.point.exception.ApplicationException;
import com.nhnacademy.taskapi.point.repository.PointLogRepository;
import com.nhnacademy.taskapi.point.dto.PointLogResponse;
import com.nhnacademy.taskapi.point.repository.PointRepository;
import com.nhnacademy.taskapi.point.service.PointLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PointLogServiceImpl implements PointLogService {

    private final PointLogRepository pointLogRepository;
    private final MemberRepository memberRepository;
    private final PointRepository pointRepository;

    @Override
    public Page<PointLogResponse> findAllPointLogsByMemberId(Long memberId, Pageable pageable) {

        // 1. 회원 검증
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException("존재하지 않는 회원입니다."));

        // 2. 포인트 검증
        Point point = pointRepository.findByMemberId(memberId)
                .orElseThrow(() -> new ApplicationException("포인트 정보가 없는 회원입니다."));

        // 3. 포인트 로그 조회
        Page<PointLog> pointLogPage = pointLogRepository.findByPoint_Member_IdOrderByPointLogUpdatedAtDesc(memberId, pageable);

        // 4. PointLogResponse 반환
        return pointLogPage.map(PointLogResponse::fromEntity);
    }
}