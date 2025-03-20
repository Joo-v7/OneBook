package com.nhnacademy.taskapi.Tag.service.Impl;

import com.nhnacademy.taskapi.Tag.domain.Tag;
import com.nhnacademy.taskapi.Tag.exception.TagNotFoundException;
import com.nhnacademy.taskapi.Tag.jpa.JpaBookTagRepository;
import com.nhnacademy.taskapi.Tag.jpa.JpaTagRepository;
import com.nhnacademy.taskapi.Tag.repository.TagRepository;
import com.nhnacademy.taskapi.Tag.service.TagService;
import com.nhnacademy.taskapi.Tag.dto.request.CreateTagRequest;
import com.nhnacademy.taskapi.Tag.dto.request.UpdateTagRequest;
import com.nhnacademy.taskapi.Tag.dto.response.TagResponse;
import com.nhnacademy.taskapi.book.domain.BookTag;
import com.nhnacademy.taskapi.Tag.exception.ApplicationException;  // ApplicationException 임포트
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final JpaTagRepository jpaTagRepository;
    private final JpaBookTagRepository jpaBookTagRepository;

    static String exceptionTitle = "요청 값이 비어있습니다.";

    @Override
    @Transactional
    public Tag addTag(String tagName) {
        Tag tag  = tagRepository.findByName(tagName);

        if(Objects.isNull(tag)){
            tag = new Tag();
            tag.setName(tagName);
            return tagRepository.save(tag);
        }
        return tag;
    }
    @Transactional
    @Override
    public TagResponse createTag(CreateTagRequest createTagRequest) {
        if (Objects.isNull(createTagRequest)) {
            throw new ApplicationException(exceptionTitle, 400);  // ApplicationException 사용
        }

        return TagResponse.fromEntity(jpaTagRepository.save(createTagRequest.toEntity()));
    }

    @Override
    public TagResponse getTag(Long tagId) {
        if (Objects.isNull(tagId)) {
            throw new ApplicationException(exceptionTitle, 400);  // ApplicationException 사용
        }

        Tag tag = jpaTagRepository.findById(tagId)
                .orElseThrow(() -> new ApplicationException("태그를 찾을 수 없습니다.", 404));  // ApplicationException 사용

        return TagResponse.fromEntity(tag);
    }

    @Override
    public List<TagResponse> getAllTags() {
        return jpaTagRepository.findAll().stream().map(TagResponse::fromEntity).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<TagResponse> getAllTags(Pageable pageable) {
        Page<Tag> tagPage = jpaTagRepository.findAll(pageable);
        List<TagResponse> responses = tagPage.stream().map(TagResponse::fromEntity).toList();

        return new PageImpl<>(responses, pageable, tagPage.getTotalElements());
    }

    @Transactional
    @Override
    public TagResponse updateTag(UpdateTagRequest updateTagRequest) {
        if (Objects.isNull(updateTagRequest)) {
            throw new ApplicationException(exceptionTitle, 400);  // ApplicationException 사용
        }

        if (!jpaTagRepository.existsById(updateTagRequest.tagId())) {
            throw new ApplicationException("태그 아이디가 존재하지 않습니다.", 404);  // ApplicationException 사용
        }

        return TagResponse.fromEntity(jpaTagRepository.save(updateTagRequest.toEntity()));
    }

    @Transactional
    @Override
    public void removeTag(Long tagId) {
        Tag tag = jpaTagRepository.findById(tagId)
                .orElseThrow(() -> new ApplicationException("태그를 찾을 수 없습니다.", 404));  // ApplicationException 사용

        List<BookTag> bookTagList = jpaBookTagRepository.findByTag(tag);

        jpaBookTagRepository.deleteAll(bookTagList);
        jpaTagRepository.deleteById(tagId);
    }

    // 태그검색
    @Override
    public Page<Tag> getTagByName(Pageable pageable, String name){
        Page<Tag> tags = tagRepository.findByNameContainingIgnoreCase(name, pageable);

        if(Objects.isNull(tags)){
            throw new TagNotFoundException("Tag not found.");
        }
        return tags;
    }
}