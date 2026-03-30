package com.rcoem.filmrentalstore.eventHandler;

import com.rcoem.filmrentalstore.entities.Category;
import com.rcoem.filmrentalstore.exception.DuplicateException;
import com.rcoem.filmrentalstore.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler(Category.class)
public class CategoryEventHandler {

    @Autowired
    private CategoryRepository categoryRepository;

    @HandleBeforeCreate
    public void handleBeforeCreate(Category category) {
        if (categoryRepository.existsByNameIgnoreCase(category.getName())) {
            throw new DuplicateException("Category already exists");
        }
    }

    @HandleBeforeSave
    public void handleBeforeUpdate(Category category) {
        if (categoryRepository.existsByNameIgnoreCase(category.getName())) {
            throw new DuplicateException("Category already exists");
        }
    }
}