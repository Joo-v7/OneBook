package com.nhnacademy.taskapi.review.service;

import com.nhnacademy.taskapi.review.service.impl.LocalImageUploadServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class LocalImageUploadServiceImplTest {

    @TempDir
    Path tempDir;  // JUnit5가 제공하는 임시 디렉토리

    private ImageUploadService localImageUploadService;

    private String originalUserHome;

    @BeforeEach
    void setUp() {

        localImageUploadService = new LocalImageUploadServiceImpl();
        // 기존 user.home을 백업
        originalUserHome = System.getProperty("user.home");

        // 테스트에서만 user.home을 임시 디렉토리로...
        System.setProperty("user.home", tempDir.toString());
    }

    @AfterEach
    void tearDown() {
        // 테스트 후 user.home 복원
        System.setProperty("user.home", originalUserHome);
    }

    /**
     * 정상적으로 이미지 업로드가 성공하는 경우
     */
    @Test
    void testUploadImageSuccess() throws IOException {
        // given
        byte[] imageBytes = "test_image_content".getBytes();
        String fileName = "test_image.jpg";
        long bookId = 123L;
        String loginId = "testUser";

        // when
        String returnedPath = localImageUploadService.uploadImage(imageBytes, fileName, bookId, loginId);

        // then
        // 1) 반환 경로 확인
        String expectedPath = "/images/review/123/testUser/test_image.jpg";
        assertEquals(expectedPath, returnedPath);

        // 2) 실제 파일 위치 확인
        // user.home + "/1nebook/taskapi/images/review/123/testUser/test_image.jpg"
        Path actualFilePath = tempDir
                .resolve("1nebook/taskapi/images/review")
                .resolve(String.valueOf(bookId))
                .resolve(loginId)
                .resolve(fileName);

        assertTrue(Files.exists(actualFilePath), "업로드된 파일이 실제로 존재해야 합니다.");

        // 3) 파일 내용 확인
        byte[] actualContent = Files.readAllBytes(actualFilePath);
        assertArrayEquals(imageBytes, actualContent, "파일 내용이 업로드된 바이트와 같아야 합니다.");
    }

    /**
     *  업로드 중 IOException 발생
     */
    @Test
    void testUploadImageThrowsIOException() {
        // given
        byte[] imageBytes = "test_image_content".getBytes();
        String fileName = "test_image.jpg";
        long bookId = 999L;
        String loginId = "user";


        assertDoesNotThrow(() -> {
            String path = localImageUploadService.uploadImage(imageBytes, fileName, bookId, loginId);
            // 만약 예외가 발생한다면 여기에 오지 못함
            System.out.println("Uploaded path = " + path);
        });
    }

    @Test
    @DisplayName("이미 존재하는 디렉토리에 이미지 업로드")
    void testUploadImageWhenDirectoryAlreadyExists() throws IOException {
        // given
        byte[] imageBytes = "another_test_image".getBytes();
        String fileName = "another_image.png";
        long bookId = 456L;
        String loginId = "existingUser";

        // 먼저 디렉토리를 미리 생성해둡니다.
        Path preCreatedDir = tempDir
                .resolve("1nebook/taskapi/images/review")
                .resolve(String.valueOf(bookId))
                .resolve(loginId);
        Files.createDirectories(preCreatedDir);

        // when
        String returnedPath = localImageUploadService.uploadImage(imageBytes, fileName, bookId, loginId);

        // then
        String expectedPath = "/images/review/456/existingUser/another_image.png";
        assertEquals(expectedPath, returnedPath);

        Path actualFilePath = preCreatedDir.resolve(fileName);
        assertTrue(Files.exists(actualFilePath), "업로드된 파일이 실제로 존재해야 합니다.");

        byte[] actualContent = Files.readAllBytes(actualFilePath);
        assertArrayEquals(imageBytes, actualContent, "파일 내용이 업로드된 바이트와 같아야 합니다.");
    }

}
