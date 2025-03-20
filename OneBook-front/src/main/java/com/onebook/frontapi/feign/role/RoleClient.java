package com.onebook.frontapi.feign.role;

import com.onebook.frontapi.dto.role.RoleModifyRequest;
import com.onebook.frontapi.dto.role.RoleRegisterRequest;
import com.onebook.frontapi.dto.role.RoleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@FeignClient(name="RoleClient", url="${onebook.gatewayUrl}")
public interface RoleClient {
    @GetMapping("/task/admin/roles")
    Page<RoleResponse> getAll(Pageable pageable);

    @GetMapping("/task/admin/roles/{id}")
    RoleResponse getRole(@PathVariable("id") Integer id);

    @PostMapping("/task/admin/roles")
    RoleResponse registerRole(@RequestBody RoleRegisterRequest roleRegisterRequest);

    @PutMapping("/task/admin/roles/{id}")
    RoleResponse modifyRole(@PathVariable("id") Integer id, @RequestBody RoleModifyRequest roleModifyRequest);

    @DeleteMapping("/task/admin/roles/{id}")
    void removeRole(@PathVariable("id") Integer id);
}
