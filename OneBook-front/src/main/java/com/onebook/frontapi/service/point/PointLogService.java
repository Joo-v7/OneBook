//package com.onebook.frontapi.service.point;
//
//import com.onebook.frontapi.dto.point.response.PointLogResponse;
//import com.onebook.frontapi.feign.point.PointFeignClient;
//import lombok.AllArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//
//@Service
//@AllArgsConstructor
//public class PointLogService {
//    private final PointFeignClient pointFeignClient;
//
//
//    public Page<PointLogResponse> getPointLogs(Pageable pageable) {
//        ResponseEntity<Page<PointLogResponse>> response =
//                pointFeignClient.getUserPointLogs(pageable);
//        return response.getBody();
//    }
//}
