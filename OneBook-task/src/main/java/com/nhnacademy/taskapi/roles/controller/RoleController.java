package com.nhnacademy.taskapi.roles.controller;

import com.nhnacademy.taskapi.roles.domain.Role;
import com.nhnacademy.taskapi.roles.dto.RoleModifyRequestDto;
import com.nhnacademy.taskapi.roles.dto.RoleRegisterRequestDto;
import com.nhnacademy.taskapi.roles.dto.RoleResponseDto;
import com.nhnacademy.taskapi.roles.service.RoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Tag(name = "role", description = "'관리자', '사용자'와같은 사용자의 권한을 등록, 수정, 삭제, 조회")
public class RoleController {

    private final RoleService roleService;

    // 전체 role 조회
    @GetMapping("/task/roles")
    public ResponseEntity<List<RoleResponseDto>> getRoles() {
        List<RoleResponseDto> roleResponseDtoList = roleService.getAllRoles();
        return ResponseEntity.ok().body(roleResponseDtoList);
    }

    // role 조회 - 페이지네이션
    @GetMapping("/task/admin/roles")
    public ResponseEntity<Page<RoleResponseDto>> getAllRoles(Pageable pageable) {
        Page<RoleResponseDto> roleResponseDtos = roleService.getRolesPagination(pageable);
        return ResponseEntity.ok(roleResponseDtos);
    }

    // 단일 role 조회
    @GetMapping("/task/admin/roles/{id}")
    public ResponseEntity<RoleResponseDto> getRoleById(@PathVariable Integer id) {
        RoleResponseDto roleResponseDto = roleService.getRoleById(id);
        return ResponseEntity.ok().body(roleResponseDto);
    }

    // role 등록
    @PostMapping("/task/admin/roles")
    public ResponseEntity<RoleResponseDto> createRole(@RequestBody @Valid RoleRegisterRequestDto roleRegisterRequestDto) {
        RoleResponseDto roleResponseDto = roleService.registerRole(roleRegisterRequestDto);
        return ResponseEntity.ok().body(roleResponseDto);
    }

    // role 수정
    @PutMapping("/task/admin/roles/{id}")
    public ResponseEntity<RoleResponseDto> updateRole(@PathVariable Integer id, @RequestBody @Valid RoleModifyRequestDto roleModifyRequestDto) {
        RoleResponseDto roleResponseDto = roleService.modifyRole(id, roleModifyRequestDto);
        return ResponseEntity.ok().body(roleResponseDto);
    }

    // role 삭제
    @DeleteMapping("/task/admin/roles/{id}")
    public ResponseEntity<String> deleteRole(@PathVariable Integer id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }


}
