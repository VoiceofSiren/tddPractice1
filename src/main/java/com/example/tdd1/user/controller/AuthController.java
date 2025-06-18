package com.example.tdd1.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    @PostMapping("/auth/signUp")
    public ResponseEntity<?> signUp() {
        throw new  UnsupportedOperationException("테스트 코드 작성 중");
    }
}
