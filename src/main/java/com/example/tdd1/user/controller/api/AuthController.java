package com.example.tdd1.user.controller.api;

import com.example.tdd1.user.dto.UserCreateRequestDto;
import com.example.tdd1.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/auth/signUp")
    public ResponseEntity<?> signUp(UserCreateRequestDto userCreateRequestDto) {

        // TODO: PostControllerIntegrationTest 작성
        Long savedId = userService.createUser(userCreateRequestDto).getId();

        Map<String, Object> responseBody = Map.of("id", savedId);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(responseBody, httpHeaders, HttpStatus.OK);
    }
}
