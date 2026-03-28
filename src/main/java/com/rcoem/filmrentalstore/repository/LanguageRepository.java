package com.rcoem.filmrentalstore.repository;

import com.rcoem.filmrentalstore.entities.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


public interface LanguageRepository extends JpaRepository<Language,Long> {
}
