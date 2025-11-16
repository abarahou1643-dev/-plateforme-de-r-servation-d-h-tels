package com.mliaedu.benchmark.repository;

import com.mliaedu.benchmark.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(collectionResourceRel = "items", path = "items")
public interface ItemRepository extends JpaRepository<Item, Long> {

    // Recherche par categoryId avec pagination
    // Exposé automatiquement comme: GET /items/search/findByCategoryId?categoryId=123
    @RestResource(path = "by-category", rel = "byCategory")
    Page<Item> findByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

    // Version optimisée avec JOIN FETCH pour éviter N+1
    @Query("SELECT i FROM Item i JOIN FETCH i.category WHERE i.category.id = :categoryId")
    @RestResource(path = "by-category-optimized", rel = "byCategoryOptimized")
    Page<Item> findByCategoryIdWithCategory(@Param("categoryId") Long categoryId, Pageable pageable);

    // Recherche par SKU (non exposé)
    @RestResource(exported = false)
    Item findBySku(String sku);
}

