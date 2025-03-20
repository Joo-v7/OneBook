package com.nhnacademy.taskapi.packaging.dto;

import com.nhnacademy.taskapi.packaging.entity.Packaging;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PackagingResponseDto {
    int id;
    String name;
    int price;

    public static PackagingResponseDto fromPackaging(Packaging packaging) {
        return new PackagingResponseDto(
                packaging.getId(),
                packaging.getName(),
                packaging.getPrice()
        );
    }
}
