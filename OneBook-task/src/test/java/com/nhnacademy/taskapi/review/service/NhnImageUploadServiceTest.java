package com.nhnacademy.taskapi.review.service;

import com.nhnacademy.taskapi.adapter.NhnImageManagerAdapter;
import com.nhnacademy.taskapi.review.service.impl.NhnImageUploadServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class NhnImageUploadServiceTest {

    @Mock
    private NhnImageManagerAdapter nhnImageManagerAdapter;

    @InjectMocks
    private NhnImageUploadServiceImpl nhnImageUploadService;

    private byte[] imageBytes;
    private String fileName;
    private long bookId;
    private String loginId;

    @BeforeEach
    void setUp() {
        imageBytes = "test_image".getBytes();
        fileName = "test_image.jpg";
        bookId = 123L;
        loginId = "testUser";
    }

    /**
     *  정상적으로 이미지 업로드가 성공하는 경우
     */
    @Test
    void testUploadImageSuccess() throws IOException {
        // given
        String expectedUrl = "http://test-url.com/test_image.jpg";
        given(nhnImageManagerAdapter.uploadReviewImage(imageBytes, fileName, bookId, loginId))
                .willReturn(expectedUrl);

        // when
        String actualUrl = nhnImageUploadService.uploadImage(imageBytes, fileName, bookId, loginId);

        // then
        assertEquals(expectedUrl, actualUrl);

        verify(nhnImageManagerAdapter, times(1))
                .uploadReviewImage(imageBytes, fileName, bookId, loginId);
    }

    /**
     *  이미지 업로드 중 IOException 발생
     */
    @Test
    void testUploadImageThrowsIOException() throws IOException {
        // given
        IOException expectedException = new IOException("Upload failed");
        given(nhnImageManagerAdapter.uploadReviewImage(any(byte[].class), anyString(), anyLong(), anyString()))
                .willThrow(expectedException);

        // when & then
        IOException thrown = assertThrows(IOException.class, () ->
                nhnImageUploadService.uploadImage(imageBytes, fileName, bookId, loginId)
        );
        assertEquals("Upload failed", thrown.getMessage());
        verify(nhnImageManagerAdapter, times(1))
                .uploadReviewImage(imageBytes, fileName, bookId, loginId);
    }
}
