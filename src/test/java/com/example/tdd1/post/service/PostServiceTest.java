package com.example.tdd1.post.service;

import com.example.tdd1.post.dto.PostRequestDto;
import com.example.tdd1.post.entity.Post;
import com.example.tdd1.post.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


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

        // PostService.create 내부 코드 작성 후 추가 시 이 테스트 메서드는 실패함.
        //
        Post savedPost = new Post();
        ReflectionTestUtils.setField(savedPost, "id", 1L);
        savedPost.setTitle("title");
        savedPost.setContent("content");
        given(postRepository.save(any(Post.class))).willReturn(savedPost);

        // when
        // 타게팅 대상 작성
        // create(PostRequestDto): Long
        Long postId = postService.create(postRequestDto);

        // then
        assertTrue(postId instanceof Long);
    }

    @Test
    @DisplayName("Post Entity 생성 테스트")
    void test2() {

        // given
        PostRequestDto postRequestDto = new PostRequestDto();
        postRequestDto.setTitle("title2");
        postRequestDto.setContent("content2");

        // PostRepository mockup 객체가 원본처럼 동작하지 않을 수 있으므로
        // 아래와 같이 동작을 꾸며줘야 함.
        Post savedPost = new Post();
        ReflectionTestUtils.setField(savedPost, "id", 1L);
        savedPost.setTitle("title");
        savedPost.setContent("content");
        given(postRepository.save(any(Post.class))).willReturn(savedPost);

        // when
        postService.create(postRequestDto);

        // then
            // postRepository.save() 메서드를 사용하였는지 검증
        verify(postRepository).save(any(Post.class));
    }

    @Test
    @DisplayName("title 필드에 빈 값이 들어가지 않도록 예외 처리")
    void test3() {

        // given
        PostRequestDto postRequestDto = new PostRequestDto();
        postRequestDto.setTitle("");
        postRequestDto.setContent("content");

        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            postService.create(postRequestDto);
        });
    }

}