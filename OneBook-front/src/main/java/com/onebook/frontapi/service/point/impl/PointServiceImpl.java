package com.onebook.frontapi.service.point.impl;

import com.onebook.frontapi.common.CommonResponse;
import com.onebook.frontapi.dto.point.response.MemberPointResponse;
import com.onebook.frontapi.dto.point.response.PointLogResponse;
import com.onebook.frontapi.feign.point.PointFeignClient;
import com.onebook.frontapi.service.point.PointService;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PointServiceImpl implements PointService {

    private final PointFeignClient pointFeign;

    public PointServiceImpl(PointFeignClient pointFeign) {
        this.pointFeign = pointFeign;
    }

    @Override
    public List<MemberPointResponse> getMemberPointHistories(Long memberId) {
        try {
            // FeignClient 호출 시 memberId 전달
            CommonResponse<List<MemberPointResponse>> response = pointFeign.getUserPointHistories(memberId);

            if (response == null || response.getResult() == null) {
                throw new IllegalStateException("잘못된 상태: 포인트 내역을 찾을 수 없음");
            }

            return response.getResult();
        } catch (FeignException.Unauthorized ex) {
            // Unauthorized (로그인 필요)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인되지 않은 상태에서 포인트 내역을 조회할 수 없습니다.", ex);
        } catch (FeignException.BadRequest ex) {
            // BadRequest (잘못된 요청)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.", ex);
        } catch (FeignException.NotFound ex) {
            // NotFound (리소스를 찾을 수 없음)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "포인트 내역을 찾을 수 없습니다.", ex);
        } catch (FeignException.InternalServerError ex) {
            // Internal Server Error
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류", ex);
        } catch (IllegalStateException ex) {
            // 잘못된 상태에 대한 예외 처리
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 오류가 발생했습니다.", ex);
        } catch (Exception ex) {
            // 그 외의 모든 예외 처리
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "서버에서 오류가 발생했습니다.", ex);
        }

    }

    @Override
    public int getMemberPoint() {
        return pointFeign.getMemberPoint();
    }
}