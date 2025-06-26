package com.example.tdd1.global.redis.handler;

import com.example.tdd1.global.redis.config.RedisConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisHandler {

    private final RedisConfig redisConfig;

    /**
     *  단일 데이터에 접근하여 다양한 연산을 수행
     * @return ValueOperations<String, Object>
     */
    public ValueOperations<String, Object> getValueOperations() {
        return redisConfig.redisTemplate().opsForValue();
    }

    /**
     *  리스트에 접근하여 다양한 연산을 수행
     * @return ListOperations<String, Object>
     */
    public ListOperations<String, Object> getListOperations() {
        return redisConfig.redisTemplate().opsForList();
    }

    /**
     *  Redis 작업 중 삽입, 갱신, 삭제에 대해 처리 및 예외를 수행
     * @param operation
     * @return
     */
    public int executeOperation(Runnable operation) {
        try {
            operation.run();
            return 1;
        } catch (Exception e) {
            System.out.println("Redis 작업 중 오류 발생 :: " + e.getMessage());
            return 0;
        }
    }
}
