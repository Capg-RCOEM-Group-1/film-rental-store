package com.rcoem.filmrentalstore.api;

import com.rcoem.filmrentalstore.entities.Category;
import com.rcoem.filmrentalstore.entities.Film;
import com.rcoem.filmrentalstore.entities.Language;
import com.rcoem.filmrentalstore.enums.Rating;
import com.rcoem.filmrentalstore.enums.Set;
import com.rcoem.filmrentalstore.repository.CategoryRepository;

import com.rcoem.filmrentalstore.repository.FilmRepository;
import com.rcoem.filmrentalstore.repository.LanguageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashSet;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class FilmCategoryApiTest {

    @Autowired
    private MockMvc mockMvc;

   /* @Autowired
    private FilmCategoryRepository filmCategoryRepository;
*/
    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private LanguageRepository languageRepository;

    private Film film;
    private Category category;

    @BeforeEach
    void setup() {
        Language language = new Language();
        language.setName("Spanish");
        languageRepository.save(language);

        film = new Film();
        film.setTitle("Inception");
        film.setDescription("A mind-bending thriller");
        film.setReleaseYear(2010);
        film.setRentalDuration(7);
        film.setRentalRate(BigDecimal.valueOf(15.0));
        System.out.println("FINAL VALUE = " + film.getRentalRate());
        film.setLength(148);
        film.setReplacementCost(BigDecimal.valueOf(50.0));
        film.setRating(Rating.PG_13);
        film.setSpecialFeatures(new HashSet<>());
        film.setLanguage(language);
        film = filmRepository.save(film);

        category = new Category();
        category.setName("Action_" + System.currentTimeMillis());
        category = categoryRepository.save(category);
    }

    @Test
    void testGetAllFilmCategories() throws Exception {
        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk());
    }
}