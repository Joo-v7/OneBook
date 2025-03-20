package com.nhnacademy.taskapi.image.service;


import com.nhnacademy.taskapi.adapter.NhnImageManagerAdapter;
import com.nhnacademy.taskapi.book.domain.Book;
import com.nhnacademy.taskapi.book.repository.BookRepository;
import com.nhnacademy.taskapi.image.domain.Image;
import com.nhnacademy.taskapi.image.dto.ImageSaveDTO;
import com.nhnacademy.taskapi.image.exception.IllegalImageException;
import com.nhnacademy.taskapi.image.exception.IllegalImageNameException;
import com.nhnacademy.taskapi.image.exception.ImageNotFoundException;
import com.nhnacademy.taskapi.image.repository.ImageRepository;
import com.nhnacademy.taskapi.image.service.Impl.ImageServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Optional;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTest {
    @Mock
    private BookRepository bookRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private NhnImageManagerAdapter nhnImageManagerAdapter;

    @InjectMocks
    private ImageServiceImpl imageService;


    @Test
    @DisplayName("saveImage_Success")
    public void saveImage_Success() throws IOException {
        Book book = new Book();
        book.setBookId(1L);
        book.setTitle("Title");

        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.of(book));
        ImageSaveDTO imageSaveDTO = new ImageSaveDTO();
        imageSaveDTO.setBookId(1L);
        imageSaveDTO.setImageBytes(new byte[10]);
        imageSaveDTO.setImageName("imageName");

        when(nhnImageManagerAdapter.uploadImage(imageSaveDTO.getImageBytes(), imageSaveDTO.getImageName()))
                .thenReturn("http://test-image-url.com");
        String url = nhnImageManagerAdapter.uploadImage(imageSaveDTO.getImageBytes(), imageSaveDTO.getImageName());



        Image image = new Image();
        image.setBook(book);
        image.setName("imageName");
        image.setUrl(url);

        when(imageRepository.save(any(Image.class))).thenReturn(image);

        Image result = imageService.saveImage(imageSaveDTO);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(url, result.getUrl());
        verify(imageRepository).save(any(Image.class));
    }

    @Test
    @DisplayName("saveImage_Fail_IllegalImage_Null")
    public void saveImage_Fail_IllegalImage() throws IOException {
        ImageSaveDTO imageSaveDTO = new ImageSaveDTO();
        imageSaveDTO.setImageBytes(null);

        Assertions.assertThrows(IllegalImageException.class, () -> imageService.saveImage(imageSaveDTO));
    }

    @Test
    @DisplayName("saveImage_Fail_IllegalImage_Empty")
    public void saveImage_Fail_IllegalImage_Empty() throws IOException {
        ImageSaveDTO imageSaveDTO = new ImageSaveDTO();
        imageSaveDTO.setImageBytes(new byte[0]);


        Assertions.assertThrows(IllegalImageException.class, () -> imageService.saveImage(imageSaveDTO));
    }

    @Test
    @DisplayName("saveImage_Fail_ImageName_Null")
    public void saveImage_Fail_ImageName_Null() throws IOException {
        ImageSaveDTO imageSaveDTO = new ImageSaveDTO();
        imageSaveDTO.setImageBytes(new byte[10]);
        imageSaveDTO.setImageName(null);

        Assertions.assertThrows(IllegalImageNameException.class, () -> imageService.saveImage(imageSaveDTO));
    }

    @Test
    @DisplayName("saveImage_Fail_ImageName_Empty")
    public void saveImage_Fail_ImageName_Empty() throws IOException {
        ImageSaveDTO imageSaveDTO = new ImageSaveDTO();
        imageSaveDTO.setImageBytes(new byte[10]);
        imageSaveDTO.setImageName("");

        Assertions.assertThrows(IllegalImageNameException.class, () -> imageService.saveImage(imageSaveDTO));
    }

    @Test
    @DisplayName("saveImage_Fail_RuntimeException")
    public void saveImage_Fail_RuntimeException() throws IOException {
        ImageSaveDTO dto = new ImageSaveDTO();
        dto.setImageBytes(new byte[]{1, 2, 3});
        dto.setImageName("test.jpg");
        dto.setBookId(1L);

        when(bookRepository.findById(dto.getBookId())).thenReturn(Optional.of(new Book()));
        when(nhnImageManagerAdapter.uploadImage(dto.getImageBytes(), dto.getImageName()))
                .thenThrow(new IOException("Failed to upload image"));
        Assertions.assertThrows(RuntimeException.class, () -> imageService.saveImage(dto));
    }

    @Test
    @DisplayName("deleteImage_Success")
    public void deleteImage_Success() throws IOException {
        Image image = new Image();
        image.setImageId(1L);
        image.setBook(new Book());
        image.setName("imageName");
        image.setUrl("http://test-image-url.com");

        when(imageRepository.findById(any(Long.class))).thenReturn(Optional.of(image));

        imageService.deleteImage(1L);
        verify(imageRepository).delete(image);
    }

    @Test
    @DisplayName("deleteImage_Fail_NotFound")
    public void deleteImage_Fail_NotFound() throws IOException {
        when(imageRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(ImageNotFoundException.class, () -> imageService.deleteImage(1L));
    }

}
