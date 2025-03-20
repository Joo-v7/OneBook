package com.nhnacademy.taskapi.author.service.Impl;

import com.nhnacademy.taskapi.author.domain.Author;
import com.nhnacademy.taskapi.author.dto.AuthorUpdateDTO;
import com.nhnacademy.taskapi.author.exception.AuthorNotFoundException;
import com.nhnacademy.taskapi.author.exception.InvalidAuthorNameException;
import com.nhnacademy.taskapi.author.repository.AuthorRepository;
import com.nhnacademy.taskapi.author.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;


    // 작가등록
    @Transactional
    @Override
    public Author addAuthor(String name) {
        if(Objects.isNull(name) || name.trim().isEmpty()){
            throw new InvalidAuthorNameException("this AuthorName is Null Or Empty !");
        }
        Author author = null;
        if(Objects.isNull(authorRepository.findByName(name))){
            author = new Author();
            author.setName(name);
        }
        return authorRepository.save(author);

    }

    //작가 수정
    @Override
    @Transactional
    public Author updateAuthor(AuthorUpdateDTO dto){
        Author author = authorRepository.findById(dto.getAuthorId()).orElseThrow(() -> new AuthorNotFoundException("This Author Not Exist !"));

        if(Objects.isNull(dto.getAuthorName()) || dto.getAuthorName().trim().isEmpty()){
            throw new InvalidAuthorNameException("this AuthorName is Null Or Empty !");
        }

        author.setName(dto.getAuthorName());
        return authorRepository.save(author);
    }

    //작가 삭제
    @Override
    @Transactional
    public void deleteAuthor(int authorId){
        Author author = authorRepository.findById(authorId).orElseThrow(() -> new AuthorNotFoundException("This Author Not Exist !"));
        authorRepository.delete(author);
    }


    @Override
    public Author getAuthor(int authorId){

        Author author = authorRepository.findById(authorId).orElseThrow(()-> new AuthorNotFoundException("Author Not Found !"));
        return author;
    }



    @Override
    public Page<Author> getAuthorList(Pageable pageable, String authorName){
        if(Objects.isNull(authorName) || authorName.trim().isEmpty()){
            throw new InvalidAuthorNameException("this AuthorName is Null Or Empty !");
        }

        Page<Author> authors = authorRepository.findAllByNameOrderByAuthorId(pageable, authorName);

        if(Objects.isNull(authors)){
            throw new AuthorNotFoundException("This Author Not Exist !");
        }

        return authors;
    }

    @Override
    @Transactional
    public Author addAuthorByAladin(String name){
        if(Objects.isNull(name) || name.trim().isEmpty()){
            throw new InvalidAuthorNameException("this AuthorName is Null Or Empty !");
        }
        Author author = authorRepository.findByName(name);

        if(Objects.isNull(author)){
            author = new Author();
            author.setName(name);
            authorRepository.save(author);
        }
        return author;
    }

    @Override
    public Page<Author> getAllAuthorList(Pageable pageable){
        return authorRepository.findAllByOrderByAuthorIdAsc(pageable);
    }
}
