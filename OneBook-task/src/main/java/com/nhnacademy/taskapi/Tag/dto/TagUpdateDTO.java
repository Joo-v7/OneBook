package com.nhnacademy.taskapi.Tag.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TagUpdateDTO {
    @NotNull
    private long tagId;
    @NotBlank
    private String tagName;
}