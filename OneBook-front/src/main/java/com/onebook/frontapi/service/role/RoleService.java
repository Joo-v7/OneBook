package com.onebook.frontapi.service.role;

import com.onebook.frontapi.dto.role.RoleModifyRequest;
import com.onebook.frontapi.dto.role.RoleRegisterRequest;
import com.onebook.frontapi.dto.role.RoleResponse;
import com.onebook.frontapi.feign.role.RoleClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RoleService {
    private final RoleClient roleClient;

    /**
     * 권한 전체 조회
     */
    public Page<RoleResponse> getAllRoles(Pageable pageable) {
        return roleClient.getAll(pageable);
    }

    /**
     * 권한 단일 조회
     */
    public RoleResponse getRole(Integer id) {
        return roleClient.getRole(id);
    }

    /**
     * 권한 등록
     */
    public RoleResponse registerRole(RoleRegisterRequest roleRegisterRequest) {
        return roleClient.registerRole(roleRegisterRequest);
    }

    /**
     * 권한 수정
     */
    public RoleResponse modifyRole(Integer id ,RoleModifyRequest roleModifyRequest) {
        return roleClient.modifyRole(id, roleModifyRequest);
    }

    /**
     * 권한 삭제
     */
    public void removeRole(Integer id) {
        roleClient.removeRole(id);
    }
}
