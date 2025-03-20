package com.nhnacademy.taskapi.Tag.jpa;

import com.nhnacademy.taskapi.Tag.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface JpaTagRepository extends JpaRepository<Tag, Long> {
//    List<Tag> findByLastModifiedAfter(LocalDateTime lastModified);
}