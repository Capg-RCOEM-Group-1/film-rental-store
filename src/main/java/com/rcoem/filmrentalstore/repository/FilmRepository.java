package com.rcoem.filmrentalstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.rcoem.filmrentalstore.entities.Film;

public interface FilmRepository extends JpaRepository<Film, Short> {
    
}

