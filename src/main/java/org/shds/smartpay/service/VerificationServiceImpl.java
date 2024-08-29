package org.shds.smartpay.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class VerificationServiceImpl {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    public String checkRedisConnection() {
        try (RedisConnection connection = redisConnectionFactory.getConnection()) {
            return connection.ping();
        } catch (Exception e) {
            return "Unable to connect to Redis: " + e.getMessage();
        }
    }

    private static final long EXPIRATION_TIME = 10; // 인증번호 유효기간 (10분)

    public void saveVerificationCode(String phoneNumber, String code) {
        redisTemplate.opsForValue().set(phoneNumber, code, EXPIRATION_TIME, TimeUnit.MINUTES);
    }

    public boolean verifyCode(String phoneNumber, String code) {
        String storedCode = redisTemplate.opsForValue().get(phoneNumber);
        return code.equals(storedCode);
    }
}
