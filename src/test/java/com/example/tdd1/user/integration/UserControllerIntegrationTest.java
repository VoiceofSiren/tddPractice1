package com.example.tdd1.user.integration;

import com.example.tdd1.user.dto.request.UserCreateRequestDto;
import com.example.tdd1.user.entity.User;
import com.example.tdd1.user.repository.UserRepository;
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

// 통합 테스트
@SpringBootTest
// MockMvc 기능 제공
@AutoConfigureMockMvc
// application-test.yaml 사용
@ActiveProfiles("test")
// rollback 기능 제공
@Transactional
public class UserControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("POST /auth/signUp API 통합 테스트")
    void test1() throws Exception {

        // given
        UserCreateRequestDto userCreateRequestDto = new UserCreateRequestDto();
        userCreateRequestDto.setUsername("user1");
        userCreateRequestDto.setPassword("password1");
        userCreateRequestDto.setRole("ROLE1");

        // when & then
        mockMvc.perform(
                // GET 요청
                post("/auth/signUp")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userCreateRequestDto))
                )
                // 응답
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.username").value("user1"))
                .andExpect(jsonPath("$.password").value("password1"))
                .andExpect(jsonPath("$.role").value("ROLE1"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("GET /users API 통합 테스트")
    void test2() throws Exception {

        // given

        User user1 = new User();
        user1.setUsername("user1");
        user1.setPassword("password1");
        user1.setRole("ROLE1");

        User user2 = new User();
        user2.setUsername("user2");
        user2.setPassword("password2");
        user2.setRole("ROLE2");

        userRepository.save(user1);
        userRepository.save(user2);


        // when & then
        mockMvc.perform(
                        get("/users")
                                .with(csrf())
                )
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.length()").value(2))
                        .andExpect(jsonPath("$[0].username").value("user1"))
                        .andExpect(jsonPath("$[0].password").value("password1"))
                        .andExpect(jsonPath("$[0].role").value("ROLE1"))
                        .andExpect(jsonPath("$[1].username").value("user2"))
                        .andExpect(jsonPath("$[1].password").value("password2"))
                        .andExpect(jsonPath("$[1].role").value("ROLE2"));


    }

}
