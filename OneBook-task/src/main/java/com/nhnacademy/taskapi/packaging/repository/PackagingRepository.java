package com.nhnacademy.taskapi.packaging.repository;

import com.nhnacademy.taskapi.packaging.entity.Packaging;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PackagingRepository extends JpaRepository<Packaging, Integer> {
    boolean existsByName(String name);

    Optional<Packaging> findByName(String name);
}