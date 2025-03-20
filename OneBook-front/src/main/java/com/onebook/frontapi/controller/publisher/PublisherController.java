package com.onebook.frontapi.controller.publisher;


import com.onebook.frontapi.dto.publisher.PublisherDTO;
import com.onebook.frontapi.service.publisher.PublisherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/publishers")
@Slf4j
public class PublisherController {
    private final PublisherService publisherService;

    @GetMapping
    public String getPublishersList(@RequestParam(defaultValue = "0") int page,
                                    Model model) {
        log.info("Publisher Controller Invoked");
        Page<PublisherDTO> publishers = publisherService.getList(PageRequest.of(page, 10));
        model.addAttribute("publishers", publishers);
        return "publisher/publisherList";
    }


}
