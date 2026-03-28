package com.rcoem.filmrentalstore.api;

import com.rcoem.filmrentalstore.entities.Category;
import com.rcoem.filmrentalstore.entities.Film;
import com.rcoem.filmrentalstore.entities.Language;
import com.rcoem.filmrentalstore.enums.Rating;
import com.rcoem.filmrentalstore.enums.Set;
import com.rcoem.filmrentalstore.repository.CategoryRepository;
import com.rcoem.filmrentalstore.repository.FilmCategoryRepository;
import com.rcoem.filmrentalstore.repository.FilmRepository;
import com.rcoem.filmrentalstore.repository.LanguageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FilmCategoryApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FilmCategoryRepository filmCategoryRepository;

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
        filmCategoryRepository.deleteAll();   // ✅ FIRST (child table)
        filmRepository.deleteAll();
        categoryRepository.deleteAll();
        languageRepository.deleteAll();

        Language language = new Language();
        language.setName("Spanish");
        languageRepository.save(language);

        film = new Film();
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
        film = filmRepository.save(film);

        category = new Category();
        category.setName("Action_" + System.currentTimeMillis());  // ✅ avoid duplicate
        category = categoryRepository.save(category);
    }

    @Test
    void testCreateFilmCategory() throws Exception {

        String json = """
        {
          "film": "http://localhost:8080/films/%d",
          "category": "http://localhost:8080/categories/%d"
        }
        """.formatted(film.getFilmId(), category.getCategoryId());

        mockMvc.perform(post("/filmCategories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void testGetAllFilmCategories() throws Exception {
        mockMvc.perform(get("/filmCategories"))
                .andExpect(status().isOk());
    }
}