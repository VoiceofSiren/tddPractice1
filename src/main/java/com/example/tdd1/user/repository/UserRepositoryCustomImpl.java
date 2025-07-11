package com.example.tdd1.user.repository;

import com.example.tdd1.user.dto.response.UserReadResponseDto;
import com.example.tdd1.user.entity.QUser;
import com.example.tdd1.user.entity.User;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<UserReadResponseDto> findUsers() {

        QUser qUser = QUser.user;

        JPAQuery<Tuple> query = jpaQueryFactory
                .select(
                        qUser.id,
                        qUser.username,
                        qUser.password,
                        qUser.role)
                .from(qUser)
                .orderBy(qUser.id.asc());

        return query.stream()
                .map(tuple -> UserReadResponseDto.builder()
                        .id(tuple.get(qUser.id))
                        .username(tuple.get(qUser.username))
                        .password(tuple.get(qUser.password))
                        .role(tuple.get(qUser.role))
                        .build()
                )
                .toList();
    }

    @Override
    public User getByUsername(String username) {

        QUser qUser = QUser.user;
        Tuple tuple = jpaQueryFactory
                .select(
                        qUser.id,
                        qUser.username,
                        qUser.password,
                        qUser.role)
                .from(qUser)
                .where(qUser.username.eq(username))
                .fetchFirst();

        User user = new User();
        assert tuple != null;
        user.setId(tuple.get(qUser.id));
        user.setUsername(tuple.get(qUser.username));
        user.setPassword(tuple.get(qUser.password));
        user.setRole(tuple.get(qUser.role));
        return user;
    }
}
