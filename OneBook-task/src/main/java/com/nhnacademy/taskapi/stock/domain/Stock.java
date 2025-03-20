package com.nhnacademy.taskapi.stock.domain;


import com.nhnacademy.taskapi.book.domain.Book;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "stocks")
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long stockId;


    @ManyToOne(optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;


    @NotNull
    @Column(nullable = false)
    @Min(0)
    private int stock;
}
