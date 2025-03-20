package com.onebook.frontapi.controller.point;

import com.onebook.frontapi.dto.point.response.PointLogPageResponseDto;
import com.onebook.frontapi.feign.point.PointFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class PointLogController {

    private final PointFeignClient pointFeignClient;

    @GetMapping("/point/logs")
    public String getPointLogs(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "15") int size,
            Model model
    ) {

        PointLogPageResponseDto logs = pointFeignClient.getPointLogs(page, size);

        model.addAttribute("pointLogs", logs);

        return "my/point/mypoint";
    }
}