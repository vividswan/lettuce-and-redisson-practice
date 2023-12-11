package com.vividswan.redispractice.facade;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.vividswan.redispractice.domain.Product;
import com.vividswan.redispractice.repository.ProductRepository;

@SpringBootTest
class RedissonLockProductFacadeTest {
	@Autowired
	private RedissonLockProductFacade redissonLockProductFacade;

	@Autowired
	private ProductRepository productRepository;

	@BeforeEach
	public void before() {
		productRepository.save(new Product(1L, 100L));
	}

	@AfterEach
	public void after() {
		productRepository.deleteAll();
	}

	@Test
	@DisplayName("동시에 100개의 요청 in redisson")
	public void 동시에_100개의_요청() throws InterruptedException {
		int threadCount = 100;
		ExecutorService executorService = Executors.newFixedThreadPool(20);
		CountDownLatch countDownLatch = new CountDownLatch(threadCount);

		for (int i = 0; i < threadCount; i++) {
			executorService.submit(() -> {
				try {
					redissonLockProductFacade.decrease(1L, 1L);
				} finally {
					countDownLatch.countDown();
				}
			});
		}

		countDownLatch.await();

		Product product = productRepository.findByProductId(1L)
			.orElseThrow(() -> new RuntimeException("not exist product"));

		assertEquals(0, product.getQuantity());

	}
}
