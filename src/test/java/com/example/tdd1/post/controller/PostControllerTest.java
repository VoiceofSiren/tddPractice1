package com.example.tdd1.post.controller;

import com.example.tdd1.post.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

// Controller 테스트를 위한 애너테이션
@WebMvcTest(controllers = PostController.class)
class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    // 가짜 PostService bean 객체를 주입 받음
    @MockitoBean
    PostService postService;

    // api method

}