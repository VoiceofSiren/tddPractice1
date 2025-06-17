package com.example.tdd1.post.repository;

import com.example.tdd1.post.dto.PostResponseDto;

public interface PostRepositoryCustom {
    PostResponseDto readById(Long id);
}
