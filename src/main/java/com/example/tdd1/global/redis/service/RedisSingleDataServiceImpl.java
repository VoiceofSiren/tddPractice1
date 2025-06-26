package com.example.tdd1.global.redis.service;

import com.example.tdd1.global.redis.config.RedisConfig;
import com.example.tdd1.global.redis.handler.RedisHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisSingleDataServiceImpl implements RedisSingleDataService{

    private final RedisHandler redisHandler;
    private final RedisConfig redisConfig;

    /**
     *  Redis 단일 데이터 값을 등록 및 수정
     *
     * @param key: redis key
     * @param value: redis value
     * @return {int} 성공(1), 실패(0)
     */
    @Override
    public int setSingleData(String key, Object value) {
        return redisHandler.executeOperation(() -> redisHandler.getValueOperations().set(key, value));
    }

    /**
     *  Redis 단일 데이터 값을 등록 및 수정
     *  duration 값이 존재하면 메모리 상 유효시간을 지정
     *
     * @param key: redis key
     * @param value: redis value
     * @return {int} 성공(1), 실패(0)
     */
    @Override
    public int setSingleData(String key, Object value, Duration duration) {
        return redisHandler.executeOperation(() -> redisHandler.getValueOperations().set(key, value, duration));
    }

    /**
     * Redis 키를 기반으로 단일 데이터의 값을 조회
     *
     * @param key: redis key
     * @return {String} redis value 값 반환 or 미 존재시 null 반환
     */
    @Override
    public String getSingleData(String key) {
        if (redisHandler.getValueOperations().get(key) == null) {
            return "";
        }
        return String.valueOf(redisHandler.getValueOperations().get(key));
    }

    /**
     *  Redis 키를 기반으로 단일 데이터의 값을 삭제
     *
     * @param key: redis key
     * @return {int} 성공(1), 실패(0)
     */
    @Override
    public int deleteSingleData(String key) {
        return redisHandler.executeOperation(() -> redisConfig.redisTemplate().delete(key));
    }

}
