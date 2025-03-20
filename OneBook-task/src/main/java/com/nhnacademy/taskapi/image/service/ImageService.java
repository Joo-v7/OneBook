package com.nhnacademy.taskapi.image.service;

import com.nhnacademy.taskapi.image.domain.Image;
import com.nhnacademy.taskapi.image.dto.ImageSaveDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {

    // 이미지 클라우드에 업로드
    Image saveImage (ImageSaveDTO dto);
    void deleteImage(long imageId);
    Image getImage(long bookId);
}