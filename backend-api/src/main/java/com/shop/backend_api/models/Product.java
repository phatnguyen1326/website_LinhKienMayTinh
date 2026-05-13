package com.shop.backend_api.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data // Tự động sinh Getter, Setter nhờ Lombok
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "products")
public class Product {
    
    @Id
    private String id;
    
    private String name;            // Ví dụ: "VGA RTX 4060 Ti"
    private String slug;            // Ví dụ: "vga-rtx-4060-ti"
    private String description;     // Mô tả chi tiết
    private double price;           // Giá bán
    private double comparePrice;    // Giá gốc (để hiển thị gạch ngang giảm giá)
    
    private String category;        // Ví dụ: "VGA", "CPU", "RAM"
    private String brand;           // Ví dụ: "ASUS", "MSI", "Gigabyte"
    
    private List<String> imageUrls; // Danh sách link ảnh
    private int stock;              // Số lượng tồn kho
    
    // Trường này rất quan trọng để sau này làm AI Semantic Search
    private List<Double> vectorEmbedding; 
    
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
}