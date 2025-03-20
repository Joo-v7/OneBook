//package com.nhnacademy.taskapi.adapter;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.multipart.MultipartFile;
//
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//class NHNCloudImageManagerAdapterTest {
//
//    @Mock
//    private RestTemplate restTemplate;
//
//    private NHNCloudImageManagerAdapter imageManagerAdapter;
//
//    @BeforeEach
//    void setUp() {
//        // Mockito 초기화
//        MockitoAnnotations.openMocks(this);
//
//        // RestTemplate을 모킹하여 Adapter 생성자에 주입
//        imageManagerAdapter = new NHNCloudImageManagerAdapter(restTemplate);
//    }
//
//    @Test
//    void testUploadImageToImageManager() throws Exception {
//        // Mock MultipartFile
//        MultipartFile file = mock(MultipartFile.class);
//
//        // Mock RestTemplate response
//        String mockUrl = "http://image.toast.com/aaaacmr/onebook/bookImage/소년이_온다.jpg";
//        ResponseEntity<String> responseEntity = new ResponseEntity<>(mockUrl, HttpStatus.OK);
//
//        // RestTemplate exchange 메서드가 호출될 때 mock된 응답을 반환하도록 설정
//        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(String.class)))
//                .thenReturn(responseEntity);
//
//        // 호출
//        String result = imageManagerAdapter.uploadImageToImageManager(file);
//
//        // 검증
//        assertNotNull(result);
//        assertEquals(mockUrl, result);
//    }
//}
