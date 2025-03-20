package com.onebook.frontapi.controller.author;

import com.onebook.frontapi.dto.author.AuthorDTO;
import com.onebook.frontapi.service.author.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/authors")
public class AdminAuthorController {
    private final AuthorService authorService;

    @GetMapping
    public String getAuthors(@RequestParam(defaultValue = "0")int page,
                             Model model) {
        Page<AuthorDTO> authors = authorService.getAuthors(PageRequest.of(page, 10));
        model.addAttribute("authors", authors);
        return "author/list";
    }
}
