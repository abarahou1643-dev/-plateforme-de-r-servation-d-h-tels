package com.benchmark.service;

import com.benchmark.dto.ItemDTO;
import com.benchmark.entity.Category;
import com.benchmark.entity.Item;
import com.benchmark.repository.CategoryRepository;
import com.benchmark.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;

    public Page<ItemDTO> findAll(Pageable pageable) {
        return itemRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    public ItemDTO findById(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));
        return convertToDTO(item);
    }

    public Page<ItemDTO> findByCategoryId(Long categoryId, Pageable pageable) {
        return itemRepository.findByCategoryId(categoryId, pageable)
                .map(this::convertToDTO);
    }

    public ItemDTO create(ItemDTO itemDTO) {
        Category category = categoryRepository.findById(itemDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + itemDTO.getCategoryId()));

        Item item = new Item();
        item.setName(itemDTO.getName());
        item.setPrice(itemDTO.getPrice());
        item.setStock(itemDTO.getStock());
        item.setDescription(itemDTO.getDescription());
        item.setCategory(category);

        Item saved = itemRepository.save(item);
        return convertToDTO(saved);
    }

    public ItemDTO update(Long id, ItemDTO itemDTO) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));

        if (!item.getCategory().getId().equals(itemDTO.getCategoryId())) {
            Category category = categoryRepository.findById(itemDTO.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + itemDTO.getCategoryId()));
            item.setCategory(category);
        }

        item.setName(itemDTO.getName());
        item.setPrice(itemDTO.getPrice());
        item.setStock(itemDTO.getStock());
        item.setDescription(itemDTO.getDescription());

        Item updated = itemRepository.save(item);
        return convertToDTO(updated);
    }

    public void delete(Long id) {
        itemRepository.deleteById(id);
    }

    private ItemDTO convertToDTO(Item item) {
        ItemDTO dto = new ItemDTO();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setPrice(item.getPrice());
        dto.setStock(item.getStock());
        dto.setDescription(item.getDescription());
        dto.setCategoryId(item.getCategory().getId());
        dto.setCreatedAt(item.getCreatedAt());
        dto.setUpdatedAt(item.getUpdatedAt());
        return dto;
    }
}