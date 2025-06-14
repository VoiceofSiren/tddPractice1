package com.example.tdd1.post.service;

import com.example.tdd1.post.dto.PostRequestDto;
import com.example.tdd1.post.entity.Post;
import com.example.tdd1.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Long create(PostRequestDto postRequestDto) {

        Post post = new Post();
        post.setTitle(postRequestDto.getTitle());
        post.setContent(post.getContent());


        return postRepository.save(post).getId();
    }

}
