//package com.nhnacademy.taskapi.tagTest;
//
//import com.nhnacademy.taskapi.Tag.domain.Tag;
//import com.nhnacademy.taskapi.Tag.dto.request.CreateTagRequest;
//import com.nhnacademy.taskapi.Tag.dto.request.UpdateTagRequest;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class TagDtoTest {
//
//    @Test
//    void createTagRequestToEntity() {
//        CreateTagRequest createTagRequest = new CreateTagRequest("testTag");
//
//        Tag tag = createTagRequest.toEntity();
//
//        assertNotNull(tag);
//        assertEquals("testTag", tag.getName());
//    }
//
//    @Test
//    void updateTagRequestToEntity() {
//        UpdateTagRequest updateTagRequest = new UpdateTagRequest(1L, "updatedTag");
//
//        Tag tag = updateTagRequest.toEntity();
//
//        assertNotNull(tag);
//        assertEquals(1L, tag.getTagId());
//        assertEquals("updatedTag", tag.getName());
//    }
//}