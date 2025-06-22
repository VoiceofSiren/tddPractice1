package com.example.tdd1.user.service;

import com.example.tdd1.user.dto.request.UserCreateRequestDto;
import com.example.tdd1.user.dto.response.UserCreateResponseDto;
import com.example.tdd1.user.dto.response.UserReadResponseDto;
import com.example.tdd1.user.entity.User;
import com.example.tdd1.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public UserCreateResponseDto createUser(UserCreateRequestDto userCreateRequestDto) {

        User user = new User();
        user.setUsername(userCreateRequestDto.getUsername());
        user.setPassword(passwordEncoder.encode(userCreateRequestDto.getPassword()));
        user.setRole(userCreateRequestDto.getRole());

        User savedUser = userRepository.save(user);

        return UserCreateResponseDto.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .password(savedUser.getPassword())
                .role(savedUser.getRole())
                .build();
    }

    @Transactional
    public List<UserReadResponseDto> readUsers() {

        return userRepository.findUsers();
    }
}
