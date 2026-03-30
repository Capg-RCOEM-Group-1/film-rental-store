package com.rcoem.filmrentalstore.repository;

import com.rcoem.filmrentalstore.dto.CategoryProjection;
import com.rcoem.filmrentalstore.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.awt.print.Pageable;

@Repository
@RepositoryRestResource(path = "categories", excerptProjection = CategoryProjection.class)
public interface CategoryRepository extends JpaRepository<Category, Byte> {

    @RestResource(path = "byName")
    Page<Category> findByNameContainingIgnoreCase(@RequestParam("name") String name, Pageable pageable);
}
