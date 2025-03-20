package com.onebook.frontapi.controller.member;

import com.onebook.frontapi.dto.member.MemberResponseForAdmin;
import com.onebook.frontapi.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/members")
public class MemberAdminController {
    private final MemberService memberService;

    /**
     * 관리자 페이지 - 회원 관리
     */
    @GetMapping
    public String memberListForm(
            @RequestParam(defaultValue = "0", name="page") int pageNo,
            @RequestParam(defaultValue = "10", name="pageSize") int pageSize,
            @RequestParam(defaultValue = "name", name="criteria") String criteria,
            Model model) {

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(criteria).ascending());
        Page<MemberResponseForAdmin> memberListForAdmin = memberService.getMemberListForAdmin(pageable);

        model.addAttribute("memberList", memberListForAdmin);
        return "admin/member/list";
    }

}
