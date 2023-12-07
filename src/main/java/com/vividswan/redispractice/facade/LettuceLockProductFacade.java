package com.vividswan.redispractice.facade;

import org.springframework.stereotype.Component;

import com.vividswan.redispractice.repository.RedisLockRepository;
import com.vividswan.redispractice.service.ProductService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class LettuceLockProductFacade {
	private final RedisLockRepository redisLockRepository;

	private final ProductService productService;

	public void decrease(Long key, Long quantity) throws InterruptedException {
		// 로컬 레디스를 켜놓어야 동작
		while (!redisLockRepository.lock(key)) {
			Thread.sleep(100);
		}

		try {
			productService.decreaseStock(key, quantity);
		} finally {
			redisLockRepository.unlock(key);
		}
	}
}
