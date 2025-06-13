package com.example.tdd1.post.service;

import com.example.tdd1.post.dto.PostRequestDto;
import com.example.tdd1.post.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

// 가짜 객체를 사용할 수 있도록 하는 모듈
@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    // 의존성 주입
    @InjectMocks
    PostService postService;

    // postService에서 주입 받은 postRepository의 가짜 객체
    @Mock
    PostRepository postRepository;

    @Test
    @DisplayName("Post Entity 생성 테스트")
    void test1() {

        // given
        PostRequestDto postRequestDto = new PostRequestDto();
        postRequestDto.setTitle("title1");
        postRequestDto.setContent("content1");

        // when
        // 타게팅 대상 작성
        // create(PostRequestDto): Long
        Long postId = postService.create(postRequestDto);

        // then
        assertTrue(postId instanceof Long);
    }



}