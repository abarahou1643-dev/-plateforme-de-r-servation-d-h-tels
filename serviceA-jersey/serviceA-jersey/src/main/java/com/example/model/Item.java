package com.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String sku; // Code unique de produit (ex: P001)

    @Column(nullable = false)
    private String name;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    private int stock;

    private LocalDateTime updatedAt;

    // ✅ Relation ManyToOne : plusieurs Items → une seule Category
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    // ✅ Constructeurs
    public Item() {}

    public Item(String sku, String name, BigDecimal price, int stock, Category category) {
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.updatedAt = LocalDateTime.now();
    }

    // ✅ Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    // ✅ toString() utile pour debug
    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", sku='" + sku + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
