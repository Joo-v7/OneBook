package com.onebook.frontapi.controller.role;

import com.onebook.frontapi.dto.role.RoleModifyRequest;
import com.onebook.frontapi.dto.role.RoleRegisterRequest;
import com.onebook.frontapi.dto.role.RoleResponse;
import com.onebook.frontapi.service.role.RoleService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/roles")
public class RoleAdminController {
    private final RoleService roleService;

    /**
     * 관리자 페이지 - 권한 리스트
     */
    @GetMapping
    public String list(
            @RequestParam(defaultValue = "0", name="page") int pageNo,
            @RequestParam(defaultValue = "10", name="pageSize") int pageSize,
            @RequestParam(defaultValue="id", name="criteria") String criteria,
            Model model) {
            Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(criteria).ascending());
            Page<RoleResponse> allRoles = roleService.getAllRoles(pageable);

            model.addAttribute("roles", allRoles);
            return "admin/role/list";
    }

    /**
     * 관리자 페이지 - 권한 등록 폼
     */
    @GetMapping("/register")
    public String memberRoleRegisterForm() {
        return "admin/role/register";
    }

    /**
     * 관리자 페이지 - 권한 등록
     */
    @PostMapping("/register")
    public String memberRoleRegister(RoleRegisterRequest roleRegisterRequest) {
        try {
            roleService.registerRole(roleRegisterRequest);
            return "redirect:/admin/roles";
        }catch(FeignException e) {
            return "redirect:/admin/roles";
        }
    }

    /**
     * 관리자 페이지 - 권한 수정 폼
     */
    @GetMapping("/modify/{role-id}")
    public String memberRoleModifyForm(
            @PathVariable("role-id") Integer roleId,
            Model model) {
        try {
            RoleResponse role = roleService.getRole(roleId);
            model.addAttribute("role", role);
            return "admin/role/modify";
        }catch(FeignException e) {
            return "redirect:/admin/roles";
        }
    }

    /**
     * 관리자 페이지 - 권한 수정
     */
    @PutMapping("/modify/{role-id}")
    public String memberRoleModifyForm(
            @PathVariable("role-id") Integer roleId,
            @ModelAttribute RoleModifyRequest roleModifyRequest) {
        try {
            roleService.modifyRole(roleId, roleModifyRequest);
            return "redirect:/admin/roles";
        }catch(FeignException e) {
            return "redirect:/admin/roles";
        }
    }

    /**
     * 관리자 페이지 - 권한 삭제
     */
    @DeleteMapping("/remove/{role-id}")
    public String memberRoleRemove(@PathVariable("role-id") Integer id) {
        try {
            roleService.removeRole(id);
            return "redirect:/admin/roles";
        }catch(FeignException e) {
            return "redirect:/admin/roles";
        }
    }



}
