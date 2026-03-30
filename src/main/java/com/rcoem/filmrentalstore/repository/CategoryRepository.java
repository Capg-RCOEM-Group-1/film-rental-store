package com.rcoem.filmrentalstore.repository;

import com.rcoem.filmrentalstore.dto.CategoryProjection;
import com.rcoem.filmrentalstore.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable; // CORRECT IMPORT
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param; // REQUIRED IMPORT
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(path = "categories", excerptProjection = CategoryProjection.class)
public interface CategoryRepository extends JpaRepository<Category, Byte> {

    boolean existsByNameIgnoreCase(String name);

    @RestResource(path = "byName")
    Page<Category> findByNameContainingIgnoreCase(@Param("name") String name, Pageable pageable); // FIXED ANNOTATION
}