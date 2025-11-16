package com.example.mapper;

import com.example.dto.CategorySummaryDTO;
import com.example.dto.ItemRequestDTO;
import com.example.dto.ItemResponseDTO;
import com.example.model.Item;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper class to convert between Item entities and DTOs
 */
public class ItemMapper {

    /**
     * Convert Item entity to ItemResponseDTO
     */
    public static ItemResponseDTO toResponseDTO(Item item) {
        if (item == null) {
            return null;
        }
        
        ItemResponseDTO dto = new ItemResponseDTO(
            item.getId(),
            item.getSku(),
            item.getName(),
            item.getPrice(),
            item.getStock(),
            item.getUpdatedAt()
        );
        
        // Add category information if present
        if (item.getCategory() != null) {
            CategorySummaryDTO categoryDTO = new CategorySummaryDTO(
                item.getCategory().getId(),
                item.getCategory().getCode(),
                item.getCategory().getName()
            );
            dto.setCategory(categoryDTO);
        }
        
        return dto;
    }

    /**
     * Convert ItemRequestDTO to Item entity
     * Note: Category must be set separately after fetching from database
     */
    public static Item toEntity(ItemRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Item item = new Item();
        item.setSku(dto.getSku());
        item.setName(dto.getName());
        item.setPrice(dto.getPrice());
        item.setStock(dto.getStock());
        // Category will be set by the service layer
        
        return item;
    }

    /**
     * Update existing Item entity with data from ItemRequestDTO
     * Note: Category must be updated separately if categoryId changes
     */
    public static void updateEntity(Item item, ItemRequestDTO dto) {
        if (item == null || dto == null) {
            return;
        }
        
        item.setSku(dto.getSku());
        item.setName(dto.getName());
        item.setPrice(dto.getPrice());
        item.setStock(dto.getStock());
        // Category will be updated by the service layer if needed
    }

    /**
     * Convert list of Item entities to list of ItemResponseDTOs
     */
    public static List<ItemResponseDTO> toResponseDTOList(List<Item> items) {
        if (items == null) {
            return null;
        }
        
        return items.stream()
            .map(ItemMapper::toResponseDTO)
            .collect(Collectors.toList());
    }
}
