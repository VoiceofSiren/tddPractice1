package com.example.tdd1.global.jwt.entity;

import com.example.tdd1.global.jwt.util.JwtConstants;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Entity
@Getter
@Setter
@RedisHash(value = "refreshToken", timeToLive = JwtConstants.REFRESH_TOKEN_EXPIRATION)
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
