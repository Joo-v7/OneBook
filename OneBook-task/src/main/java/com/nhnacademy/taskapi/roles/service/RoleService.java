package com.nhnacademy.taskapi.roles.service;

import com.nhnacademy.taskapi.roles.domain.Role;
import com.nhnacademy.taskapi.roles.dto.RoleModifyRequestDto;
import com.nhnacademy.taskapi.roles.dto.RoleRegisterRequestDto;
import com.nhnacademy.taskapi.roles.dto.RoleResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoleService {
    List<RoleResponseDto> getAllRoles();
    Page<RoleResponseDto> getRolesPagination(Pageable pageable);
    RoleResponseDto getRoleById(Integer id);
    RoleResponseDto getDefaultRole();
//    boolean isDuplicateName(String name);
    RoleResponseDto registerRole(RoleRegisterRequestDto roleRegisterRequestDto);
    RoleResponseDto modifyRole(Integer id, RoleModifyRequestDto roleModifyRequestDto);
    void deleteRole(Integer id);
}
