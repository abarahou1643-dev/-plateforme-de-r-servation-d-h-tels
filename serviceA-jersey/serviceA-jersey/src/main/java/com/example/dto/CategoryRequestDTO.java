package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for creating or updating a Category
 */
public class CategoryRequestDTO {
    
    @NotBlank(message = "Code is required")
    @Size(min = 2, max = 50, message = "Code must be between 2 and 50 characters")
    private String code;
    
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    // Constructors
    public CategoryRequestDTO() {}

    public CategoryRequestDTO(String code, String name) {
        this.code = code;
        this.name = name;
    }

    // Getters and Setters
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
}
