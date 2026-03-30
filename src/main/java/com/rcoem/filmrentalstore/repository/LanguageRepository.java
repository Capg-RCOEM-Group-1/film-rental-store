package com.rcoem.filmrentalstore.repository;

import com.rcoem.filmrentalstore.dto.LanguageView;
import com.rcoem.filmrentalstore.entities.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(excerptProjection = LanguageView.class)
public interface LanguageRepository extends JpaRepository<Language,Byte> {
    boolean existsByNameIgnoreCase(String name);
}
