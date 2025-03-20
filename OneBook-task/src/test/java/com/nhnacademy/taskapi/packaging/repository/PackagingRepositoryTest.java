package com.nhnacademy.taskapi.packaging.repository;

import com.nhnacademy.taskapi.config.QuerydslConfig;
import com.nhnacademy.taskapi.packaging.entity.Packaging;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@DataJpaTest
@Import(QuerydslConfig.class)

class PackagingRepositoryTest {
    @Autowired
    PackagingRepository packagingRepository;

    // create
    @Test
    @DisplayName("포장지 저장")
    void savePackaging() {
        // given
        Packaging packaging = new Packaging("한복", 3000);

        // when
        Packaging savePackaging = packagingRepository.save(packaging);

        // then
        Optional<Packaging> findPackaging = packagingRepository.findById(savePackaging.getId());
        Assertions.assertTrue(findPackaging.isPresent());
        Assertions.assertEquals(packaging, findPackaging.get());
    }

    // read
    @Test
    @DisplayName("포장지 검색")
    void findPackagingByName() {
        // given
        Packaging packaging1 = new Packaging("한복", 3000);
        Packaging packaging2 = new Packaging("크리스마스", 3000);
        packagingRepository.save(packaging1);
        packagingRepository.save(packaging2);

        // when
        Optional<Packaging> findPackaging = packagingRepository.findByName("크리스마스");

        // then
        Assertions.assertTrue(findPackaging.isPresent());
        Assertions.assertEquals("크리스마스", findPackaging.get().getName());
        Assertions.assertEquals(3000, findPackaging.get().getPrice());
    }
}