package com.vividswan.redispractice.facade;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import com.vividswan.redispractice.service.ProductService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class RedissonLockProductFacade {
	private final RedissonClient redissonClient;

	private final ProductService productService;

	private String generateKey(Long key) {
		return key.toString();
	}

	public void decrease(Long id, Long quantity) {
		// redisson은 별도의 lock 관리 클래스를 제공해주므로 repository는 작성할 필요가 없지만 락 획득 / 해제는 해주어야 함
		// redisson은 lettuce와 다르게 pub, sub 기반이기 때문에 부하를 줄여줄 수 있지만 구현이 조금 복잡하고 별도의 라이브러리를 사용해야함

		RLock lock = redissonClient.getLock(generateKey(id));

		try {
			boolean isAvailable = lock.tryLock(20, 1, TimeUnit.SECONDS);

			if (!isAvailable) {
				System.out.println("lock 획득 실패");
				return;
			}

			productService.decreaseStock(id, quantity);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
			lock.unlock();
		}

	}

}
