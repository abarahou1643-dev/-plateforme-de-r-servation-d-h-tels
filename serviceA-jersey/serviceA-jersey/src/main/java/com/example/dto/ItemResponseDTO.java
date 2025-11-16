package com.example.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for Item responses with full details
 */
public class ItemResponseDTO {
    private Long id;
    private String sku;
    private String name;
    private BigDecimal price;
    private int stock;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
    
    private CategorySummaryDTO category;

    // Constructors
    public ItemResponseDTO() {}

    public ItemResponseDTO(Long id, String sku, String name, BigDecimal price, int stock, LocalDateTime updatedAt) {
        this.id = id;
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public CategorySummaryDTO getCategory() {
        return category;
    }

    public void setCategory(CategorySummaryDTO category) {
        this.category = category;
    }
}
