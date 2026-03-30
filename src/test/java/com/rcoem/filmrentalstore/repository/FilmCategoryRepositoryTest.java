package com.rcoem.filmrentalstore.repository;

import com.rcoem.filmrentalstore.entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FilmCategoryRepositoryTest {

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private CategoryRepository categoryRepository;

   /* @Autowired
    private FilmCategoryRepository filmCategoryRepository;*/
    @Autowired
    private LanguageRepository languageRepository;

    @BeforeEach
    void setup() {
        /*filmCategoryRepository.deleteAll();*/   // ✅ FIRST
        filmRepository.deleteAll();
        categoryRepository.deleteAll();
        languageRepository.deleteAll();
    }

    @Test
    void testCreateFilmCategory() {
        Language language = new Language();
        language.setName("");
        Film film = new Film();
        film.setTitle("Test Film");

        film.setLanguage(language);
        languageRepository.save(language);
        film.setRentalDuration(9);
        film.setRentalRate(BigDecimal.valueOf(0.0));
        film.setReplacementCost(BigDecimal.valueOf(0.0));
        film = filmRepository.save(film);

        Category category = new Category();
        category.setName("Action");
        List<Film> films = new ArrayList<>();
        films.add(film);
        category.setFilms(films);
        category = categoryRepository.save(category);
        assertThat(category.getFilms()).contains(film);
    }
}