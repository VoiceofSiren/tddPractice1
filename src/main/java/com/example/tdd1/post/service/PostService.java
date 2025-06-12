package com.example.tdd1.post.service;

import com.example.tdd1.post.dto.PostRequestDto;
import com.example.tdd1.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Long create(PostRequestDto postRequestDto) {
        throw new UnsupportedOperationException("테스트 코드 작성 중");
    }

}
