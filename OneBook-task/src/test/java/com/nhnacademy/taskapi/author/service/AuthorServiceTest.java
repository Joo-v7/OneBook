package com.nhnacademy.taskapi.author.service;


import com.nhnacademy.taskapi.author.domain.Author;
import com.nhnacademy.taskapi.author.dto.AuthorUpdateDTO;
import com.nhnacademy.taskapi.author.exception.AuthorNotFoundException;
import com.nhnacademy.taskapi.author.exception.InvalidAuthorNameException;
import com.nhnacademy.taskapi.author.repository.AuthorRepository;
import com.nhnacademy.taskapi.author.service.Impl.AuthorServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {
    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorServiceImpl authorService;

    private Author author;


    @Test
    @DisplayName("addAuthor_Success")
    void addAuthor_success(){
        author = new Author();
        author.setName("test");

        when(authorRepository.findByName("test")).thenReturn(null);
        when(authorRepository.save(any(Author.class))).thenReturn(author);

        Author result = authorService.addAuthor("test");

        assertNotNull(result);
        assertEquals("test", result.getName());
        verify(authorRepository).save(any(Author.class));
    }

    @Test
    @DisplayName("addAuthor_Fail_InvalidAuthorNameException_Null")
    void addAuthor_fail_InvalidAuthorNameException_null(){
        author = new Author();
        String name = null;
        author.setName(name);

        Assertions.assertThrows(InvalidAuthorNameException.class, ()-> authorService.addAuthor(name));
    }

    @Test
    @DisplayName("addAuthor_Fail_InvalidAuthorNameException_Empty")
    void addAuthor_fail_InvalidAuthorNameException(){
        author = new Author();
        String name = "";
        author.setName(name);

        Assertions.assertThrows(InvalidAuthorNameException.class, ()-> authorService.addAuthor(name));
    }

    @Test
    @DisplayName("updateAuthor_success")
    void updateAuthor_success(){
        String UpdateName = "updateTest";

        author = new Author();
        author.setAuthorId(1);
        author.setName("existTest");


        AuthorUpdateDTO dto = new AuthorUpdateDTO();
        dto.setAuthorId(author.getAuthorId());
        dto.setAuthorName(UpdateName);

        when(authorRepository.findById(any(Integer.class))).thenReturn(java.util.Optional.of(author));
        when(authorRepository.save(any(Author.class))).thenReturn(author);

        Author result = authorService.updateAuthor(dto);

        assertNotNull(result);
        assertEquals(UpdateName, result.getName());
        verify(authorRepository).save(any(Author.class));
    }

    @Test
    @DisplayName("updateAuthor_Fail_AuthorNotFoundException")
    void updateAuthor_fail_NotFoundAuthor(){
        AuthorUpdateDTO dto = new AuthorUpdateDTO();
        dto.setAuthorId(1);
        dto.setAuthorName("updateTest");

        Assertions.assertThrows(AuthorNotFoundException.class, () -> authorService.updateAuthor(dto));
    }

    @Test
    @DisplayName("updateAuthor_Fail_InvalidAuthorNameException_Null")
    void updateAuthor_fail_InvalidAuthorNameException_null(){
        String UpdateName = null;

        author = new Author();
        author.setAuthorId(1);
        author.setName("existTest");

        AuthorUpdateDTO dto = new AuthorUpdateDTO();
        dto.setAuthorId(author.getAuthorId());
        dto.setAuthorName(UpdateName);

        when(authorRepository.findById(any(Integer.class))).thenReturn(java.util.Optional.of(author));

        Assertions.assertThrows(InvalidAuthorNameException.class, () -> authorService.updateAuthor(dto));
    }

    @Test
    @DisplayName("updateAuthor_Fail_InvalidAuthorNameException_Empty")
    void updateAuthor_fail_InvalidAuthorNameException_empty(){
        String UpdateName = "";

        author = new Author();
        author.setAuthorId(1);
        author.setName("existTest");

        AuthorUpdateDTO dto = new AuthorUpdateDTO();
        dto.setAuthorId(author.getAuthorId());
        dto.setAuthorName(UpdateName);

        when(authorRepository.findById(any(Integer.class))).thenReturn(java.util.Optional.of(author));

        Assertions.assertThrows(InvalidAuthorNameException.class, () -> authorService.updateAuthor(dto));
    }

    @Test
    @DisplayName("deleteAuthor_success")
    void deleteAuthor_success(){
        int authorId = 1;
        author = new Author();
        author.setAuthorId(authorId);
        author.setName("existTest");

        when(authorRepository.findById(any(Integer.class))).thenReturn(java.util.Optional.of(author));

        authorService.deleteAuthor(authorId);
        verify(authorRepository).delete(author);
    }

    @Test
    @DisplayName("deleteAuthor_Fail_AuthorNotFoundException")
    void deleteAuthor_fail_NotFoundAuthor(){
        int authorId = 1;
        author = new Author();
        author.setAuthorId(authorId);
        author.setName("existTest");

        when(authorRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(AuthorNotFoundException.class, () ->authorService.deleteAuthor(authorId));
    }

    @Test
    @DisplayName("getAuthor_success")
    void getAuthor_success(){
        author = new Author();
        author.setAuthorId(1);
        author.setName("existTest");
        when(authorRepository.findById(any(Integer.class))).thenReturn(java.util.Optional.of(author));

        Author result = authorService.getAuthor(1);

        assertNotNull(result);
        assertEquals(author.getAuthorId(), result.getAuthorId());
        assertEquals(author.getName(), result.getName());
    }

    @Test
    @DisplayName("getAuthor_Fail_AuthorNotFoundException")
    void getAuthor_fail_NotFoundAuthor(){
        author = new Author();
        author.setAuthorId(1);
        author.setName("existTest");
        when(authorRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(AuthorNotFoundException.class, () ->authorService.getAuthor(1));
    }
}
