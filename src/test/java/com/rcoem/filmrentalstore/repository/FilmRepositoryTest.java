package com.rcoem.filmrentalstore.repository;


import com.rcoem.filmrentalstore.entities.Film;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
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

    private Film film;

    @BeforeEach
    void setUp() {

        film = new Film();
        film.setTitle("Inception");
        film.setDescription("A mind-bending thriller");
        film.setReleaseYear(2010);
        film.setRentalDuration(7);
        film.setRentalRate(150.0);
        film.setLength(148);
        film.setReplacementCost(500.0);
        film.setRating("PG-13");
        film.setSpecialFeatures("Trailers,Behind the Scenes");

    }

    @Test
    void testSaveAndFindFilm() {

     
        Film savedFilm = filmRepository.save(film);

        Optional<Film> retrieved = filmRepository.findById(savedFilm.getFilmId());

       
        assertTrue(retrieved.isPresent(), "Film should be present");

        Film result = retrieved.get();

        assertEquals("Inception", result.getTitle());
        assertEquals("A mind-bending thriller", result.getDescription());
        assertEquals(2010, result.getReleaseYear()); 
        assertEquals(7, result.getRentalDuration()); 
        assertEquals(150.0, result.getRentalRate()); 
        assertEquals(148, result.getLength());
        assertEquals(500.0, result.getReplacementCost()); 
        assertEquals("PG-13", result.getRating());
        assertEquals("Trailers,Behind the Scenes", result.getSpecialFeatures());

        assertNotNull(result.getLastUpdate(), "Timestamp should be auto-generated");
    }
}


