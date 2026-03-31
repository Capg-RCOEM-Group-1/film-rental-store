package com.rcoem.filmrentalstore.repository;

import com.rcoem.filmrentalstore.entities.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CategoryRepositoryTesting {
    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    public void cleanup() {
//        categoryRepository.deleteAll();
    }

    // Test saving a category and retrieving it by ID
    @Test
    public void testFindById(){
        Category category = new Category();
        category.setName("Comedy");
        categoryRepository.save(category);

        Optional<Category> category1 = categoryRepository.findById(category.getCategoryId());

        assertThat(category1).isPresent();
        assertThat(category.getCategoryId()).isEqualTo(category1.get().getCategoryId());
    }

    // Test the custom query method existsByNameIgnoreCase
    @Test
    void testExistsByNameIgnoreCase() {
        Category category = new Category();
        category.setName("Action");
        categoryRepository.save(category);

        boolean exists = categoryRepository.existsByNameIgnoreCase("action");

        assertThat(exists).isTrue();
    }

    // Test the custom query method existsByNameIgnoreCase with a non-existing name
    @Test
    void testExistsByNameIgnoreCase_NotFound() {
        boolean exists = categoryRepository.existsByNameIgnoreCase("xyz");

        assertThat(exists).isFalse();
    }

    // Test deleting a category and ensuring it no longer exists
    @Test
    void testDeleteCategory() {
        Category category = new Category();
        category.setName("Drama");
        category = categoryRepository.save(category);

        categoryRepository.deleteById(category.getCategoryId());

        Optional<Category> result = categoryRepository.findById(category.getCategoryId());

        assertThat(result).isEmpty();
    }

    // Test saving multiple categories and counting them
    @Test
    void testSaveMultipleCategories() {
        Category c1 = new Category();
        c1.setName("Action");

        Category c2 = new Category();
        c2.setName("Drama");

        categoryRepository.save(c1);
        categoryRepository.save(c2);

        assertThat(categoryRepository.count()).isEqualTo(3);
    }

    // Test updating a category's name and verifying the change
    @Test
    void testUpdateCategory() {
        Category category = new Category();
        category.setName("Action");
        category = categoryRepository.save(category);

        category.setName("Updated");
        categoryRepository.save(category);

        Category updated = categoryRepository.findById(category.getCategoryId()).get();

        assertThat(updated.getName()).isEqualTo("Updated");
    }
}
