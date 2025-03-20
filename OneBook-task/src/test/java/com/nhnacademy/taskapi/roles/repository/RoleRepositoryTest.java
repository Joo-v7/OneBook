package com.nhnacademy.taskapi.roles.repository;

import com.nhnacademy.taskapi.config.QuerydslConfig;
import com.nhnacademy.taskapi.roles.domain.Role;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Objects;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@DataJpaTest
@Import(QuerydslConfig.class)
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    @DisplayName("Find Role by ID")
    void findByIdTest() {
        Role role = Role.createRole("MEMBER", "일반 회원입니다.");
        Role savedRole = roleRepository.save(role);

        Role getRole = roleRepository.findById(savedRole.getId()).orElse(null);
        if(Objects.isNull(getRole)) {
            log.error("role을 찾을 수 없음");
        }
        assertThat(getRole.getName()).isEqualTo(savedRole.getName());
        assertThat(getRole.getDescription()).isEqualTo(savedRole.getDescription());
    }

    @Test
    @DisplayName("Save Role")
    void saveTest() {
        Role role = Role.createRole("MEMBER", "일반 회원입니다.");
        Role savedRole = roleRepository.save(role);

        assertThat(savedRole.getId()).isEqualTo(role.getId());
        assertThat(savedRole.getName()).isEqualTo(role.getName());
        assertThat(savedRole.getDescription()).isEqualTo(role.getDescription());
    }

    @Test
    @DisplayName("Update Role")
    void updateTest() {
        Role role = Role.createRole("MEMBER", "일반 회원입니다.");
        Role savedRole = roleRepository.save(role);

        savedRole.modifyRole("update", "수정");
        Role updatedRole = roleRepository.save(savedRole);

        assertThat(updatedRole.getName()).isEqualTo("update");
        assertThat(updatedRole.getDescription()).isEqualTo("수정");
    }

    @Test
    @DisplayName("Delete Role")
    void deleteByIdTest() {
        Role role = Role.createRole("MEMBER", "일반 회원입니다.");
        Role savedRole = roleRepository.save(role);

        roleRepository.deleteById(savedRole.getId());

        Role deletedRole = roleRepository.findById(savedRole.getId()).orElse(null);
        if(Objects.isNull(deletedRole)) {
            log.info("Role 삭제 테스트 성공");
        }else {
            log.error("Role 삭제 테스트 실패");
        }

    }


}
