package com.nhnacademy.taskapi.review.service.impl;

import com.nhnacademy.taskapi.adapter.NhnImageManagerAdapter;
import com.nhnacademy.taskapi.review.service.ImageUploadService;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class NhnImageUploadServiceImpl implements ImageUploadService {
    private final NhnImageManagerAdapter nhnImageManagerAdapter;

    public NhnImageUploadServiceImpl(NhnImageManagerAdapter nhnImageManagerAdapter) {
        this.nhnImageManagerAdapter = nhnImageManagerAdapter;
    }

    @Override
    public String uploadImage(byte[] imageBytes, String fileName, long bookId, String loginId) throws IOException {
        return nhnImageManagerAdapter.uploadReviewImage(imageBytes, fileName, bookId, loginId);
    }
}
