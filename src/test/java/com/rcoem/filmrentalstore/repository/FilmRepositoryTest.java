package com.rcoem.filmrentalstore.repository;

import com.rcoem.filmrentalstore.entities.Film;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class FilmRepositoryTest {

    @Autowired
    private FilmRepository filmRepository;

    @Test
    void testGetAllFilms() {

        Film film = new Film();
        film.setTitle("Inception");
        filmRepository.save(film);

       
        Optional<Film> fm = filmRepository.findById(film.getFilmId());

       
        assertTrue(fm.isPresent());
        assertEquals("Inception", fm.get().getTitle());
    }
}
