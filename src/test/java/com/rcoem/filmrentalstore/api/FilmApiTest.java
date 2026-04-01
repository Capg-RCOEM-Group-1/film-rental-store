package com.rcoem.filmrentalstore.api;

import com.rcoem.filmrentalstore.entities.Film;
import com.rcoem.filmrentalstore.entities.Language;
import com.rcoem.filmrentalstore.enums.Rating;
import com.rcoem.filmrentalstore.enums.Set;
import com.rcoem.filmrentalstore.repository.FilmRepository;
import com.rcoem.filmrentalstore.repository.LanguageRepository;

import jakarta.transaction.Transactional;

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

class FilmApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private LanguageRepository languageRepository;

    private Film savedFilm;
    private Language savedLanguage;

    @BeforeEach
    public void setUp() {
        // 1. Create and save Language
        Language language = new Language();
        language.setName("English");
        // Use saveAndFlush if using JpaRepository to ensure it's in the DB immediately
        savedLanguage = languageRepository.saveAndFlush(language);

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

        savedFilm = filmRepository.saveAndFlush(film);
    }

    @Test
    @Transactional
    void shouldReturnAllFilms() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/films"))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    void shouldReturnFilmById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/films/" + savedFilm.getFilmId()))
                .andDo(print()) 
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    void shouldReturnNotFoundForInvalidFilmId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/films/32767"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void shouldCreateFilm() throws Exception {
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
                    "language": "http://localhost/languages/%d"
                }
                """.formatted(savedLanguage.getId());

        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(filmJson))
                .andExpect(status().isCreated());
    }

@Test
@Transactional
void shouldPartiallyUpdateFilm() throws Exception {
        String patchJson = """
            {
                "title": "Inception Updated",
                "rentalRate": 4.99
            }
            """;

    mockMvc.perform(MockMvcRequestBuilders.patch("/films/" +savedFilm.getFilmId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(patchJson))
            .andExpect(status().isNoContent());
}

    @Test
    @Transactional
    void shouldSearchFilmByTitle() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/films/search/byTitle")
                .param("title", "ALABAMA DEVIL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.films").isArray());
    }

    @Test
    @Transactional
    void shouldReturnEmptyListForUnknownTitle() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/films/search/byTitle")
                .param("title", "xyzunknownfilm"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.films").isArray())
                .andExpect(jsonPath("$._embedded.films").isEmpty());
    }
}

