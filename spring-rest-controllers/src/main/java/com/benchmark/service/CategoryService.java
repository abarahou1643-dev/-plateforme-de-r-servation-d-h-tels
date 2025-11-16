package com.benchmark.service;

import com.benchmark.dto.CategoryDTO;
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
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;

    public Page<CategoryDTO> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    public CategoryDTO findById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        return convertToDTO(category);
    }

    public CategoryDTO create(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());

        Category saved = categoryRepository.save(category);
        return convertToDTO(saved);
    }

    public CategoryDTO update(Long id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());

        Category updated = categoryRepository.save(category);
        return convertToDTO(updated);
    }

    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    public Page<ItemDTO> getCategoryItems(Long categoryId, Pageable pageable) {
        return itemRepository.findByCategoryId(categoryId, pageable)
                .map(this::convertItemToDTO);
    }

    private CategoryDTO convertToDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setCreatedAt(category.getCreatedAt());
        dto.setUpdatedAt(category.getUpdatedAt());
        return dto;
    }

    private ItemDTO convertItemToDTO(Item item) {
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