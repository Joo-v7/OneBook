package com.onebook.frontapi.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


// 리뷰 작성 사진등록 -> 로컬에 등록됨
// -> 그 로컬에 등록된 사진을 보기 위한 코드
@Configuration
public class ReviewWebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String baseDir = System.getProperty("user.home") + "/1nebook/taskapi/images/review/";
        registry.addResourceHandler("/images/review/**")
                .addResourceLocations("file:" + baseDir);
    }
}
