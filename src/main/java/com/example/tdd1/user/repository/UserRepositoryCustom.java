package com.example.tdd1.user.repository;

import com.example.tdd1.user.dto.response.UserReadResponseDto;
import com.example.tdd1.user.entity.User;

import java.util.List;

public interface UserRepositoryCustom {
    List<UserReadResponseDto> findUsers();

    User getByUsername(String username);
}
