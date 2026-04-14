package com.marketplace.RateLimiter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RateLimiterService {

    private final StringRedisTemplate stringRedisTemplate;

    public static final Duration EXPIRE_TIME = Duration.ofSeconds(10);

//    maximum request is 5
    public static final long MAX_REQUESTS = 5L;

    public static final long FIRST_REQUEST = 1L;

    public boolean isAllowed(String userEmail) {
        String key = "rate_limit:" + userEmail;

//        increment = saveData(key, INITIAL_REQUEST++);
        Long increment = stringRedisTemplate.opsForValue().increment(key);
        Assert.notNull(increment, "Increment value must not be null");

        if (increment == FIRST_REQUEST) {
            stringRedisTemplate.expire(key, EXPIRE_TIME);
        }

        return increment <= MAX_REQUESTS;

    }

}
