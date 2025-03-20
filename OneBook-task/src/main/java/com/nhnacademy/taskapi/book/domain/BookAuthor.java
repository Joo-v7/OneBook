package com.nhnacademy.taskapi.book.domain;

import com.nhnacademy.taskapi.author.domain.Author;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "books_authors")
public class BookAuthor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookAuthorId;


    @ManyToOne(optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;


    @ManyToOne(optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;
}
