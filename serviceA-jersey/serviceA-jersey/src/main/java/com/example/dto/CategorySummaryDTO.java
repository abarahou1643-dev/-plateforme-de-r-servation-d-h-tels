package com.example.dto;

/**
 * Simplified DTO for Category summary (used in Item responses)
 */
public class CategorySummaryDTO {
    private Long id;
    private String code;
    private String name;

    // Constructors
    public CategorySummaryDTO() {}

    public CategorySummaryDTO(Long id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
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
}
