package com.example.tdd1.user.controller.api;

import com.example.tdd1.user.dto.request.UserCreateRequestDto;
import com.example.tdd1.user.dto.request.UserLoginRequestDto;
import com.example.tdd1.user.dto.response.UserCreateResponseDto;
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

    @PostMapping("/auth/logIn")
    public ResponseEntity<?> logIn(@RequestBody UserLoginRequestDto userLoginRequestDto) {

        throw new UnsupportedOperationException("테스트 코드 작성 중");
    }


}
