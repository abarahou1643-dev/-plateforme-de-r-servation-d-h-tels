package com.benchmark.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CategoryDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CategoryDTO() {}

    public CategoryDTO(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}