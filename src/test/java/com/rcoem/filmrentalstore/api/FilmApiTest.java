package com.rcoem.filmrentalstore.api;

import com.rcoem.filmrentalstore.entities.Film;
import com.rcoem.filmrentalstore.entities.Language;
import com.rcoem.filmrentalstore.enums.Rating;
import com.rcoem.filmrentalstore.enums.Set;
import com.rcoem.filmrentalstore.repository.FilmRepository;
import com.rcoem.filmrentalstore.repository.LanguageRepository;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class FilmApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private LanguageRepository languageRepository;

    private Short savedFilmId;
    private Byte savedLanguageId;


    private void setUp() {
        // 1. Create and save Language
        Language language = new Language();
        language.setName("English");
        // Use saveAndFlush if using JpaRepository to ensure it's in the DB immediately
        Language savedLanguage = languageRepository.saveAndFlush(language);
        savedLanguageId = savedLanguage.getId();

        // 2. Create and save Film
        Film film = new Film();
        film.setTitle("Inception");
        film.setDescription("A mind-bending thriller");
        film.setReleaseYear(2010);
        film.setRentalDuration(7);
        film.setRentalRate(BigDecimal.valueOf(4.99));
        film.setLength(148);
        film.setReplacementCost(BigDecimal.valueOf(19.99));
        film.setRating(Rating.PG_13);

        // Ensure the Set is initialized
        HashSet<Set> features = new HashSet<>();
        features.add(Set.BEHIND_THE_SCENES);
        film.setSpecialFeatures(features);

        film.setLanguage(savedLanguage);
        film.setLastUpdate(new Timestamp(System.currentTimeMillis()));

        Film savedFilm = filmRepository.saveAndFlush(film);
        savedFilmId = savedFilm.getFilmId();
    }

    @Test
    void shouldReturnAllFilms() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/films"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnFilmById() throws Exception {
        setUp();
        mockMvc.perform(MockMvcRequestBuilders.get("/films/" + savedFilmId))
                .andDo(print()) 
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnNotFoundForInvalidFilmId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/films/32767"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateFilm() throws Exception {
        setUp();
        String filmJson = """
                {
                    "title": "Interstellar",
                    "description": "Space exploration",
                    "releaseYear": 2014,
                    "rentalDuration": 5,
                    "rentalRate": 4.99,
                    "length": 169,
                    "replacementCost": 19.99,
                    "rating": "PG",
                    "language": "/api/languages/%d"
                }
                """.formatted(savedLanguageId);

        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(filmJson))
                .andExpect(status().isCreated());
    }

@Test
void shouldPartiallyUpdateFilm() throws Exception {
    setUp();
        String patchJson = """
            {
                "title": "Inception Updated",
                "rentalRate": 4.99
            }
            """;

    mockMvc.perform(MockMvcRequestBuilders.patch("/films/" + savedFilmId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(patchJson))
            .andExpect(status().isNoContent());
}

    @Test
    void shouldSearchFilmByTitle() throws Exception {
        setUp();
        mockMvc.perform(MockMvcRequestBuilders.get("/films/search/byTitle")
                .param("title", "Inception"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.films").isArray());
    }

    @Test
    void shouldReturnEmptyListForUnknownTitle() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/films/search/byTitle")
                .param("title", "xyzunknownfilm"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.films").isArray())
                .andExpect(jsonPath("$._embedded.films").isEmpty());
    }
}

