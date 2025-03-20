package com.onebook.frontapi.dto.packaging;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Getter
@Setter
public class PackagingCreateDto {
    String name;
    int price;
}
