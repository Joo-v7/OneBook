package com.nhnacademy.taskapi.category.domain;

import com.nhnacademy.taskapi.book.domain.BookCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int categoryId;

    @ManyToOne(optional = true)
    @JoinColumn(name = "category_parent_id")
    private Category parentCategory;


    @NotBlank
    @Length(max = 20)
    @Column(nullable = false)
    private String name;

    private boolean status = false;
}
