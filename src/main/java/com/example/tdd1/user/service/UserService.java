package com.example.tdd1.user.service;

import com.example.tdd1.user.dto.UserCreateRequestDto;
import com.example.tdd1.user.dto.UserCreateResponseDto;
import com.example.tdd1.user.entity.User;
import com.example.tdd1.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserCreateResponseDto createUser(UserCreateRequestDto userCreateRequestDto) {

        User user = new User();
        user.setUsername(userCreateRequestDto.getUsername());
        user.setPassword(userCreateRequestDto.getPassword());
        user.setRole(userCreateRequestDto.getRole());

        User savedUser = userRepository.save(user);

        return UserCreateResponseDto.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .password(savedUser.getPassword())
                .role(savedUser.getRole())
                .build();
    }
}
