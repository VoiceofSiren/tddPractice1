package com.example.tdd1.post.controller;

import com.example.tdd1.post.dto.PostRequestDto;
import com.example.tdd1.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/post")
    public ResponseEntity<?> createPost(@RequestBody PostRequestDto postRequestDto) {

        // tdd 구현

        throw new UnsupportedOperationException("테스트 메서드 구현 중");
    }
}
