package com.example.tdd1.user.controller.api;

import com.example.tdd1.global.idempotency.IdempotencyConstants;
import com.example.tdd1.global.idempotency.service.IdempotencyKeyService;
import com.example.tdd1.user.dto.request.UserCreateRequestDto;
import com.example.tdd1.user.dto.request.UserLoginRequestDto;
import com.example.tdd1.user.dto.response.UserCreateResponseDto;
import com.example.tdd1.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final IdempotencyKeyService idempotencyKeyService;

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(
            @RequestHeader (value = IdempotencyConstants.IDEMPOTENCY_KEY_HEADER_NAME, required = false) String idempotencyKey,
            @RequestBody UserCreateRequestDto userCreateRequestDto) {

        if (idempotencyKey != null && idempotencyKeyService.isProcessed(idempotencyKey)) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Duplicate request: Already processed.");
        }

        UserCreateResponseDto userCreateResponseDto = userService.createUser(userCreateRequestDto);

        if (idempotencyKey != null) {
            idempotencyKeyService.markProcessed(idempotencyKey);
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(userCreateResponseDto, httpHeaders, HttpStatus.OK);
    }

}
