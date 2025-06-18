package com.example.tdd1.user.controller;

import com.example.tdd1.user.dto.UserCreateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    @PostMapping("/auth/signUp")
    public ResponseEntity<?> signUp(UserCreateRequestDto userCreateRequestDto) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(httpHeaders, HttpStatus.OK);
    }
}
