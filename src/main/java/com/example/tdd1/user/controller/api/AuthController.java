package com.example.tdd1.user.controller.api;

import com.example.tdd1.user.dto.UserCreateRequestDto;
import com.example.tdd1.user.dto.UserCreateResponseDto;
import com.example.tdd1.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/auth/signUp")
    public ResponseEntity<?> signUp(@RequestBody UserCreateRequestDto userCreateRequestDto) {

        UserCreateResponseDto userCreateResponseDto = userService.createUser(userCreateRequestDto);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(userCreateResponseDto, httpHeaders, HttpStatus.OK);
    }


}
