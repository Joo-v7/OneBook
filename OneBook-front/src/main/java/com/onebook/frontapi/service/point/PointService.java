package com.onebook.frontapi.service.point;

import com.onebook.frontapi.dto.point.response.MemberPointResponse;
import com.onebook.frontapi.dto.point.response.PointLogResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PointService {
    List<MemberPointResponse> getMemberPointHistories(Long memberId);

    int getMemberPoint();
}
