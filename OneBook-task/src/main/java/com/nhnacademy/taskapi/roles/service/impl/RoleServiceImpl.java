package com.nhnacademy.taskapi.roles.service.impl;

import com.nhnacademy.taskapi.roles.domain.Role;
import com.nhnacademy.taskapi.roles.dto.RoleModifyRequestDto;
import com.nhnacademy.taskapi.roles.dto.RoleRegisterRequestDto;
import com.nhnacademy.taskapi.roles.dto.RoleResponseDto;
import com.nhnacademy.taskapi.roles.exception.RoleIllegalArgumentException;
import com.nhnacademy.taskapi.roles.exception.RoleNotFoundException;
import com.nhnacademy.taskapi.roles.repository.RoleRepository;
import com.nhnacademy.taskapi.roles.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    // 모든 role 조회
    @Transactional(readOnly = true)
    @Override
    public List<RoleResponseDto> getAllRoles() {
        List<Role> roleList = roleRepository.findAll();
        List<RoleResponseDto> result = new ArrayList<>();

        if(roleList.isEmpty()) {
            return result;
        }

        for(Role r : roleList) {
            RoleResponseDto rr = RoleResponseDto.from(r);
            result.add(rr);
        }
        return result;
    }

    // 모든 role 조회 - 페이지네이션
    @Transactional(readOnly = true)
    @Override
    public Page<RoleResponseDto> getRolesPagination(Pageable pageable) {
        Page<Role> roleResponseDtos = roleRepository.findAll(pageable);
        return roleResponseDtos.map(RoleResponseDto::from);
    }

    // 단일 role 조회
    @Transactional(readOnly = true)
    @Override
    public RoleResponseDto getRoleById(Integer id) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new RoleNotFoundException("Not Found Role by" + id));
        return RoleResponseDto.from(role);
    }

    @Transactional(readOnly = true)
    @Override
    public RoleResponseDto getDefaultRole() {
        Role role = roleRepository.findById(1).orElseThrow(() -> new RoleNotFoundException("Default Role doesn't exist"));
        return RoleResponseDto.from(role);
    }

//    // Role name 중복 체크
//    @Override
//    public boolean isDuplicateName(String name) {
//        return roleRepository.existsByName(name);
//    }

    // role 등록
    @Override
    public RoleResponseDto registerRole(RoleRegisterRequestDto roleRegisterRequestDto) {
        if(roleRepository.existsByName(roleRegisterRequestDto.name())) {
            throw new RoleIllegalArgumentException("Role Name already exists");
        }

        try {
            Role role = Role.createRole(roleRegisterRequestDto.name(), roleRegisterRequestDto.description());
            return RoleResponseDto.from(roleRepository.save(role));
        }catch(DataIntegrityViolationException e) {
            throw new RoleIllegalArgumentException("Failed to save role in the database: Invalid format");
        }
    }

    // role 수정
    @Override
    public RoleResponseDto modifyRole(Integer id, RoleModifyRequestDto roleModifyRequestDto) {
        if(roleRepository.existsByName(roleModifyRequestDto.name())) {
            throw new RoleIllegalArgumentException("Role Name already exists");
        }

            Role role = roleRepository.findById(id).orElseThrow(()-> new RoleNotFoundException("Not Found Role by " + id));
            role.modifyRole(roleModifyRequestDto.name(), roleModifyRequestDto.description());
            return RoleResponseDto.from(role);
    }

    // role 삭제
    @Override
    public void deleteRole(Integer id) {
        if(!roleRepository.existsById(id)) {
            throw new RoleIllegalArgumentException("Role id does not exist: " + id);
        }

        roleRepository.deleteById(id);
    }
}
