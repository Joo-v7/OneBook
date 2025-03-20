package com.onebook.frontapi.controller.packaging;

import com.onebook.frontapi.adaptor.packaging.PackagingResponseAdaptor;
import com.onebook.frontapi.dto.packaging.PackagingRequest;
import com.onebook.frontapi.service.packaging.PackagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class PackagingController {
    private final PackagingService packagingService;

    @GetMapping("/packagings")
    public String viewPackagings(Model model) {
        List<PackagingResponseAdaptor> packagingList = packagingService.getAllPackaging();

        model.addAttribute("packagingList", packagingList);
        return "packaging/packagings";
    }

    @GetMapping("/admin/packaging")
    public String registerPackaging() {
        return "admin/packagingRegister";
    }

    @PostMapping("/admin/packaging")
    public String registerPackaging(@ModelAttribute PackagingRequest packagingRequest) {
        packagingService.createPackaging(packagingRequest);

        return "redirect:/admin/packaging";
    }
}