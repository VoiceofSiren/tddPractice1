package com.example.tdd1.global.idempotency.service;

import com.example.tdd1.global.idempotency.IdempotencyConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class IdempotencyKeyService {

    private final RedisTemplate<String, String> redisTemplate;
    private final Duration timeToLive = Duration.ofMinutes(IdempotencyConstants.IDEMPOTENCY_KEY_DURATION_OF_MINUTES);

    public boolean isProcessed(String idempotencyKey) {
        return redisTemplate.hasKey(idempotencyKey);
    }

    public void markProcessed(String idempotencyKey) {
        redisTemplate.opsForValue().set(idempotencyKey, "USED", timeToLive);
    }
}
