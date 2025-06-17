package com.example.tdd1.post.service;

import com.example.tdd1.post.dto.PostRequestDto;
import com.example.tdd1.post.dto.PostResponseDto;
import com.example.tdd1.post.entity.Post;
import com.example.tdd1.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public Long create(PostRequestDto postRequestDto) {

        if (postRequestDto.getTitle().isBlank()) {
            throw new IllegalArgumentException("빈 값 금지");
        }

        Post post = new Post();
        post.setTitle(postRequestDto.getTitle());
        post.setContent(post.getContent());

        return postRepository.save(post).getId();
    }

    @Transactional
    public PostResponseDto read(Long id) {

        return postRepository.readById(id);
    }
}
