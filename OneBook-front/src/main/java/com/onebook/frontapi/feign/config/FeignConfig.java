package com.onebook.frontapi.feign.config;

import com.onebook.frontapi.feign.interceptor.FeignRequestInterceptor;
import feign.Client;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.JsonFormWriter;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
public class FeignConfig {

//    @Bean
//    public FeignRequestInterceptor feignRequestInterceptor(HttpServletRequest request){
//        return new FeignRequestInterceptor(request);
//    }



    @Bean
    JsonFormWriter jsonFormWriter() {
        return new JsonFormWriter();
    }




}
