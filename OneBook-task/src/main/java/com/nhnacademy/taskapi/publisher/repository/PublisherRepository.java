package com.nhnacademy.taskapi.publisher.repository;

import com.nhnacademy.taskapi.publisher.domain.Publisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    Publisher findByName(String name);
    boolean existsByName(String name);
    List<Publisher> findAllByName(String name);
    Page<Publisher> findAllByOrderByPublisherIdAsc(Pageable pageable);
}
