//package com.nhnacademy.taskapi.tagTest;
//
//import com.nhnacademy.taskapi.Tag.domain.Tag;
//import com.nhnacademy.taskapi.Tag.jpa.JpaTagRepository;
//import com.nhnacademy.taskapi.Tag.repository.TagRepository;
//import com.nhnacademy.taskapi.Tag.exception.ApplicationException;
//import com.nhnacademy.taskapi.Tag.dto.request.CreateTagRequest;
//import com.nhnacademy.taskapi.Tag.dto.response.TagResponse;
//import com.nhnacademy.taskapi.Tag.service.Impl.TagServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//public class TagServiceImplTest {
//
//    @Mock
//    private TagRepository tagRepository;
//
//    @Mock
//    private JpaTagRepository jpaTagRepository;
//
//    @InjectMocks
//    private TagServiceImpl tagService;
//
//    @BeforeEach
//    void setup() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void createTag() {
//        CreateTagRequest request = new CreateTagRequest("testTag");
//
//        // tagId는 null로 설정됨
//        Tag tag = Tag.builder()
//                .tagId(null)  // tagId가 null인 상태로 설정
//                .name("testTag")
//                .build();
//
//        when(jpaTagRepository.save(any(Tag.class))).thenReturn(tag);
//
//        TagResponse response = tagService.createTag(request);
//        assertNotNull(response);
//        assertEquals("testTag", response.tagName());
//    }
//
//    @Test
//    void getTag() {
//        Tag tag = new Tag();
//        tag.setTagId(1L);
//        tag.setName("testTag");
//
//        when(jpaTagRepository.findById(1L)).thenReturn(java.util.Optional.of(tag));
//
//        TagResponse response = tagService.getTag(1L);
//        assertNotNull(response);
//        assertEquals("testTag", response.tagName());
//    }
//
//    @Test
//    void getTagNotFound() {
//        when(jpaTagRepository.findById(1L)).thenReturn(java.util.Optional.empty());
//
//        assertThrows(ApplicationException.class, () -> tagService.getTag(1L));
//    }
//}