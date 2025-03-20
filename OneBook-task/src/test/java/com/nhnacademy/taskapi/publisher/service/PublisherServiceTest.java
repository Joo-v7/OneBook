package com.nhnacademy.taskapi.publisher.service;


import com.nhnacademy.taskapi.publisher.domain.Publisher;
import com.nhnacademy.taskapi.publisher.dto.PublisherUpdateDTO;
import com.nhnacademy.taskapi.publisher.exception.InvalidPublisherNameException;
import com.nhnacademy.taskapi.publisher.exception.PublisherAlreadyExistException;
import com.nhnacademy.taskapi.publisher.exception.PublisherNotFoundException;
import com.nhnacademy.taskapi.publisher.repository.PublisherRepository;
import com.nhnacademy.taskapi.publisher.service.serviceImpl.PublisherServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class
PublisherServiceTest {
    @Mock
    private PublisherRepository publisherRepository;

    @InjectMocks
    private PublisherServiceImpl publisherService;

    private Publisher publisher;



    @Test
    @DisplayName("addPublisher_Success")
    void addPubliser_Success() {
        publisher = new Publisher();
        publisher.setName("test");

        when(publisherRepository.findByName(any(String.class))).thenReturn(null);
        when(publisherRepository.save(any(Publisher.class))).thenReturn(publisher);

        Publisher result = publisherService.addPublisher("test");

        assertNotNull(result);
        assertEquals("test", result.getName());
        verify(publisherRepository).save(any(Publisher.class));
    }


    @Test
    @DisplayName("addPublisher_InvalidPublisherNameException_Empty")
    void addPubliser_Fail_EmptyName() {
        publisher = new Publisher();
        String name = "";
        publisher.setName(name);

        assertThrows(InvalidPublisherNameException.class, ()-> publisherService.addPublisher(name));
        verify(publisherRepository, never()).save(any(Publisher.class));
    }

    @Test
    @DisplayName("addPublisher_InvalidPublisherNameException_Null")
    void addPubliser_Fail_NullName() {
        publisher = new Publisher();
        String name = null;
        publisher.setName(name);

        assertThrows(InvalidPublisherNameException.class, ()-> publisherService.addPublisher(name));
        verify(publisherRepository, never()).save(any(Publisher.class));
    }

    @Test
    @DisplayName("addPublisher_PublisherAlreadyExistException")
    void addPublisher_Fail_PublisherAlreadyExist() {
        publisher = new Publisher();
        publisher.setName("test");
        publisherService.addPublisher("test");
        when(publisherRepository.findByName(any(String.class))).thenReturn(publisher);

        assertThrows(PublisherAlreadyExistException.class, ()-> publisherService.addPublisher("test"));
    }

    @Test
    @DisplayName("updatePublisher_Success")
    void updatePublisher_Success() {
        String UpdateName = "updateTest";

        Publisher publisher = new Publisher();
        publisher.setPublisherId(1L);
        publisher.setName("existTest");

        PublisherUpdateDTO dto = new PublisherUpdateDTO();

        dto.setPublisherId(publisher.getPublisherId());
        dto.setPublisherName(UpdateName);


        when(publisherRepository.findById(any(Long.class))).thenReturn(java.util.Optional.of(publisher));
        when(publisherRepository.save(any(Publisher.class))).thenReturn(publisher);

        Publisher result = publisherService.updatePublisher(dto);

        assertNotNull(result);
        assertEquals(UpdateName, result.getName());
        verify(publisherRepository).save(any(Publisher.class));
    }

    @Test
    @DisplayName("updatePublisher_Fail_PublisherNotFoundException")
    void updatePublisher_Fail_NotFoundPublisher() {
        PublisherUpdateDTO dto = new PublisherUpdateDTO();
        dto.setPublisherId(1L);
        dto.setPublisherName("updateTest");
        assertThrows(PublisherNotFoundException.class, () -> publisherService.updatePublisher(dto));
    }

    @Test
    @DisplayName("updatePublisher_Fail_InvalidPublisherNameException_Null")
    void updatePublisher_Fail_InvalidPublisherName() {
        String UpdateName = null;

        Publisher publisher = new Publisher();
        publisher.setPublisherId(1L);
        publisher.setName("existTest");

        PublisherUpdateDTO dto = new PublisherUpdateDTO();

        dto.setPublisherId(publisher.getPublisherId());
        dto.setPublisherName(UpdateName);


        when(publisherRepository.findById(any(Long.class))).thenReturn(java.util.Optional.of(publisher));

        assertThrows(InvalidPublisherNameException.class, () -> publisherService.updatePublisher(dto));
    }

    @Test
    @DisplayName("updatePublisher_Fail_InvalidPublisherNameException_Empty")
    void updatePublisher_Fail_InvalidPublisherName_Empty() {
        String UpdateName = "";

        Publisher publisher = new Publisher();
        publisher.setPublisherId(1L);
        publisher.setName("existTest");

        PublisherUpdateDTO dto = new PublisherUpdateDTO();

        dto.setPublisherId(publisher.getPublisherId());
        dto.setPublisherName(UpdateName);


        when(publisherRepository.findById(any(Long.class))).thenReturn(java.util.Optional.of(publisher));

        assertThrows(InvalidPublisherNameException.class, () -> publisherService.updatePublisher(dto));
    }


    @Test
    @DisplayName("deletePublisher_Success")
    void deletePublisher_Success() {
        long publisherId = 1L;
        publisher = new Publisher();
        publisher.setPublisherId(publisherId);
        publisher.setName("existTest");

        when(publisherRepository.findById(any(Long.class))).thenReturn(java.util.Optional.of(publisher));


        publisherService.deletePublisher(publisherId);
        verify(publisherRepository).delete(publisher);
    }


    @Test
    @DisplayName("getPublisher_Success")
    void getPublisher_Success() {
        Publisher publisher = new Publisher();
        publisher.setPublisherId(1L);
        publisher.setName("existTest");

        when(publisherRepository.findByName(any(String.class))).thenReturn(publisher);

        Publisher result = publisherService.getPublisher(publisher.getName());

        assertNotNull(result);
        verify(publisherRepository).findByName(any(String.class));
    }

    @Test
    @DisplayName("getPublisher_Fail_Null")
    void getPublisher_Fail_Null() {
        Publisher publisher = new Publisher();
        publisher.setPublisherId(1L);
        publisher.setName("existTest");

        when(publisherRepository.findByName(any(String.class))).thenReturn(null);

        assertThrows(PublisherNotFoundException.class, () -> publisherService.getPublisher(publisher.getName()));
    }

    @Test
    @DisplayName("getPublisherList_Success")
    void getPublisherList_Success() {
        Publisher publisher = new Publisher();
        publisher.setPublisherId(1L);
        publisher.setName("existTest");

        when(publisherRepository.findAllByName(any(String.class))).thenReturn(java.util.Arrays.asList(publisher));

        List<Publisher> result = publisherService.getPublisherList("existTest");
        assertNotNull(result);
        verify(publisherRepository).findAllByName(any(String.class));
    }

    @Test
    @DisplayName("getPublisherList_Fail_Null")
    void getPublisherList_Fail_Null() {
        String name = null;

        assertThrows(InvalidPublisherNameException.class, () -> publisherService.getPublisherList(name));
    }

    @Test
    @DisplayName("getPublisherList_Fail_Empty")
    void getPublisherList_Fail_EmptyName() {
        String name = "";

        assertThrows(InvalidPublisherNameException.class, () -> publisherService.getPublisherList(name));
    }

    @Test
    @DisplayName("Publisher가 없으면 새로 생성하여 반환")
    void testAddPublisherByAladin_whenPublisherNotExists() {
        // Arrange
        String publisherName = "Aladin Publisher";
        Publisher mockPublisher = new Publisher();
        mockPublisher.setName(publisherName);

        // Mocking
        when(publisherRepository.findByName(publisherName)).thenReturn(null);  // 없으면 null 반환
        when(publisherRepository.save(any(Publisher.class))).thenReturn(mockPublisher);  // 저장 후 mockPublisher 반환

        // Act
        Publisher result = publisherService.addPublisherByAladin(publisherName);

        // Assert
        assertNotNull(result);
        assertEquals(publisherName, result.getName());
        verify(publisherRepository, times(1)).findByName(publisherName);  // findByName 호출 확인
        verify(publisherRepository, times(1)).save(any(Publisher.class));  // save 호출 확인
    }

    @Test
    @DisplayName("Publisher가 이미 존재하면 기존 Publisher 반환")
    void testAddPublisherByAladin_whenPublisherExists() {
        // Arrange
        String publisherName = "Existing Publisher";
        Publisher existingPublisher = new Publisher();
        existingPublisher.setName(publisherName);

        // Mocking
        when(publisherRepository.findByName(publisherName)).thenReturn(existingPublisher);  // 이미 존재하는 publisher 반환

        // Act
        Publisher result = publisherService.addPublisherByAladin(publisherName);

        // Assert
        assertNotNull(result);
        assertEquals(publisherName, result.getName());
        verify(publisherRepository, times(1)).findByName(publisherName);  // findByName 호출 확인
        verify(publisherRepository, times(0)).save(any(Publisher.class));  // save 호출 안됨 확인
    }

    @Test
    @DisplayName("PublisherName이 빈 문자열이면 예외 발생")
    void testAddPublisherByAladin_whenPublisherNameIsNullOrEmpty() {
        // Arrange
        String invalidName = "";

        // Act & Assert
        assertThrows(InvalidPublisherNameException.class, () -> publisherService.addPublisherByAladin(invalidName));
    }

    @Test
    @DisplayName("PublisherName이 Null 문자열이면 예외 발생")
    void testAddPublisherByAladin_whenPublisherNameIsNull() {
        // Arrange
        String invalidName = null;

        // Act & Assert
        assertThrows(InvalidPublisherNameException.class, () -> publisherService.addPublisherByAladin(invalidName));
    }

}
