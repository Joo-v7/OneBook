package com.onebook.frontapi.feign.packaging;

import com.onebook.frontapi.adaptor.packaging.PackagingResponseAdaptor;
import com.onebook.frontapi.dto.packaging.PackagingCreateDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "packagingClient", url = "${onebook.gatewayUrl}")
public interface PackagingClient {
    @GetMapping("/task/packagings")
    List<PackagingResponseAdaptor> getAllPackaging();

    @PostMapping("/task/admin/packaging")
    void createPackaging(PackagingCreateDto packagingCreateDto);
}