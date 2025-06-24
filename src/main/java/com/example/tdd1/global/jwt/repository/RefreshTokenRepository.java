package com.example.tdd1.global.jwt.repository;

import com.example.tdd1.global.jwt.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>, RefreshTokenRepositoryCustom {

    Boolean existsByRefresh(String refresh);

    @Transactional
    void deleteRefreshTokenByRefresh(String refresh);
}
