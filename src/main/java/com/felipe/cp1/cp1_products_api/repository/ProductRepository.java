package com.felipe.cp1.cp1_products_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.felipe.cp1.cp1_products_api.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}