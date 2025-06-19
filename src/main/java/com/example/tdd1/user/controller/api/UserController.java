package com.example.tdd1.user.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/users")
    public ResponseEntity<?> readUsers() {

        // TODO: 단위 테스트 코드 작성

        throw new UnsupportedOperationException("테스트 코드 작성 중");
    }
}
