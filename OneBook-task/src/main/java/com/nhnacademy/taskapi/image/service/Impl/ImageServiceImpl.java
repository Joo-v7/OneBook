package com.nhnacademy.taskapi.image.service.Impl;

import com.nhnacademy.taskapi.adapter.NhnImageManagerAdapter;
import com.nhnacademy.taskapi.book.domain.Book;
import com.nhnacademy.taskapi.book.exception.BookNotFoundException;
import com.nhnacademy.taskapi.book.repository.BookRepository;
import com.nhnacademy.taskapi.image.domain.Image;
import com.nhnacademy.taskapi.image.dto.ImageSaveDTO;
import com.nhnacademy.taskapi.image.exception.IllegalImageException;
import com.nhnacademy.taskapi.image.exception.IllegalImageNameException;
import com.nhnacademy.taskapi.image.exception.ImageNotFoundException;
import com.nhnacademy.taskapi.image.repository.ImageRepository;
import com.nhnacademy.taskapi.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageServiceImpl implements ImageService {
    private final NhnImageManagerAdapter nhnImageManagerAdapter;
    private final BookRepository bookRepository;
    private final ImageRepository imageRepository;


    @Transactional
    @Override
    public Image saveImage(ImageSaveDTO dto) {
        // 파일이 null이거나 비어있는지 확인
        if(dto.getImageBytes() == null || dto.getImageBytes() .length <= 0) {
            throw new IllegalImageException("Image bytes cannot be empty or null");
        }
        if(dto.getImageName() == null || dto.getImageName().isEmpty()) {
            throw new IllegalImageNameException("Image name cannot be empty or null");
        }

        Book book = bookRepository.findById(dto.getBookId()).get();



        // 클라우드 서비스에 이미지 업로드
        String imageUrl = null;
        try {
            imageUrl = nhnImageManagerAdapter.uploadImage(dto.getImageBytes(), dto.getImageName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 이미지 객체 생성 및 저장
        Image image = new Image();
        image.setBook(book);
        image.setName(dto.getImageName());
        image.setUrl(imageUrl);

        return imageRepository.save(image);
    }

    @Transactional
    @Override
    public void deleteImage(long imageId) {
        Image image = imageRepository.findById(imageId).orElseThrow(() -> new ImageNotFoundException("Image not found"));
        imageRepository.delete(image);
    }


    @Override
    public Image getImage(long bookId){
        Image image = imageRepository.findByBook_BookId(bookId);
        if(Objects.isNull(image)) {
            throw new ImageNotFoundException("Image not found");
        }

        return image;
    }
}