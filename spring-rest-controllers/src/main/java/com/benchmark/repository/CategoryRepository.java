package com.benchmark.repository;

import com.benchmark.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.items WHERE c.id = :id")
    Category findByIdWithItems(@Param("id") Long id);

    Page<Category> findAll(Pageable pageable);
}