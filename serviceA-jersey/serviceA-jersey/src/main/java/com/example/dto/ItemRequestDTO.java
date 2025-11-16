package com.example.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * DTO for creating or updating an Item
 */
public class ItemRequestDTO {
    
    @NotBlank(message = "SKU is required")
    @Size(min = 2, max = 50, message = "SKU must be between 2 and 50 characters")
    private String sku;
    
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;
    
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;
    
    @Min(value = 0, message = "Stock cannot be negative")
    private int stock;
    
    @NotNull(message = "Category ID is required")
    private Long categoryId;

    // Constructors
    public ItemRequestDTO() {}

    public ItemRequestDTO(String sku, String name, BigDecimal price, int stock, Long categoryId) {
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.categoryId = categoryId;
    }

    // Getters and Setters
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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
