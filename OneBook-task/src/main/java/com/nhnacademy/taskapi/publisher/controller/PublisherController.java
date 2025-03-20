package com.nhnacademy.taskapi.publisher.controller;

import com.nhnacademy.taskapi.publisher.domain.Publisher;
import com.nhnacademy.taskapi.publisher.dto.PublisherUpdateDTO;
import com.nhnacademy.taskapi.publisher.service.PublisherService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/task/publisher")
@Tag(name = "Publisher", description = "출판사를 등록, 조회, 수정, 삭제")
public class PublisherController {
    private final PublisherService publisherService;


    @PostMapping("/{publisherName}")
    public ResponseEntity<Publisher> createPublisher(@PathVariable String publisherName){
        Publisher publisher = publisherService.addPublisher(publisherName);
        return ResponseEntity.ok().body(publisher);
    }

    @PutMapping
    public ResponseEntity<Publisher> modifyPublisher(@RequestBody PublisherUpdateDTO dto){
        Publisher publisher = publisherService.updatePublisher(dto);
        return ResponseEntity.ok().body(publisher);
    }


    @DeleteMapping("/{publisherId}")
    public ResponseEntity<Void> deletePublisher(@PathVariable("publisherId") long publisherId){
        publisherService.deletePublisher(publisherId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{publisherName}")
    public ResponseEntity<Publisher> getPublisher(@PathVariable("publisherName") String publisherName){
        Publisher publisher = publisherService.getPublisher(publisherName);
        return ResponseEntity.status(HttpStatus.OK).body(publisher);
    }


    @GetMapping("/list/{publisherName}")
    public ResponseEntity<List<Publisher>> getPublisherList(@PathVariable("publisherName") String name) {

        List<Publisher> publisherList = publisherService.getPublisherList(name);
        return ResponseEntity.status(HttpStatus.OK).body(publisherList); // 성공 시 200 OK와 함께 목록 반환


    }

    @GetMapping("/list")
    public ResponseEntity<Page<Publisher>> getAllPublishers(Pageable pageable) {
        Page<Publisher> publishers = publisherService.getList(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(publishers);
    }


    @GetMapping
    public ResponseEntity<Publisher> getPublisher(@RequestParam(value = "publisherId") long publisherId) {
        Publisher publisher = publisherService.getPublisherById(publisherId);
        return ResponseEntity.status(HttpStatus.OK).body(publisher);
    }



}
