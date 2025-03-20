package com.nhnacademy.taskapi.adapter;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class AladinApiAdapterTest {

    @Test
    void testFetchAladinData() {
        // Given
        RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
        String url = "https://www.aladin.co.kr/ttb/api/ItemList.aspx?ttbkey=ttbtjswns12211534001&QueryType=Bestseller&MaxResults=50&start=1&SearchTarget=Book&output=js&Version=20131101";
        String mockResponse = "{ \"item\": [ { \"title\": \"Test Book\" } ] }";

        // RestTemplate의 getForObject 호출에 대해 Mock 동작 정의
        when(restTemplate.getForObject(url, String.class)).thenReturn(mockResponse);

        // AladinApiAdapter 생성
        AladinApiAdapter aladinApiAdapter = new AladinApiAdapter(restTemplate);

        // When
        String actualResponse = aladinApiAdapter.fetchAladinData(url);

        // Then
        assertEquals(mockResponse, actualResponse, "The response should match the mocked data.");
    }
}
