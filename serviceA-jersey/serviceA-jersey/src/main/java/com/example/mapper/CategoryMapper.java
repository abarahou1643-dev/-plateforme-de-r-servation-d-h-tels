package com.example.mapper;

import com.example.dto.CategoryRequestDTO;
import com.example.dto.CategoryResponseDTO;
import com.example.dto.CategorySummaryDTO;
import com.example.dto.ItemSummaryDTO;
import com.example.model.Category;
import com.example.model.Item;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper class to convert between Category entities and DTOs
 */
public class CategoryMapper {

    /**
     * Convert Category entity to CategoryResponseDTO
     */
    public static CategoryResponseDTO toResponseDTO(Category category) {
        if (category == null) {
            return null;
        }
        
        CategoryResponseDTO dto = new CategoryResponseDTO(
            category.getId(),
            category.getCode(),
            category.getName(),
            category.getUpdatedAt()
        );
        
        return dto;
    }

    /**
     * Convert Category entity to CategoryResponseDTO with items
     */
    public static CategoryResponseDTO toResponseDTOWithItems(Category category) {
        if (category == null) {
            return null;
        }
        
        CategoryResponseDTO dto = toResponseDTO(category);
        
        // Map items if present
        if (category.getItems() != null && !category.getItems().isEmpty()) {
            List<ItemSummaryDTO> itemSummaries = category.getItems().stream()
                .map(CategoryMapper::toItemSummaryDTO)
                .collect(Collectors.toList());
            dto.setItems(itemSummaries);
        }
        
        return dto;
    }

    /**
     * Convert Category entity to CategorySummaryDTO
     */
    public static CategorySummaryDTO toSummaryDTO(Category category) {
        if (category == null) {
            return null;
        }
        
        return new CategorySummaryDTO(
            category.getId(),
            category.getCode(),
            category.getName()
        );
    }

    /**
     * Convert CategoryRequestDTO to Category entity
     */
    public static Category toEntity(CategoryRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Category category = new Category();
        category.setCode(dto.getCode());
        category.setName(dto.getName());
        
        return category;
    }

    /**
     * Update existing Category entity with data from CategoryRequestDTO
     */
    public static void updateEntity(Category category, CategoryRequestDTO dto) {
        if (category == null || dto == null) {
            return;
        }
        
        category.setCode(dto.getCode());
        category.setName(dto.getName());
    }

    /**
     * Helper method to convert Item to ItemSummaryDTO
     */
    private static ItemSummaryDTO toItemSummaryDTO(Item item) {
        if (item == null) {
            return null;
        }
        
        return new ItemSummaryDTO(
            item.getId(),
            item.getSku(),
            item.getName(),
            item.getPrice(),
            item.getStock()
        );
    }

    /**
     * Convert list of Category entities to list of CategoryResponseDTOs
     */
    public static List<CategoryResponseDTO> toResponseDTOList(List<Category> categories) {
        if (categories == null) {
            return null;
        }
        
        return categories.stream()
            .map(CategoryMapper::toResponseDTO)
            .collect(Collectors.toList());
    }
}
