package com.example.tdd1.post.controller.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PageController.class)
class PageControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("GET /page 테스트")
    void test1() throws Exception {

        // given

        // when & then
        mockMvc.perform(
                // get 요청
                get("/page")
        )
                // 응답
                .andExpect(status().isOk())
                .andExpect(view().name("post/page"))
                .andExpect(model().attributeExists("POSTLIST"));
    }

    @Test
    @DisplayName("POST /page 테스트")
    void test2() throws Exception {

        // given
        String title = "title";
        String content = "content";

        // when & then
        mockMvc.perform(
                // get 요청
                post("/page")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", title)
                        .param("content", content)
        )
                // 응답
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/page"));
    }

}