package com.nhnacademy.taskapi.author.controller;


import com.nhnacademy.taskapi.author.domain.Author;
import com.nhnacademy.taskapi.author.dto.AuthorUpdateDTO;
import com.nhnacademy.taskapi.author.service.AuthorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/task/author")
@Tag(name = "Author", description = "작가를 생성,수정,조회,삭제")  // API 그룹 설명 추가
public class AuthorController {
    private final AuthorService authorService;

    @PostMapping("/{authorName}")
    public ResponseEntity<Author> createAuthor(@PathVariable String authorName){
        Author author = authorService.addAuthor(authorName);
        return ResponseEntity.ok().body(author);
    }

    @PutMapping
    public ResponseEntity<Author> modifyAuthor(@RequestBody @Valid AuthorUpdateDTO dto){
        Author author = authorService.updateAuthor(dto);
        return ResponseEntity.ok().body(author);
    }

    @DeleteMapping("/{authorId}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable int authorId){
        authorService.deleteAuthor(authorId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{authorId}")
    public ResponseEntity<Author> getAuthor(@PathVariable int authorId){
        Author author = authorService.getAuthor(authorId);
        return ResponseEntity.ok().body(author);
    }

    @GetMapping("/authorList")
    public ResponseEntity<Page<Author>> getAuthorList(Pageable pageable,
                                                      @RequestParam String name){
        Page<Author> authors = authorService.getAuthorList(pageable, name);
        return ResponseEntity.ok().body(authors);
    }

    @GetMapping("/list")
    public ResponseEntity<Page<Author>> getAuthorList(Pageable pageable){
        Page<Author> authors = authorService.getAllAuthorList(pageable);
        return ResponseEntity.ok().body(authors);
    }

}
