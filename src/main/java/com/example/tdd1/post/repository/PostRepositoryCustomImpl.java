package com.example.tdd1.post.repository;

import com.example.tdd1.post.dto.PostResponseDto;
import com.example.tdd1.post.entity.QPost;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public PostResponseDto readById(Long id) {

        QPost qPost = QPost.post;
        JPAQuery<Tuple> query = jpaQueryFactory
                .select(
                        qPost.id,
                        qPost.title,
                        qPost.content)
                .from(qPost)
                .where(qPost.id.eq(id))
                .orderBy(qPost.id.asc());

        Tuple tuple = query.fetchFirst();

        return PostResponseDto.builder()
                .id(Objects.requireNonNull(tuple).get(qPost.id))
                .title(tuple.get(qPost.title))
                .content(tuple.get(qPost.content))
                .build();
    }
}
