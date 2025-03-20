package com.nhnacademy.taskapi.adapter.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@ConfigurationProperties(prefix = "image")
@Getter
@Setter
public class ImageProperties {
    private String appkey;
    private String secretkey;
}
