package com.nhnacademy.taskapi.author.repository;

import com.nhnacademy.taskapi.author.domain.Author;
import com.nhnacademy.taskapi.config.QuerydslConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)
public class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void findByNameTest(){
        Author author = new Author();
        author.setName("Ronaldo");
        authorRepository.save(author);

        Author resultAuthor = authorRepository.findByName("Ronaldo");

        assertThat(resultAuthor).isNotNull();
        assertThat(resultAuthor.getName()).isEqualTo("Ronaldo");
    }
}
