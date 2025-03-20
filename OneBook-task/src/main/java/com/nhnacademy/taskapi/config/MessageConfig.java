package com.nhnacademy.taskapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

public class MessageConfig {

    @Bean
    public LocaleResolver localResolver() {
        // messageSource의 기본 언어를 한국어로 설정
        SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
        sessionLocaleResolver.setDefaultLocale(Locale.KOREA);

        return sessionLocaleResolver;
    }
}