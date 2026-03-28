package com.rcoem.filmrentalstore.repository;

import com.rcoem.filmrentalstore.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(path = "categories")
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
