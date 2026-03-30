package com.rcoem.filmrentalstore.repository;

import com.rcoem.filmrentalstore.entities.Film;
import com.rcoem.filmrentalstore.entities.Language;
import com.rcoem.filmrentalstore.enums.Rating;
import com.rcoem.filmrentalstore.enums.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.math.BigDecimal;
import java.util.List;
import java.sql.Timestamp;
import java.util.HashSet;
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
    private HashSet<Set> set;
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
        film.setRentalRate(BigDecimal.valueOf(20.00));
        film.setLength(148);
        film.setReplacementCost(BigDecimal.valueOf(500.00));
        film.setRating(Rating.PG_13);
        set = new HashSet<>();
        set.add(Set.BEHIND_THE_SCENES);
        film.setSpecialFeatures(set);
        film.setLanguage(language);
        film.setLastUpdate(new Timestamp(System.currentTimeMillis()));
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
        assertEquals(BigDecimal.valueOf(20.00), result.getRentalRate());
        assertEquals(148, result.getLength());
        assertEquals(BigDecimal.valueOf(500.00), result.getReplacementCost());
        assertEquals(Rating.PG_13, result.getRating());
        assertEquals(Set.BEHIND_THE_SCENES, result.getSpecialFeatures());
    }

    @Test
    void testUpdateFilm() {
        Film savedFilm = filmRepository.save(film);

        savedFilm.setTitle("Inception Updated");
        savedFilm.setRentalRate(BigDecimal.valueOf(200.0));
        savedFilm.setLength(160);
        filmRepository.save(savedFilm);

        Optional<Film> retrieved = filmRepository.findById(savedFilm.getFilmId());

        assertTrue(retrieved.isPresent(), "Updated film should be present");

        Film result = retrieved.get();

        assertEquals("Inception Updated", result.getTitle());
        assertEquals(BigDecimal.valueOf(200.0), result.getRentalRate());
        assertEquals(160, result.getLength());
    }

    @Test
    void testFindAllFilms() {
        Film film2 = new Film();
        film2.setTitle("Interstellar");
        film2.setDescription("Space exploration");
        film2.setReleaseYear(2014);
        film2.setRentalDuration(5);
        film2.setRentalRate(BigDecimal.valueOf(20.00));
        film2.setLength(169);
        film2.setReplacementCost(BigDecimal.valueOf(400.00));
        film2.setRating(Rating.PG);
        film2.setSpecialFeatures(new HashSet<>());
        film2.getSpecialFeatures().add(Set.BEHIND_THE_SCENES);
        film2.setLanguage(film.getLanguage());

        filmRepository.save(film);
        filmRepository.save(film2);

        List<Film> allFilms = filmRepository.findAll();

        assertEquals(2, allFilms.size(), "There should be 2 films in the repository");
    }

    @Test
    void testFindByTitleContainingIgnoreCase() {
        filmRepository.save(film);

        List<Film> results = filmRepository.findByTitleContainingIgnoreCase("incep");

        assertFalse(results.isEmpty(), "Should find films matching partial title");
        assertEquals("Inception", results.get(0).getTitle());
    }

    @Test
    void testFindByTitleContainingIgnoreCase_NoMatch() {
        filmRepository.save(film);

        List<Film> results = filmRepository.findByTitleContainingIgnoreCase("avatar");

        assertTrue(results.isEmpty(), "Should return empty list when no title matches");
    }
}
