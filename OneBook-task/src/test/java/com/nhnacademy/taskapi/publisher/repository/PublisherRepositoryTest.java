package com.nhnacademy.taskapi.publisher.repository;


import com.nhnacademy.taskapi.config.QuerydslConfig;
import com.nhnacademy.taskapi.publisher.domain.Publisher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)

public class PublisherRepositoryTest {
    @Autowired
    private PublisherRepository publisherRepository;


    @Test
    void findByNameTest(){
        Publisher publisher = new Publisher();
        publisher.setName("Test");
        publisherRepository.save(publisher);

        Publisher result = publisherRepository.findByName("Test");

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test");
    }
}
