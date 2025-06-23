package com.example.tdd1.user.service;

import com.example.tdd1.user.dto.request.UserCreateRequestDto;
import com.example.tdd1.user.dto.response.UserCreateResponseDto;
import com.example.tdd1.user.dto.response.UserReadResponseDto;
import com.example.tdd1.user.entity.User;
import com.example.tdd1.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;


import java.util.List;

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

    @Spy
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("UserService.createUser() 단위 테스트 코드")
    void test1() {

        // given
        UserCreateRequestDto userCreateRequestDto = new UserCreateRequestDto();
        userCreateRequestDto.setUsername("user1");
        String plainPassword = "password1";
        userCreateRequestDto.setPassword(plainPassword);
        userCreateRequestDto.setRole("role1");

            // UserRepository의 mock 객체가 원본처럼 동작하지 않을 수 있으므로
            // 아래와 같이 꾸며줘야 함.
        User savedUser = new User();
        ReflectionTestUtils.setField(savedUser, "id", 1L);
        savedUser.setUsername(userCreateRequestDto.getUsername());
        String encodedPassword = "$2a$10$jjXKMN6hzbOJWYF2.0llM.8sHYOM.D8UtXpZPNNwadAMaLvlBSds6";
        savedUser.setPassword(encodedPassword);
        savedUser.setRole(userCreateRequestDto.getRole());
        given(passwordEncoder.encode(plainPassword)).willReturn(encodedPassword);
        given(userRepository.save(any(User.class))).willReturn(savedUser);

        // when
        // 타게팅 대상 - createUser(UserCreateRequestDto): UserCreateResponseDto
        UserCreateResponseDto userCreateResponseDto = userService.createUser(userCreateRequestDto);

        // then
        verify(userRepository).save(any(User.class));
        assertEquals(1L, userCreateResponseDto.getId());
        assertEquals("user1", userCreateResponseDto.getUsername());
        System.out.println("encoded pw: " + encodedPassword);
        System.out.println("user pw: " + userCreateRequestDto.getPassword());
        assertTrue(passwordEncoder.matches(plainPassword, userCreateResponseDto.getPassword()));
        assertEquals("role1", userCreateResponseDto.getRole());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("UserService.readUsers() 단위 테스트 코드")
    void test2() {

        // given
        UserReadResponseDto responseDto1 = UserReadResponseDto.builder()
                .id(1L)
                .username("user1")
                .password("password1")
                .role("ROLE1")
                .build();

        UserReadResponseDto responseDto2 = UserReadResponseDto.builder()
                .id(2L)
                .username("user2")
                .password("password2")
                .role("ROLE2")
                .build();


        List<UserReadResponseDto> userReadResponseDtoList = List.of(responseDto1, responseDto2);

        given(userRepository.findUsers()).willReturn(userReadResponseDtoList);

        // when
        List<UserReadResponseDto> result = userService.readUsers();

        // then
        verify(userRepository).findUsers();
        assertTrue(result.size() == 2);
        assertTrue(result.get(0).getId().equals(1L));
        assertTrue(result.get(1).getId().equals(2L));
    }

}