package com.example.tdd1.global.jwt.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
public class RefreshToken implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @org.springframework.data.annotation.Id
    @Column
    private String username;

    @Column
    private String refresh;

    @Column
    private String expiration;
}
