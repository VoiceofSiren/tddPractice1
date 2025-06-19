package com.example.tdd1.user.repository;

import com.example.tdd1.user.dto.response.UserReadResponseDto;

import java.util.List;

public interface UserRepositoryCustom {
    List<UserReadResponseDto> findUsers();
}
