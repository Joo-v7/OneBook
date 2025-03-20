package com.nhnacademy.taskapi.book.domain;

import com.nhnacademy.taskapi.publisher.domain.Publisher;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long  bookId;


    // 출판사 - 책 단방향 다대일
    @ManyToOne(optional = false)
    @JoinColumn(name = "publisher_id", nullable = false)
    private Publisher publisher;


    @Column(nullable = false)
    private String title;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String content;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @NotBlank
    @Length(max = 20)
    @Column(nullable = false)
    private String isbn13;

    @NotNull
    @Column(nullable = false)
    @Min(0)
    private int price;

    @NotNull
    @Column(nullable = false)
    @Min(0)
    private int salePrice;

    private long amount;

    private long views;

    @Column(nullable = false)
    private LocalDate pubdate;

    private boolean status = false;
}
