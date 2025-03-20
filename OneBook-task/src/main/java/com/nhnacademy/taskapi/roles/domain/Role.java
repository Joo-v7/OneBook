package com.nhnacademy.taskapi.roles.domain;

import com.nhnacademy.taskapi.roles.dto.RoleResponseDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "roles")
public class Role {

    /**
     * 역할 ID
     * 1: MEMBER
     * 2: ADMIN
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Column(unique = true)
    private String name;

    @NotBlank
    private String description;

    public static Role createRole(String name, String description) {
        return new Role(name, description);
    }

    public static Role from(RoleResponseDto roleResponseDto) {
        return new Role(
                roleResponseDto.id(),
                roleResponseDto.name(),
                roleResponseDto.description()
        );
    }

    public void modifyRole(String name, String description) {
        this.name = name;
        this.description = description;
    }

    private Role(String name, String description) {
        this.name = name;
        this.description = description;
    }

}
