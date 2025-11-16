package com.example.service;

import com.example.dao.CategoryDAO;
import com.example.dto.CategoryRequestDTO;
import com.example.dto.CategoryResponseDTO;
import com.example.dto.ItemSummaryDTO;
import com.example.exception.ResourceNotFoundException;
import com.example.exception.ValidationException;
import com.example.mapper.CategoryMapper;
import com.example.model.Category;
import com.example.model.Item;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for Category business logic
 * Handles validation and orchestrates DAO operations
 */
public class CategoryService {

    private final CategoryDAO categoryDAO;

    public CategoryService() {
        this.categoryDAO = new CategoryDAO();
    }

    // Constructor for dependency injection (useful for testing)
    public CategoryService(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    /**
     * Get all categories with pagination
     * 
     * @param page Page number
     * @param size Page size
     * @return List of category DTOs
     */
    public List<CategoryResponseDTO> getAllCategories(int page, int size) {
        validatePagination(page, size);
        
        List<Category> categories = categoryDAO.findAll(page, size);
        return CategoryMapper.toResponseDTOList(categories);
    }

    /**
     * Get a category by ID
     * 
     * @param id Category ID
     * @return Category DTO
     * @throws ResourceNotFoundException if not found
     */
    public CategoryResponseDTO getCategoryById(Long id) {
        Category category = categoryDAO.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category", id));
        
        return CategoryMapper.toResponseDTO(category);
    }

    /**
     * Get a category by ID with its items
     * 
     * @param id Category ID
     * @return Category DTO with items
     * @throws ResourceNotFoundException if not found
     */
    public CategoryResponseDTO getCategoryWithItems(Long id) {
        Category category = categoryDAO.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category", id));
        
        // Fetch items for this category
        List<Item> items = categoryDAO.findItemsByCategory(id);
        
        CategoryResponseDTO dto = CategoryMapper.toResponseDTO(category);
        
        // Convert items to summary DTOs
        List<ItemSummaryDTO> itemSummaries = items.stream()
            .map(item -> new ItemSummaryDTO(
                item.getId(),
                item.getSku(),
                item.getName(),
                item.getPrice(),
                item.getStock()
            ))
            .collect(Collectors.toList());
        
        dto.setItems(itemSummaries);
        return dto;
    }

    /**
     * Create a new category
     * 
     * @param requestDTO Category data
     * @return Created category DTO
     * @throws ValidationException if validation fails
     */
    public CategoryResponseDTO createCategory(CategoryRequestDTO requestDTO) {
        validateCategoryRequest(requestDTO);
        
        // Check if code already exists
        if (categoryDAO.existsByCode(requestDTO.getCode(), null)) {
            throw new ValidationException("Category code '" + requestDTO.getCode() + "' already exists");
        }
        
        Category category = CategoryMapper.toEntity(requestDTO);
        Category savedCategory = categoryDAO.save(category);
        
        return CategoryMapper.toResponseDTO(savedCategory);
    }

    /**
     * Update an existing category
     * 
     * @param id Category ID
     * @param requestDTO Updated category data
     * @return Updated category DTO
     * @throws ResourceNotFoundException if not found
     * @throws ValidationException if validation fails
     */
    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO requestDTO) {
        validateCategoryRequest(requestDTO);
        
        // Check if category exists
        Category existingCategory = categoryDAO.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category", id));
        
        // Check if code is being changed and if new code already exists
        if (!existingCategory.getCode().equals(requestDTO.getCode()) 
            && categoryDAO.existsByCode(requestDTO.getCode(), id)) {
            throw new ValidationException("Category code '" + requestDTO.getCode() + "' already exists");
        }
        
        // Update entity with new data
        CategoryMapper.updateEntity(existingCategory, requestDTO);
        
        Category updatedCategory = categoryDAO.update(existingCategory);
        return CategoryMapper.toResponseDTO(updatedCategory);
    }

    /**
     * Delete a category
     * 
     * @param id Category ID
     * @throws ResourceNotFoundException if not found
     */
    public void deleteCategory(Long id) {
        // Check if category exists
        if (!categoryDAO.existsById(id)) {
            throw new ResourceNotFoundException("Category", id);
        }
        
        // Check if category has items
        List<Item> items = categoryDAO.findItemsByCategory(id);
        if (!items.isEmpty()) {
            throw new ValidationException(
                "Cannot delete category with " + items.size() + " associated item(s). " +
                "Please delete or reassign the items first."
            );
        }
        
        categoryDAO.delete(id);
    }

    /**
     * Get total count of categories
     * 
     * @return Total count
     */
    public long getCategoryCount() {
        return categoryDAO.count();
    }

    /**
     * Validate pagination parameters
     */
    private void validatePagination(int page, int size) {
        if (page < 0) {
            throw new ValidationException("Page number cannot be negative");
        }
        if (size <= 0) {
            throw new ValidationException("Page size must be greater than 0");
        }
        if (size > 100) {
            throw new ValidationException("Page size cannot exceed 100");
        }
    }

    /**
     * Validate category request DTO
     */
    private void validateCategoryRequest(CategoryRequestDTO requestDTO) {
        if (requestDTO == null) {
            throw new ValidationException("Category data is required");
        }
        
        if (requestDTO.getCode() == null || requestDTO.getCode().trim().isEmpty()) {
            throw new ValidationException("Category code is required");
        }
        
        if (requestDTO.getName() == null || requestDTO.getName().trim().isEmpty()) {
            throw new ValidationException("Category name is required");
        }
        
        if (requestDTO.getCode().length() < 2 || requestDTO.getCode().length() > 50) {
            throw new ValidationException("Category code must be between 2 and 50 characters");
        }
        
        if (requestDTO.getName().length() < 2 || requestDTO.getName().length() > 100) {
            throw new ValidationException("Category name must be between 2 and 100 characters");
        }
    }
}
