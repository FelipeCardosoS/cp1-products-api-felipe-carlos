package com.felipe.cp1.cp1_products_api.service;

import org.springframework.stereotype.Service;

import java.util.List;

import com.felipe.cp1.cp1_products_api.dto.ProductDTO;
import com.felipe.cp1.cp1_products_api.model.Product;
import com.felipe.cp1.cp1_products_api.repository.ProductRepository;

@Service
public class ProductService {
    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public List<Product> findAll() {
        return repo.findAll();
    }

    public Product findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    public Product create(ProductDTO dto) {
        Product p = new Product();
        p.setName(dto.name());
        p.setPrice(dto.price());
        p.setStock(dto.stock());
        return repo.save(p);
    }

    public Product update(Long id, ProductDTO dto) {
        Product p = findById(id);
        p.setName(dto.name());
        p.setPrice(dto.price());
        p.setStock(dto.stock());
        return repo.save(p);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
