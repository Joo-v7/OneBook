package com.nhnacademy.taskapi.publisher.service;

import com.nhnacademy.taskapi.publisher.domain.Publisher;
import com.nhnacademy.taskapi.publisher.dto.PublisherUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PublisherService {
    //출판사 등록
    Publisher addPublisher(String name);
    Publisher updatePublisher(PublisherUpdateDTO dto);
    void deletePublisher(long publisherId);
    Publisher getPublisher(String publisherName);
    Publisher addPublisherByAladin(String name);
    List<Publisher> getPublisherList(String name);
    Page<Publisher> getList(Pageable pageable);
    Publisher getPublisherById(long publisherId);
}
