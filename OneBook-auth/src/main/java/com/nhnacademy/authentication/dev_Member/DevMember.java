package com.nhnacademy.authentication.dev_Member;


import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class DevMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int member_id;
    String id;
    String password;
}
