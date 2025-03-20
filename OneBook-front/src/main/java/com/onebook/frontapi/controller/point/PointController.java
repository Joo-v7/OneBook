package com.onebook.frontapi.controller.point;

import com.onebook.frontapi.dto.point.response.MemberPointResponse;
import com.onebook.frontapi.service.point.PointService;
import feign.FeignException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/point")
public class PointController {

    private final PointService pointService;

    @Autowired
    public PointController(PointService pointService) {
        this.pointService = pointService;
    }

    @GetMapping("/myPoint")
    public ModelAndView getUserPointHistories(HttpSession session) {
        // 로그인 체크
        if (session.getAttribute("user") == null) {
            // 로그인되지 않으면 로그인 페이지로 리다이렉트
            return new ModelAndView("redirect:/login");
        }

        try {
            // 세션에서 사용자 ID를 가져옴
            Long memberId = (Long) session.getAttribute("userId"); // "userId"는 예시로, 실제 저장된 키를 사용해야 함

            // 외부 서비스에서 포인트 히스토리를 가져옴
            List<MemberPointResponse> memberPointHistories = pointService.getMemberPointHistories(memberId);

            ModelAndView modelAndView = new ModelAndView("mypage/mypagePoint");
            modelAndView.addObject("memberPointHistories", memberPointHistories);
            return modelAndView;
        } catch (FeignException.Unauthorized e) {
            // Unauthorized (로그인 필요)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인 필요", e);
        } catch (FeignException.InternalServerError e) {
            // 서버 오류 (Feign 예외 처리)
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류", e);
        } catch (RuntimeException e) {
            // 그 외 오류 (일반 서버 오류 처리)
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류", e);
        }
    }
}
