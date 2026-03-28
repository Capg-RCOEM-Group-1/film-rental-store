package com.rcoem.filmrentalstore.repository;


import com.rcoem.filmrentalstore.entities.Film;

import com.rcoem.filmrentalstore.entities.Language;
import com.rcoem.filmrentalstore.enums.Rating;
import com.rcoem.filmrentalstore.enums.Set;
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
    @Autowired
    private LanguageRepository languageRepository;
    private Film film;

    @BeforeEach
    void setUp() {
        filmRepository.deleteAll();
        languageRepository.deleteAll();

        film = new Film();
        Language language = new Language();
        language.setName("A");
        languageRepository.save(language);
        film.setTitle("Inception");
        film.setDescription("A mind-bending thriller");
        film.setReleaseYear(2010);
        film.setRentalDuration(7);
        film.setRentalRate(150.0);
        film.setLength(148);
        film.setReplacementCost(500.0);
        film.setRating(Rating.PG_13);
        film.setSpecialFeatures(Set.BEHIND_THE_SCENES);
        film.setLanguage(language);

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
        assertEquals(Rating.PG_13, result.getRating());
        assertEquals(Set.BEHIND_THE_SCENES, result.getSpecialFeatures());

    }
}



