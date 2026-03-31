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
        film.setRentalRate(BigDecimal.valueOf(4.99));
        film.setLength(148);
        film.setReplacementCost(BigDecimal.valueOf(19.99));
        film.setRating(Rating.PG_13);
        film.setSpecialFeatures(new HashSet<>());
        film.getSpecialFeatures().add(Set.BEHIND_THE_SCENES);
        film.setLanguage(savedLanguage);
        film.setLastUpdate(new Timestamp(System.currentTimeMillis()));
        Film savedFilm = filmRepository.save(film);
        savedFilmId = savedFilm.getFilmId();
    }


    @Test
    void shouldReturnAllFilms() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/films"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnFilmById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/films/" + savedFilmId))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnNotFoundForInvalidFilmId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/films/30000"))
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
                    "rentalRate": 4.99,
                    "length": 169,
                    "replacementCost": 19.99,
                    "rating": "PG",
                    "language": "http://localhost/languages/%d"
                }
                """.formatted(savedLanguageId);

        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(filmJson))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldPartiallyUpdateFilm() throws Exception {
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