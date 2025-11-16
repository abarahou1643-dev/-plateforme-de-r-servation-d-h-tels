package com.mliaedu.benchmark.repository;

import com.mliaedu.benchmark.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(collectionResourceRel = "categories", path = "categories")
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @RestResource(exported = false)
    boolean existsByCode(String code);

    // Recherche par code (optionnel)
    @RestResource(path = "by-code", rel = "byCode")
    Category findByCode(@Param("code") String code);
}

