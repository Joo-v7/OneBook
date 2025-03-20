package com.nhnacademy.authentication.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

//@Configuration
@Component
@ConfigurationProperties(prefix="jwt")
@Getter
@Setter
public class JwtProperties {
    private String secret;
    private Integer expirationTime;
    private String tokenPrefix;
    private String headerString;
    private String loginUrl;

}

