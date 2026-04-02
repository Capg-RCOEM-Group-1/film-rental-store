package com.rcoem.filmrentalstore.repository;

import com.rcoem.filmrentalstore.dto.LanguageView;
import com.rcoem.filmrentalstore.entities.Language;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(excerptProjection = LanguageView.class)
public interface LanguageRepository extends JpaRepository<Language,Byte> {
    boolean existsByNameIgnoreCase(String name);
    Page<Language> findByNameIgnoreCase(String name, Pageable pageable);
    Page<Language> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
