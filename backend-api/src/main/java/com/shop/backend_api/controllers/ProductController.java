package com.shop.backend_api.controllers;

import com.shop.backend_api.models.Product;
import com.shop.backend_api.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    // API 1: Lấy danh sách linh kiện
    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // API 2: Thêm linh kiện mới vào Database
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }
}