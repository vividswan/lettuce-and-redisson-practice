package com.vividswan.redispractice.repository;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class RedisLockRepository {

	private final RedisTemplate<String, String> redisTemplate;

	private String generateKey(Long key) {
		return key.toString();
	}

	public Boolean lock(Long key) {
		return redisTemplate
			.opsForValue()
			.setIfAbsent(generateKey(key), "lock", Duration.ofMillis(3_000));
	}

	public Boolean unlock(Long key) {
		return redisTemplate.delete(generateKey(key));
	}
}
