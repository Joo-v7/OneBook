//package com.nhnacademy.taskapi.tagTest;
//
//import com.nhnacademy.taskapi.Tag.domain.Tag;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class TagTest {
//
//    @Test
//    void createTagWithBuilder() {
//        Tag tag = Tag.builder()
//                .tagId(1L)
//                .name("testTag")
//                .build();
//
//        assertNotNull(tag);
//        assertEquals(1L, tag.getTagId());
//        assertEquals("testTag", tag.getName());
//    }
//
//    @Test
//    void createTagWithoutTagId() {
//        Tag tag = Tag.builder()
//                .name("noIdTag")
//                .build();
//
//        assertNotNull(tag);
//        // Now checking if tagId is null since the default value might be null
//        assertNull(tag.getTagId()); // Expecting null since tagId wasn't set
//        assertEquals("noIdTag", tag.getName());
//    }
//
//    @Test
//    void createTagWithNullName() {
//        Tag tag = Tag.builder()
//                .tagId(2L)
//                .name(null) // Explicitly setting name as null
//                .build();
//
//        assertNotNull(tag);
//        assertEquals(2L, tag.getTagId());
//        assertNull(tag.getName()); // Verifying that the name is null
//    }
//}