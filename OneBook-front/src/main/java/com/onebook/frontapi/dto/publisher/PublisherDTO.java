package com.onebook.frontapi.dto.publisher;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class PublisherDTO {

    private long publisherId;


    private String name;
}
