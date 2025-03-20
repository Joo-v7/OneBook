package com.nhnacademy.taskapi.author.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorUpdateDTO {
    @NotNull(message = "authorId는 필수입니다 ! ")
    private int authorId;
    @NotBlank(message = "authorName은 필수입니다 !")
    @Length(max = 20, message = "authorName은 최대 20자까지 가능합니다.")
    private String authorName;



}
