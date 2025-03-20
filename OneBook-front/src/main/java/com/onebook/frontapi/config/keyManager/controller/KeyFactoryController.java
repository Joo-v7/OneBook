//package com.onebook.frontapi.config.keyManager.controller;
//
//import com.onebook.frontapi.config.keyManager.dto.KeyResponseDto;
//import com.onebook.frontapi.config.keyManager.service.KeyFactoryManager;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RequiredArgsConstructor
//@RestController
//public class KeyFactoryController {
//    private final KeyFactoryManager keyFactoryManager;
//
//    @Value("${nhnKey.keyId}")
//    private String keyId;
//
//    @GetMapping("/keyFactory")
//    public KeyResponseDto keyFactory() {
//        return keyFactoryManager.keyInit(keyId);
//    }
//}
