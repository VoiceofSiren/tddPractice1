package com.example.tdd1.user.service;

import com.example.tdd1.user.dto.UserCreateRequestDto;
import com.example.tdd1.user.dto.UserCreateResponseDto;
import com.example.tdd1.user.entity.User;
import com.example.tdd1.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

// 가짜 객체를 사용할 수 있도록 하는 모듈
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    // 가짜 의존성 주입
    @InjectMocks
    UserService userService;

    // UserService에서 주입 받은 UserRepository의 가짜 객체
    @Mock
    UserRepository userRepository;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("UserService.createUser() 테스트 코드")
    void test1() {

        // given
        UserCreateRequestDto userCreateRequestDto = new UserCreateRequestDto();
        userCreateRequestDto.setUsername("user1");
        userCreateRequestDto.setPassword("password1");
        userCreateRequestDto.setRole("role1");

            // UserRepository의 mock 객체가 원본처럼 동작하지 않을 수 있으므로
            // 아래와 같이 꾸며줘야 함.
        User savedUser = new User();
        ReflectionTestUtils.setField(savedUser, "id", 1L);
        savedUser.setUsername(userCreateRequestDto.getUsername());
        savedUser.setPassword(userCreateRequestDto.getPassword());
        savedUser.setRole(userCreateRequestDto.getRole());
        given(userRepository.save(any(User.class))).willReturn(savedUser);

        // when
        // 타게팅 대상 - createUser(UserCreateRequestDto): UserCreateResponseDto
        UserCreateResponseDto userCreateResponseDto = userService.createUser(userCreateRequestDto);

        // then
        assertTrue(userCreateResponseDto.getId().equals(1L));
        assertTrue(userCreateResponseDto.getUsername().equals("user1"));
        assertTrue(userCreateResponseDto.getPassword().equals("password1"));
        assertTrue(userCreateResponseDto.getRole().equals("role1"));
    }

}