package com.nhnacademy.taskapi.image.domain;

import com.nhnacademy.taskapi.book.domain.Book;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long imageId;

    @Length(max = 255)
    private String url;

    // 책과 이미지는 다대일 관계 (하나의 책에 여러 이미지가 가능)
    @ManyToOne(optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    // 책 이미지 이름 추가
    @Length(max = 255)
    private String name; // 책 이미지 이름 필드 추가
}