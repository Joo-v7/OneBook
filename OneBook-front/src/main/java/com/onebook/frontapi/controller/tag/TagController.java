package com.onebook.frontapi.controller.tag;

import com.onebook.frontapi.dto.tag.CreateTagRequest;
import com.onebook.frontapi.dto.tag.TagResponse;
import com.onebook.frontapi.dto.tag.UpdateTagRequest;
import com.onebook.frontapi.service.tag.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;


    @GetMapping
    public String createTag() {
        return "tag/createForm";
    }

    @GetMapping("/{tagId}")
    public String getTags(@PathVariable long tagId, Model model){
        TagResponse tag = tagService.getTag(tagId);

        model.addAttribute("tag", tag);
        return "tag/updateForm";
    }


    @PostMapping
    public String createTag(@RequestParam(value = "name") String name) {
        CreateTagRequest request = new CreateTagRequest(name);
        tagService.createTag(request);
        return "redirect:/";
    }


    @GetMapping("/list")
    public String listTags(@RequestParam(defaultValue = "0") int page,
                           Model model) {

        Page<TagResponse> tags = tagService.allTags(PageRequest.of(page, 20));

        model.addAttribute("tags", tags);

        return "tag/tagList";
    }

    @GetMapping("/tagList")
    public String getTags(@RequestParam(defaultValue = "0") int page,
                           Model model) {

        Page<TagResponse> tags = tagService.allTags(PageRequest.of(page, 10));

        model.addAttribute("tags", tags);

        return "tag/tagList_book";
    }


    @PutMapping("/update")
    public String updateTag(@RequestParam(value = "tagId") long tagId,
                            @RequestParam(value = "tagName") String name) {
        UpdateTagRequest request = new UpdateTagRequest(tagId, name);
        tagService.updateTag(request);
        return "redirect:/";
    }

    @DeleteMapping("/delete")
    public String deleteTag(@RequestParam(value = "tagId") long tagId) {
        tagService.deleteTag(tagId);
        return "redirect:/";
    }
}
