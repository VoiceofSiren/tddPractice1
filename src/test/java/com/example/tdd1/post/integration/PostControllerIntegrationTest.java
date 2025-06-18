package com.example.tdd1.post.integration;

import com.example.tdd1.post.dto.PostRequestDto;
import com.example.tdd1.post.entity.Post;
import com.example.tdd1.post.repository.PostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// 통합 테스트를 위한 애너테이션, Mock 객체가 아닌 실제 객체를 사용
@SpringBootTest
// @SpringBootTest는 MockMvc 기능을 제공하지 않으므로 해당 기능 사용을 위해 추가하는 애너테이션
@AutoConfigureMockMvc
// application-test.yaml 사용을 위한 애너테이션
@ActiveProfiles("test")
// rollback 가능
@Transactional
public class PostControllerIntegrationTest {

    @Autowired
    // 가짜 클라이언트 객체
    MockMvc mockMvc;

    @Autowired
    PostRepository postRepository;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("POST /post API 통합 테스트")
    void test1() throws Exception {

        // given
        PostRequestDto postRequestDto = new PostRequestDto();
        postRequestDto.setTitle("title");
        postRequestDto.setContent("content");

        // when & then
            // API 요청 수행
        mockMvc.perform(
                // POST 요청
                post("/post")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(postRequestDto))
                )
                // 응답
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists());

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("GET /post/{id} API 통합 테스트")
    void test2() throws Exception {

        // given
        Post post = new Post();
        post.setTitle("title");
        post.setContent("content");

            // DB에 데이터가 저장되어 있어야 테스트를 통과하므로 아래의 코드를 추가
        Long savedId = postRepository.save(post).getId();

        // when & then
        mockMvc.perform(
                // GET 요청
                get("/post/" + savedId)
                        .with(csrf())
                )
                // 응답
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(savedId))
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.content").value("content"));
    }

}
