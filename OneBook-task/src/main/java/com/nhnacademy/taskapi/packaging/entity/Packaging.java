package com.nhnacademy.taskapi.packaging.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "packagings")
public class Packaging {
    @Id
    @Column(name = "packaging_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    String name;
    int price;

    public Packaging(String name, int price) {
        this.name = name;
        this.price = price;
    }
}