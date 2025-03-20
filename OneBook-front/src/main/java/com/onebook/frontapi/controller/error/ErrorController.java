package com.onebook.frontapi.controller.error;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class ErrorController {

    @GetMapping("/error/403")
    public String error403(Model model) {
        String error = "403";
        String content = "관리자 외에 접근할 수 없는 페이지 입니다.";

        model.addAttribute("error", error);
        model.addAttribute("content", content);

        return "error/error";
    }

    @GetMapping("/error/401")
    public String error401(Model model) {
        String error = "401";
        String content = "인증이 실패 되었습니다.";

        model.addAttribute("error", error);
        model.addAttribute("content", content);

        return "error/error";
    }

}
