package com.rcoem.filmrentalstore.repository;

import java.util.List;

import com.rcoem.filmrentalstore.dto.FilmProjection;
import com.rcoem.filmrentalstore.dto.FilmView;
import com.rcoem.filmrentalstore.entities.Language;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import com.rcoem.filmrentalstore.entities.Film;


@RepositoryRestResource(excerptProjection = FilmProjection.class)
public interface FilmRepository extends JpaRepository<Film, Short> {

    @RestResource(path = "byTitle")
    List<Film> findByTitleContainingIgnoreCase(@Param("title") String title);

    Page<Film> findByLanguage_Id(@Param("id") Byte id, Pageable pageable);
    Page<Film> findByLanguage_IdAndTitleContainingIgnoreCase(Byte id, String title, Pageable pageable);

    @RestResource(path = "byCategoryId")
    Page<Film> findByCategories_CategoryId(@Param("categoryId") Byte categoryId, Pageable pageable);
}