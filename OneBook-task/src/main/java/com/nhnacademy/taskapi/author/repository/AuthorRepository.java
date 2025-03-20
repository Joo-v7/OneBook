package com.nhnacademy.taskapi.author.repository;

import com.nhnacademy.taskapi.author.domain.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
    Author findByName(String name);
    Page<Author> findAllByNameOrderByAuthorId(Pageable pageable, String name);
    Page<Author> findAllByOrderByAuthorIdAsc(Pageable pageable);
}
