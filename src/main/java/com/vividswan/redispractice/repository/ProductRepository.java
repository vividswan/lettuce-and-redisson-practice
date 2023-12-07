package com.vividswan.redispractice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vividswan.redispractice.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	Optional<Product> findByProductId(Long productId);
}
