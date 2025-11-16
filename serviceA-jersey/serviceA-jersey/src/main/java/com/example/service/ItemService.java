package com.example.service;

import com.example.dao.CategoryDAO;
import com.example.dao.ItemDAO;
import com.example.dto.ItemRequestDTO;
import com.example.dto.ItemResponseDTO;
import com.example.exception.ResourceNotFoundException;
import com.example.exception.ValidationException;
import com.example.mapper.ItemMapper;
import com.example.model.Category;
import com.example.model.Item;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service layer for Item business logic
 * Handles validation and orchestrates DAO operations
 */
public class ItemService {

    private final ItemDAO itemDAO;
    private final CategoryDAO categoryDAO;

    public ItemService() {
        this.itemDAO = new ItemDAO();
        this.categoryDAO = new CategoryDAO();
    }

    // Constructor for dependency injection (useful for testing)
    public ItemService(ItemDAO itemDAO, CategoryDAO categoryDAO) {
        this.itemDAO = itemDAO;
        this.categoryDAO = categoryDAO;
    }

    /**
     * Get all items with pagination
     * 
     * @param page Page number
     * @param size Page size
     * @return List of item DTOs
     */
    public List<ItemResponseDTO> getAllItems(int page, int size) {
        validatePagination(page, size);
        
        List<Item> items = itemDAO.findAll(page, size);
        return ItemMapper.toResponseDTOList(items);
    }

    /**
     * Get an item by ID
     * 
     * @param id Item ID
     * @return Item DTO
     * @throws ResourceNotFoundException if not found
     */
    public ItemResponseDTO getItemById(Long id) {
        Item item = itemDAO.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Item", id));
        
        return ItemMapper.toResponseDTO(item);
    }

    /**
     * Search items by name
     * 
     * @param keyword Search keyword
     * @return List of matching items
     */
    public List<ItemResponseDTO> searchItemsByName(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new ValidationException("Search keyword is required");
        }
        
        List<Item> items = itemDAO.searchByName(keyword);
        return ItemMapper.toResponseDTOList(items);
    }

    /**
     * Get items by category ID
     * 
     * @param categoryId Category ID
     * @return List of items
     */
    public List<ItemResponseDTO> getItemsByCategoryId(Long categoryId) {
        // Verify category exists
        if (!categoryDAO.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category", categoryId);
        }
        
        List<Item> items = itemDAO.findByCategoryId(categoryId);
        return ItemMapper.toResponseDTOList(items);
    }

    /**
     * Create a new item
     * 
     * @param requestDTO Item data
     * @return Created item DTO
     * @throws ValidationException if validation fails
     */
    public ItemResponseDTO createItem(ItemRequestDTO requestDTO) {
        validateItemRequest(requestDTO);
        
        // Check if SKU already exists
        if (itemDAO.existsBySku(requestDTO.getSku(), null)) {
            throw new ValidationException("Item SKU '" + requestDTO.getSku() + "' already exists");
        }
        
        // Fetch and validate category
        Category category = categoryDAO.findById(requestDTO.getCategoryId())
            .orElseThrow(() -> new ResourceNotFoundException("Category", requestDTO.getCategoryId()));
        
        // Create item entity
        Item item = ItemMapper.toEntity(requestDTO);
        item.setCategory(category);
        
        Item savedItem = itemDAO.save(item);
        return ItemMapper.toResponseDTO(savedItem);
    }

    /**
     * Update an existing item
     * 
     * @param id Item ID
     * @param requestDTO Updated item data
     * @return Updated item DTO
     * @throws ResourceNotFoundException if not found
     * @throws ValidationException if validation fails
     */
    public ItemResponseDTO updateItem(Long id, ItemRequestDTO requestDTO) {
        validateItemRequest(requestDTO);
        
        // Check if item exists
        Item existingItem = itemDAO.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Item", id));
        
        // Check if SKU is being changed and if new SKU already exists
        if (!existingItem.getSku().equals(requestDTO.getSku()) 
            && itemDAO.existsBySku(requestDTO.getSku(), id)) {
            throw new ValidationException("Item SKU '" + requestDTO.getSku() + "' already exists");
        }
        
        // Fetch and validate category if it's being changed
        if (!existingItem.getCategory().getId().equals(requestDTO.getCategoryId())) {
            Category newCategory = categoryDAO.findById(requestDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", requestDTO.getCategoryId()));
            existingItem.setCategory(newCategory);
        }
        
        // Update entity with new data
        ItemMapper.updateEntity(existingItem, requestDTO);
        
        Item updatedItem = itemDAO.update(existingItem);
        return ItemMapper.toResponseDTO(updatedItem);
    }

    /**
     * Delete an item
     * 
     * @param id Item ID
     * @throws ResourceNotFoundException if not found
     */
    public void deleteItem(Long id) {
        // Check if item exists
        if (!itemDAO.existsById(id)) {
            throw new ResourceNotFoundException("Item", id);
        }
        
        itemDAO.delete(id);
    }

    /**
     * Update item stock
     * 
     * @param id Item ID
     * @param quantity Stock quantity to add (can be negative)
     * @return Updated item DTO
     */
    public ItemResponseDTO updateStock(Long id, int quantity) {
        Item item = itemDAO.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Item", id));
        
        int newStock = item.getStock() + quantity;
        if (newStock < 0) {
            throw new ValidationException("Insufficient stock. Current stock: " + item.getStock());
        }
        
        item.setStock(newStock);
        Item updatedItem = itemDAO.update(item);
        
        return ItemMapper.toResponseDTO(updatedItem);
    }

    /**
     * Get total count of items
     * 
     * @return Total count
     */
    public long getItemCount() {
        return itemDAO.count();
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
     * Validate item request DTO
     */
    private void validateItemRequest(ItemRequestDTO requestDTO) {
        if (requestDTO == null) {
            throw new ValidationException("Item data is required");
        }
        
        if (requestDTO.getSku() == null || requestDTO.getSku().trim().isEmpty()) {
            throw new ValidationException("Item SKU is required");
        }
        
        if (requestDTO.getName() == null || requestDTO.getName().trim().isEmpty()) {
            throw new ValidationException("Item name is required");
        }
        
        if (requestDTO.getPrice() == null) {
            throw new ValidationException("Item price is required");
        }
        
        if (requestDTO.getCategoryId() == null) {
            throw new ValidationException("Category ID is required");
        }
        
        if (requestDTO.getSku().length() < 2 || requestDTO.getSku().length() > 50) {
            throw new ValidationException("Item SKU must be between 2 and 50 characters");
        }
        
        if (requestDTO.getName().length() < 2 || requestDTO.getName().length() > 100) {
            throw new ValidationException("Item name must be between 2 and 100 characters");
        }
        
        if (requestDTO.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Item price must be greater than 0");
        }
        
        if (requestDTO.getStock() < 0) {
            throw new ValidationException("Item stock cannot be negative");
        }
    }
}
