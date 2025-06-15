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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    @DisplayName("POST /post 테스트 코드 작성")
    void test1() throws Exception {

        // given
        PostRequestDto postRequestDto = new PostRequestDto();
        postRequestDto.setTitle("title");
        postRequestDto.setContent("content");

        // when & then
        mockMvc.perform(
                // POST 요청
                post("post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(postRequestDto))
                )
                // 응답
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }
}