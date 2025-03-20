package com.onebook.frontapi.service.tag;


import com.onebook.frontapi.dto.tag.CreateTagRequest;
import com.onebook.frontapi.dto.tag.TagResponse;
import com.onebook.frontapi.dto.tag.UpdateTagRequest;
import com.onebook.frontapi.feign.tag.TagClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagClient tagClient;

    public TagResponse getTag(long tagId) {
        return tagClient.getTag(tagId);
    }

    public TagResponse createTag(CreateTagRequest tagRequest) {
        return tagClient.createTag(tagRequest);
    }

    public TagResponse updateTag(UpdateTagRequest tagRequest) {
        return tagClient.updateTag(tagRequest);
    }

    public Page<TagResponse> allTags(Pageable pageable) {
        return tagClient.findAllTags(pageable);
    }

    public void deleteTag(long tagId) {
        tagClient.deleteTag(tagId);
    }

}
