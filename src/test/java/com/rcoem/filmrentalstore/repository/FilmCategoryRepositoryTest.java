package com.rcoem.filmrentalstore.repository;

import com.rcoem.filmrentalstore.entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FilmCategoryRepositoryTest {

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FilmCategoryRepository filmCategoryRepository;
    @Autowired
    private LanguageRepository languageRepository;

    @BeforeEach
    void setup() {
        filmCategoryRepository.deleteAll();   // ✅ FIRST
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
        film.setRentalRate(0.0);
        film.setReplacementCost(0.0);
        film = filmRepository.save(film);

        Category category = new Category();
        category.setName("Action");
        category = categoryRepository.save(category);

        FilmCategory fc = new FilmCategory();
        fc.setFilm(film);
        fc.setCategory(category);

        FilmCategory saved = filmCategoryRepository.save(fc);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getFilm().getFilmId()).isEqualTo(film.getFilmId());
        assertThat(saved.getCategory().getCategoryId()).isEqualTo(category.getCategoryId());
    }
}