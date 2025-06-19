package com.example.tdd1.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserReadResponseDto {
    private Long id;
    private String username;
    private String password;
    private String role;
}
