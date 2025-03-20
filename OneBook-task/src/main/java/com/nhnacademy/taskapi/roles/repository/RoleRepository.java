package com.nhnacademy.taskapi.roles.repository;

import com.nhnacademy.taskapi.roles.domain.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    boolean existsByName(String name);
    Page<Role> findAll(Pageable pageable);
}
