package com.rcoem.filmrentalstore.repository;

import com.rcoem.filmrentalstore.entities.Film;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FilmRepositoryTest {

    @Autowired
    private FilmRepository filmRepository;

    private Film film;
    private Timestamp testTimestamp;

    @BeforeEach
    void setUp() {
         testTimestamp = Timestamp.from(Instant.now());
      
        film = new Film();
        film.setTitle("Inception");
        film.setDescription("A mind-bending thriller");
        film.setReleaseYear("2010");
        film.setRentalDuration(7L);
        film.setRentalRate(150L);
        film.setLength(148L);
        film.setReplacementCost(500L);
        film.setRating("PG-13");
        film.setSpecialFeatures("Trailers,Behind the Scenes");
        film.setTimestamp(testTimestamp);
        
    }

    @Test
    void testSaveAndFindFilm() {
       
        Film savedFilm = filmRepository.save(film);

        
        Optional<Film> retrieved = filmRepository.findById(savedFilm.getFilmId());

      
        assertTrue(retrieved.isPresent(), "Film should be present in the repository");
        assertEquals("Inception", retrieved.get().getTitle(), "Title should match");
        assertEquals("A mind-bending thriller", retrieved.get().getDescription(), "Description should match");
        assertEquals("2010", retrieved.get().getReleaseYear(), "Release year should match");
        assertEquals(7L, retrieved.get().getRentalDuration(), "Rental duration should match");
        assertEquals(150L, retrieved.get().getRentalRate(), "Rental rate should match");
        assertEquals(148L, retrieved.get().getLength(), "Length should match");
        assertEquals(500L, retrieved.get().getReplacementCost(), "Replacement cost should match");
        assertEquals("PG-13", retrieved.get().getRating(), "Rating should match");
        assertEquals("Trailers,Behind the Scenes", retrieved.get().getSpecialFeatures(), "Special features should match");
        assertNotNull(retrieved.get().getTimestamp(), "Timestamp should be automatically generated");
    }
}
