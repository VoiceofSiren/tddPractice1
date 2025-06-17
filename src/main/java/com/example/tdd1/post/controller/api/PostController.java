package com.example.tdd1.post.controller.api;

import com.example.tdd1.post.dto.PostRequestDto;
import com.example.tdd1.post.dto.PostResponseDto;
import com.example.tdd1.post.entity.Post;
import com.example.tdd1.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/post")
    public ResponseEntity<?> createPost(@RequestBody PostRequestDto postRequestDto) {

        // tdd 구현
        Long resultId = postService.create(postRequestDto);

        // HTTP Body
        Map<String, Object> responseBody = Map.of("id", resultId);

        // HTTP Header를 이용하여 test1()이 성공하도록 구현
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json"));

        return new ResponseEntity<>(responseBody, httpHeaders, HttpStatus.OK);
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<?> readPost(@PathVariable (name = "id") Long id) {

        PostResponseDto postResponseDto = postService.read(id);

        // HTTP Header를 이용하여 test2()가 성공하도록 구현
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json"));

        return new ResponseEntity<>(postResponseDto, httpHeaders, HttpStatus.OK);
    }
}
