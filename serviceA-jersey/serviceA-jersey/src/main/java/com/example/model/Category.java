package com.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    // ✅ Relation OneToMany avec Item (1 catégorie → plusieurs items)
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Item> items;

    // ✅ Constructeurs
    public Category() {}

    public Category(String code, String name) {
        this.code = code;
        this.name = name;
        this.updatedAt = LocalDateTime.now();
    }

    // ✅ Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<Item> getItems() { return items; }
    public void setItems(List<Item> items) { this.items = items; }

    // ✅ toString() pour debug
    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
