package com.nhnacademy.taskapi.review.repository;

import com.nhnacademy.taskapi.review.domain.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {
}
