package com.rcoem.filmrentalstore.repository;

import com.rcoem.filmrentalstore.dto.FilmView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.rcoem.filmrentalstore.entities.Film;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(excerptProjection = FilmView.class)
public interface FilmRepository extends JpaRepository<Film, Short> {
    Page<Film> findByLanguage_Id(Byte id, Pageable pageable);
}

