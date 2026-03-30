package com.rcoem.filmrentalstore.api;

import com.rcoem.filmrentalstore.entities.Category;
import com.rcoem.filmrentalstore.entities.Film;
import com.rcoem.filmrentalstore.repository.CategoryRepository;
import com.rcoem.filmrentalstore.repository.FilmCategoryRepository;
import com.rcoem.filmrentalstore.repository.FilmRepository;
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

    private Film film;
    private Category category;

    @BeforeEach
    void setup() {
        filmCategoryRepository.deleteAll();   // ✅ FIRST (child table)
        filmRepository.deleteAll();
        categoryRepository.deleteAll();

        film = new Film();
        film.setTitle("Test Film");
        film.setLastUpdate(Timestamp.from(Instant.now()));
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