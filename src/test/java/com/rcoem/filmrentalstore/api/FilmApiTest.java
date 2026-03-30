package com.rcoem.filmrentalstore.api;

import com.rcoem.filmrentalstore.entities.Film;
import com.rcoem.filmrentalstore.entities.Language;
import com.rcoem.filmrentalstore.enums.Rating;
import com.rcoem.filmrentalstore.enums.Set;
import com.rcoem.filmrentalstore.repository.FilmRepository;
import com.rcoem.filmrentalstore.repository.LanguageRepository;
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

@SpringBootTest
@AutoConfigureMockMvc
class FilmApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private LanguageRepository languageRepository;

    private Short savedFilmId;
    private Long savedLanguageId;

    @BeforeEach
    void setUp() {
        Language language = new Language();
        language.setName("English");
        Language savedLanguage = languageRepository.save(language);
        savedLanguageId = savedLanguage.getId();

        Film film = new Film();
        film.setTitle("Inception");
        film.setDescription("A mind-bending thriller");
        film.setReleaseYear(2010);
        film.setRentalDuration(7);
        film.setRentalRate(150.0);
        film.setLength(148);
        film.setReplacementCost(500.0);
        film.setRating(Rating.PG_13);
        film.setSpecialFeatures(Set.BEHIND_THE_SCENES);
        film.setLanguage(savedLanguage);
        Film savedFilm = filmRepository.save(film);
        savedFilmId = savedFilm.getFilmId();
    }

    @AfterEach
    void tearDown() {
        filmRepository.deleteAll();
        languageRepository.deleteAll();
    }

    @Test
    void shouldReturnAllFilms() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/films"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnFilmById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/films/" + savedFilmId))
                .andDo(print()) 
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnNotFoundForInvalidFilmId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/films/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateFilm() throws Exception {
        String filmJson = """
                {
                    "title": "Interstellar",
                    "description": "Space exploration",
                    "releaseYear": 2014,
                    "rentalDuration": 5,
                    "rentalRate": 120.0,
                    "length": 169,
                    "replacementCost": 400.0,
                    "rating": "PG",
                    "language": "/api/languages/%d"
                }
                """.formatted(savedLanguageId);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(filmJson))
                .andExpect(status().isCreated());
    }

@Test
void shouldPartiallyUpdateFilm() throws Exception {
    String patchJson = """
            {
                "title": "Inception Updated",
                "rentalRate": 200.0
            }
            """;

    mockMvc.perform(MockMvcRequestBuilders.patch("/api/films/" + savedFilmId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(patchJson))
            .andExpect(status().isNoContent());
}

    @Test
    void shouldSearchFilmByTitle() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/films/search/byTitle")
                .param("title", "Inception"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.films").isArray());
    }

    @Test
    void shouldReturnEmptyListForUnknownTitle() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/films/search/byTitle")
                .param("title", "xyzunknownfilm"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.films").isEmpty());
    }
}

