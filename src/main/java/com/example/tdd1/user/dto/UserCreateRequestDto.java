package com.example.tdd1.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateRequestDto {
    private String username;
    private String password;
    private String role;
}
