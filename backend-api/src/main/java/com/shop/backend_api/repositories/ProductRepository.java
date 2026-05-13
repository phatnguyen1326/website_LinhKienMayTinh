package com.shop.backend_api.repositories;

import com.shop.backend_api.models.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    // Chỉ cần khai báo thế này, Spring Boot tự hiểu cách Thêm/Sửa/Xóa/Tìm kiếm
}