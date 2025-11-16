package com.benchmark.controller;

import com.benchmark.dto.ItemDTO;
import com.benchmark.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public ResponseEntity<Page<ItemDTO>> getAllItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(required = false) Long categoryId) {

        Pageable pageable = PageRequest.of(page, size);

        if (categoryId != null) {
            Page<ItemDTO> items = itemService.findByCategoryId(categoryId, pageable);
            return ResponseEntity.ok(items);
        } else {
            Page<ItemDTO> items = itemService.findAll(pageable);
            return ResponseEntity.ok(items);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDTO> getItemById(@PathVariable Long id) {
        ItemDTO item = itemService.findById(id);
        return ResponseEntity.ok(item);
    }

    @PostMapping
    public ResponseEntity<ItemDTO> createItem(@RequestBody ItemDTO itemDTO) {
        ItemDTO created = itemService.create(itemDTO);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemDTO> updateItem(
            @PathVariable Long id,
            @RequestBody ItemDTO itemDTO) {

        ItemDTO updated = itemService.update(id, itemDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        itemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}