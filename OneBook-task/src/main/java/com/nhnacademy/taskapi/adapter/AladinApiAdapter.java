package com.nhnacademy.taskapi.adapter;


import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AladinApiAdapter {
    private final RestTemplate restTemplate;

    public AladinApiAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String fetchAladinData(String url){
        return restTemplate.getForObject(url, String.class);
    }
}
