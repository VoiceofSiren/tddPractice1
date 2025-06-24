package com.example.tdd1.global.jwt.repository;

import com.example.tdd1.global.jwt.entity.QRefreshToken;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RefreshTokenRepositoryCustomImpl implements RefreshTokenRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public void deleteOldRefreshToken(String refresh) {
        QRefreshToken qRefreshToken = QRefreshToken.refreshToken;
        jpaQueryFactory
                .delete(qRefreshToken)
                .where(qRefreshToken.refresh.eq(refresh));
    }
}
