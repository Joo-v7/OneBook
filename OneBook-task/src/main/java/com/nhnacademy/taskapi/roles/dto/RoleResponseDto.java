package com.nhnacademy.taskapi.roles.dto;

import com.nhnacademy.taskapi.roles.domain.Role;

public record RoleResponseDto(
        Integer id,
        String name,
        String description
)
{
    public static RoleResponseDto from(Role role) {
        return new RoleResponseDto(
                role.getId(),
                role.getName(),
                role.getDescription()
        );
    }
}