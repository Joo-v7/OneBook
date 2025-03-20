package com.nhnacademy.taskapi.adapter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


// feignClient 예시
@FeignClient(name = "account-service", path = "/account")
public interface AccountClient {
    @GetMapping("/{id}")
    String getAccountInfo(@PathVariable("id") String id);
}
