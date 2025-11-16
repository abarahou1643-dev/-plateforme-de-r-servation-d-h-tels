package com.example.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for Category responses with full details
 */
public class CategoryResponseDTO {
    private Long id;
    private String code;
    private String name;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
    
    private List<ItemSummaryDTO> items;

    // Constructors
    public CategoryResponseDTO() {}

    public CategoryResponseDTO(Long id, String code, String name, LocalDateTime updatedAt) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<ItemSummaryDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemSummaryDTO> items) {
        this.items = items;
    }
}
