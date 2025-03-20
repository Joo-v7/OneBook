package com.onebook.frontapi.controller.admin;

import com.onebook.frontapi.dto.member.MemberResponse;
import com.onebook.frontapi.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final MemberService memberService;

    @GetMapping()
    public String index(Model model) {
        MemberResponse memberResponse = memberService.getMember();
        model.addAttribute("member", memberResponse);

        return "admin/home/home";
    }
}
