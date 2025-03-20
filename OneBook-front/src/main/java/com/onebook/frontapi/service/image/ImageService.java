package com.onebook.frontapi.service.image;

import com.onebook.frontapi.dto.image.ImageDTO;
import com.onebook.frontapi.feign.image.ImageClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.*;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageClient imageClient;

    public ImageDTO getImage(long bookId) {
        return imageClient.getNewBooks(bookId);
    }
}
