package com.nhnacademy.taskapi.roles.service;

import com.nhnacademy.taskapi.roles.domain.Role;
import com.nhnacademy.taskapi.roles.dto.RoleModifyRequestDto;
import com.nhnacademy.taskapi.roles.dto.RoleRegisterRequestDto;
import com.nhnacademy.taskapi.roles.dto.RoleResponseDto;
import com.nhnacademy.taskapi.roles.exception.RoleIllegalArgumentException;
import com.nhnacademy.taskapi.roles.repository.RoleRepository;
import com.nhnacademy.taskapi.roles.service.impl.RoleServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    @Test
    @DisplayName("Get All Roles Successfully")
    void getAllRoleTest1() {
        Role role = Role.createRole("MEMBER", "일반 회원");
        List<Role> roleList = Arrays.asList(role);
        Mockito.when(roleRepository.findAll()).thenReturn(roleList);

        List<RoleResponseDto> result = roleService.getAllRoles();

        Mockito.verify(roleRepository, Mockito.times(1)).findAll();

        Assertions.assertThat(result.getFirst().name()).isEqualTo("MEMBER");
        Assertions.assertThat(result.getFirst().description()).isEqualTo("일반 회원");
    }

    @Test
    @DisplayName("Get All Roles Empty")
    void getAllRoleTest2() {
        List<Role> roleList = new ArrayList<>();
        Mockito.when(roleRepository.findAll()).thenReturn(roleList);

        List<RoleResponseDto> result = roleService.getAllRoles();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Register Role Successfully")
    void registerRoleTest() {
        RoleResponseDto roleResponseDto = new RoleResponseDto(1, "MEMBER", "일반 회원");

        Mockito.when(roleRepository.existsByName("MEMBER")).thenReturn(false);
        Mockito.when(roleRepository.save(Mockito.any())).thenReturn(Role.from(roleResponseDto));

        RoleResponseDto result = roleService.registerRole(new RoleRegisterRequestDto("MEMBER", "일반회원"));

        Mockito.verify(roleRepository, Mockito.times(1)).save(Mockito.any(Role.class));

        Assertions.assertThat(result.id()).isEqualTo(roleResponseDto.id());
        Assertions.assertThat(result.name()).isEqualTo("MEMBER");
        Assertions.assertThat(result.description()).isEqualTo("일반 회원");
    }

    @Test
    @DisplayName("Register Role Failed 1 - DuplicateName")
    void failedRegisterRoleDuplicateNameTest() {
        Mockito.when(roleRepository.existsByName(Mockito.anyString())).thenReturn(true);

        assertThrows(RoleIllegalArgumentException.class, () -> {
            roleService.registerRole(new RoleRegisterRequestDto("MEMBER", "일반회원"));
        });
    }

    @Test
    @DisplayName("Register Role Failed 2 - DB error")
    void failedRegisterRoleDBErrorTest() {
        Mockito.when(roleRepository.save(Mockito.any())).thenThrow(DataIntegrityViolationException.class);

        assertThrows(RoleIllegalArgumentException.class, () -> {
            roleService.registerRole(new RoleRegisterRequestDto("MEMBER", "일반회원"));
        });
    }

    @Test
    @DisplayName("Modify Role Successfully")
    void modifyRoleTest() {
        RoleResponseDto roleResponseDto = new RoleResponseDto(1, "수정", "수정 설명");
        Mockito.when(roleRepository.findById(1)).thenReturn(Optional.of(Role.from(roleResponseDto)));

        RoleResponseDto result = roleService.modifyRole(1, new RoleModifyRequestDto("수정", "수정 설명"));

        Mockito.verify(roleRepository, Mockito.times(1)).findById(1);

        Assertions.assertThat(result.id()).isEqualTo(1);
        Assertions.assertThat(result.name()).isEqualTo("수정");
        Assertions.assertThat(result.description()).isEqualTo("수정 설명");
    }

    @Test
    @DisplayName("Modify Role Failed 1 - isDuplicateName")
    void failedModifyRoleDuplicateNameTest() {
        Mockito.when(roleRepository.existsByName(Mockito.anyString())).thenReturn(true);

        assertThrows(RoleIllegalArgumentException.class, () -> {
            roleService.modifyRole(1, new RoleModifyRequestDto("등급", "등급 설명"));
        });
    }

//    @Test
//    @DisplayName("Modify Role Failed 2 - DB error")
//    void failedModifyRoleDBErrorTest() {
//        Mockito.when(roleRepository.findById(1)).thenReturn(Optional.of(Role.createRole("등급", "등급 설명")));
//        Mockito.when(roleRepository.save(Mockito.any(Role.class))).thenThrow(DataIntegrityViolationException.class);
//
//        assertThrows(DataIntegrityViolationException.class, () -> {
//            roleService.modifyRole(1, new RoleModifyRequestDto("등급", "등급 설명"));
//        });
//    }

    @Test
    @DisplayName("Remove Role Successfully")
    void removeRoleTest() {
        Mockito.when(roleRepository.existsById(1)).thenReturn(true);

        roleService.deleteRole(1);

        Mockito.verify(roleRepository, Mockito.times(1)).deleteById(1);
    }

    @Test
    @DisplayName("Remove Role Failed - Role doesn't exist")
    void failedRemoveRoleFailedTest() {
        Mockito.when(roleRepository.existsById(1)).thenReturn(false);

        assertThrows(RoleIllegalArgumentException.class, () -> roleService.deleteRole(1));
    }


}
