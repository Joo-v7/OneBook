package com.onebook.frontapi.feign.publisher;


import com.onebook.frontapi.dto.publisher.PublisherDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "publisherClient", url = "${onebook.gatewayUrl}")
public interface PublisherClient {


    @GetMapping("/task/publisher/list")
    Page<PublisherDTO> getPublisherList(Pageable pageable);

    @GetMapping("/task/publisher")
    PublisherDTO getPublisherById(@RequestParam(value = "publisherId") Long publisherId);

}