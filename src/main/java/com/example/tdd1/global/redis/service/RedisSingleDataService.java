package com.example.tdd1.global.redis.service;

import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public interface RedisSingleDataService {

    // Redis 단일 데이터 값을 등록 또는 수정
    int setSingleData(String key, Object value);

    // Redis 단일 데이터 값을 등록 또는 수정
    // duration 값이 존재하면 메모리 상 유효시간을 지정
    int setSingleData(String key, Object value, Duration duration);

    // Redis 키를 기반으로 단일 데이터의 값을 조회
    String getSingleData(String key);

    // Redis 키를 기반으로 단일 데이터의 값을 삭제
    int deleteSingleData(String key);
}