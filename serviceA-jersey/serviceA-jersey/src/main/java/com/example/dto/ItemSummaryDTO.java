package com.example.dto;

import java.math.BigDecimal;

/**
 * Simplified DTO for Item summary (used in Category responses)
 */
public class ItemSummaryDTO {
    private Long id;
    private String sku;
    private String name;
    private BigDecimal price;
    private int stock;

    // Constructors
    public ItemSummaryDTO() {}

    public ItemSummaryDTO(Long id, String sku, String name, BigDecimal price, int stock) {
        this.id = id;
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.stock = stock;
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
}
