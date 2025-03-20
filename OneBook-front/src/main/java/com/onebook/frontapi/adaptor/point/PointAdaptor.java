//package com.onebook.frontapi.adaptor.point;
//
//import com.onebook.frontapi.dto.point.response.PointLogResponse;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//
//import java.util.List;
//
//@FeignClient(name = "pointAdaptor", url = "${onebook.gatewayUrl}")
//public interface PointAdaptor {
//    @GetMapping("/admin/points/logs/{memberId}")
//    List<PointLogResponse> getPointLogsByMemberId(@PathVariable("memberId") Long memberId);
//}