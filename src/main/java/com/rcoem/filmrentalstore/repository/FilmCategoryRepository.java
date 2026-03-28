package com.rcoem.filmrentalstore.repository;

import com.rcoem.filmrentalstore.entities.FilmCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FilmCategoryRepository extends JpaRepository<FilmCategory, Long> {

    List<FilmCategory> findByFilm_FilmId(Long filmId);

    List<FilmCategory> findByCategory_CategoryId(Long categoryId);
}