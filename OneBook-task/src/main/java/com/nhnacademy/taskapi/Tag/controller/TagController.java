package com.nhnacademy.taskapi.Tag.controller;

import com.nhnacademy.taskapi.Tag.exception.ValidationFailedException;
import com.nhnacademy.taskapi.Tag.service.TagService;
import com.nhnacademy.taskapi.Tag.dto.request.CreateTagRequest;
import com.nhnacademy.taskapi.Tag.dto.request.UpdateTagRequest;
import com.nhnacademy.taskapi.Tag.dto.response.TagResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/task/tags")
@Tag(name = "Tag", description = "태그를 등록, 조회, 수정, 삭제")
public class TagController {

    private final TagService tagService;

    @GetMapping("/page")
    public ResponseEntity<Page<TagResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(tagService.getAllTags(pageable));
    }

    @GetMapping
    public ResponseEntity<List<TagResponse>> findAll() {
        return ResponseEntity.ok(tagService.getAllTags());
    }


    @GetMapping("/{tagId}")
    public ResponseEntity<TagResponse> find(@PathVariable Long tagId) {
        return ResponseEntity.ok(tagService.getTag(tagId));
    }


    @PostMapping
    public ResponseEntity<TagResponse> create(@RequestBody @Valid CreateTagRequest createTagRequest) {
        return ResponseEntity.ok(tagService.createTag(createTagRequest));
    }


    @PutMapping
    public ResponseEntity<TagResponse> update(@RequestBody @Valid UpdateTagRequest updateTagRequest, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        return ResponseEntity.ok(tagService.updateTag(updateTagRequest));
    }


    @DeleteMapping("/{tagId}")
    public ResponseEntity<Void> delete(@PathVariable Long tagId) {
        tagService.removeTag(tagId);

        return ResponseEntity.noContent().build();
    }
}
