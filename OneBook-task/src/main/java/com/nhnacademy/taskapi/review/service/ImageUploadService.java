package com.nhnacademy.taskapi.review.service;

import java.io.IOException;

public interface ImageUploadService {
    String uploadImage(byte[] imageBytes, String fileName, long bookId, String loginId) throws IOException;
}
