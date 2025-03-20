package com.nhnacademy.taskapi.image.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImageSaveDTO {
    @NotBlank
    private byte[] imageBytes;
    @NotNull
    private long bookId;
    @NotBlank
    private String imageName;
}
