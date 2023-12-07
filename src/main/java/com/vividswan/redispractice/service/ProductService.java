package com.vividswan.redispractice.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vividswan.redispractice.domain.Product;
import com.vividswan.redispractice.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductService {
	private final ProductRepository productRepository;

	@Transactional
	public void decreaseStock(Long productId, Long quantity) {
		Optional<Product> productById = productRepository.findByProductId(productId);

		if (productById.isEmpty()) {
			throw new RuntimeException("It's a non-existent product.");
		}

		productById.get().decrease(quantity);
	}
}
