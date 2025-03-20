package com.onebook.frontapi.feign.tag;

import com.onebook.frontapi.dto.book.BookDTO;
import com.onebook.frontapi.dto.book.BookSaveDTO;
import com.onebook.frontapi.dto.tag.CreateTagRequest;
import com.onebook.frontapi.dto.tag.TagDTO;
import com.onebook.frontapi.dto.tag.TagResponse;
import com.onebook.frontapi.dto.tag.UpdateTagRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = "tagClient", url = "${onebook.gatewayUrl}")
public interface TagClient {

    @GetMapping("/task/tags/{tagId}")
    TagResponse getTag(@PathVariable("tagId") Long tagId);

    @PostMapping("/task/tags")
    TagResponse createTag(@RequestBody CreateTagRequest tagRequest);

    //태그 전체목록 조회
    @GetMapping("/task/tags/page")
    Page<TagResponse> findAllTags(Pageable pageable);


    @PutMapping("/task/tags")
    TagResponse updateTag(@RequestBody UpdateTagRequest tagRequest);

    @DeleteMapping("task/tags/{tagId}")
    void deleteTag(@PathVariable("tagId") Long tagId);

}
