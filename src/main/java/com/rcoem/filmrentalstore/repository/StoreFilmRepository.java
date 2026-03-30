package com.rcoem.filmrentalstore.repository;

import com.rcoem.filmrentalstore.dto.FilmView;
import com.rcoem.filmrentalstore.entities.Film;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.awt.print.Pageable;

@RepositoryRestResource(excerptProjection = FilmView.class)
public interface StoreFilmRepository extends JpaRepository<Film,Short> {
    Page<Film> findByStore_Id(Byte id , Pageable pageable);
}
