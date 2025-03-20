package com.nhnacademy.taskapi.author.service;

import com.nhnacademy.taskapi.author.domain.Author;
import com.nhnacademy.taskapi.author.dto.AuthorUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AuthorService {
    Author addAuthor(String name);
    Author updateAuthor(AuthorUpdateDTO dto);
    void deleteAuthor(int authorId);
    Author getAuthor(int authorId);
    Page<Author> getAuthorList(Pageable pageable, String authorName);
    Author addAuthorByAladin(String name);
    Page<Author> getAllAuthorList(Pageable pageable);
}
