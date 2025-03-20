package com.nhnacademy.taskapi.review.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "review_images")
public class ReviewImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long imageId; // auto_increment

    @Column(nullable = true, length = 255)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;
}

