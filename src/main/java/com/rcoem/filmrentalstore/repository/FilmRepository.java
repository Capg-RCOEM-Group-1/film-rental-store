package com.rcoem.filmrentalstore.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import com.rcoem.filmrentalstore.entities.Film;


@RepositoryRestResource(path = "films")
public interface FilmRepository extends JpaRepository<Film, Short> {

    @RestResource(path = "byTitle")
    List<Film> findByTitleContainingIgnoreCase(@Param("title") String title);

     Page<Film> findByLanguage_Id(Byte id, Pageable pageable);
    
}

