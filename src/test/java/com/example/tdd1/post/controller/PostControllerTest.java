package com.example.tdd1.post.controller;

import com.example.tdd1.post.dto.PostRequestDto;
import com.example.tdd1.post.service.PostService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Controller 테스트를 위한 애너테이션
@WebMvcTest(controllers = PostController.class)
class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    // 가짜 PostService bean 객체를 주입 받음
    @MockitoBean
    PostService postService;

    // api method
    @Test
    @DisplayName("POST /post 200 OK 응답 확인을 위한 테스트 코드")
    void test1() throws Exception {

        // given
        PostRequestDto postRequestDto = new PostRequestDto();
        postRequestDto.setTitle("title");
        postRequestDto.setContent("content");

        // when & then
        mockMvc.perform(
                // POST 요청
                post("/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(postRequestDto))
        )
                // 응답
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("PostService.create() 로직 확인을 위한 테스트 코드")
    void test2() throws Exception {

        // given
        PostRequestDto postRequestDto = new PostRequestDto();
        postRequestDto.setTitle("title");
        postRequestDto.setContent("content");

            // 아래의 andExpect(jsonPath("$.id").value(1)); 코드와 관련이 있음.
        given(postService.create(any(PostRequestDto.class))).willReturn(1L);

        // when & then
        mockMvc.perform(
                // POST 요청
                post("/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(postRequestDto))
        )
                // 응답
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    // JSON Body에 있는 id 필드의 값이 1인 걍우
                .andExpect(jsonPath("$.id").value(1));

         // postService.create() 메서드를 사용하였는지 검증
        verify(postService).create(any(PostRequestDto.class));
    }

    @Test
    @DisplayName("PostService에서 title에 빈 값 입력 시 PostController로 전파되는 예외를 처리하는 테스트")
    void test3() throws Exception {

        // given
        PostRequestDto postRequestDto = new PostRequestDto();
        postRequestDto.setTitle("");
        postRequestDto.setContent("content");

        given(postService.create(any(PostRequestDto.class))).willThrow(IllegalArgumentException.class);

        // when & then
        mockMvc.perform(
                // post 요청
                post("/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(postRequestDto))
        )
                // 응답
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}