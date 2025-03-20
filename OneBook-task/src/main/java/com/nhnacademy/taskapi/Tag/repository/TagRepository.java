package com.nhnacademy.taskapi.Tag.repository;

import com.nhnacademy.taskapi.Tag.domain.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findByName(String name);
    Page<Tag> findByNameContainingIgnoreCase(String name, Pageable pageable);

}