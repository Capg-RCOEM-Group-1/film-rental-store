package com.rcoem.filmrentalstore.repository;

import com.rcoem.filmrentalstore.entities.Film;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FilmRepositoryTest {

    @Autowired
    private FilmRepository filmRepository;

    @Test
    void testGetAllFilms() {
     
        Film film = new Film();
        film.setTitle("Inception");
        Film savedFilm = filmRepository.save(film);

       
        Optional<Film> fm = filmRepository.findById(savedFilm.getFilmId());

        assertTrue(fm.isPresent(), "Film should be present in the repository");
        assertEquals("Inception", fm.get().getTitle(), "Film title should match");
    }
}
