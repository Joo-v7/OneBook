package com.nhnacademy.taskapi.review.service.impl;

import com.nhnacademy.taskapi.review.service.ImageUploadService;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class LocalImageUploadServiceImpl implements ImageUploadService {

    @Override
    public String uploadImage(byte[] imageBytes, String fileName, long bookId, String loginId) throws IOException {
        String uploadDir = System.getProperty("user.home") + "/1nebook/taskapi/images/review";

        File dir = new File(uploadDir + File.separator + bookId + File.separator + loginId);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(imageBytes);
        }

        return "/images/review/" + bookId + "/" + loginId + "/" + fileName;
    }
}
