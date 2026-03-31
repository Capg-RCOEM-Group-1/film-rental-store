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

    @Test
    public void testFindById(){
        Category category = new Category();
        category.setName("Comedy");
        categoryRepository.save(category);

        Optional<Category> category1 = categoryRepository.findById(category.getCategoryId());

        assertThat(category1).isPresent();
        assertThat(category.getCategoryId()).isEqualTo(category1.get().getCategoryId());
    }
}
