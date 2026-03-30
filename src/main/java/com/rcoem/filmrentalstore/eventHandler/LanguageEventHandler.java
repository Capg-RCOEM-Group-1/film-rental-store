package com.rcoem.filmrentalstore.eventHandler;

import com.rcoem.filmrentalstore.entities.Language;
import com.rcoem.filmrentalstore.exception.DuplicateException;
import com.rcoem.filmrentalstore.repository.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler(Language.class)
public class LanguageEventHandler {

    @Autowired
    private LanguageRepository languageRepo;

    @HandleBeforeCreate
    public void handleBeforeCreate(Language language) {

        if (languageRepo.existsByNameIgnoreCase(language.getName())) {
            throw new DuplicateException("Language already exists: " + language.getName());
        }
    }
}
