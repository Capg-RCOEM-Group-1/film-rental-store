package com.rcoem.filmrentalstore.api;

import com.rcoem.filmrentalstore.entities.Film;

import com.rcoem.filmrentalstore.entities.Language;
import com.rcoem.filmrentalstore.enums.Rating;
import com.rcoem.filmrentalstore.enums.Set;

import com.rcoem.filmrentalstore.repository.FilmRepository;
import com.rcoem.filmrentalstore.repository.LanguageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LanguageFilmApiTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    LanguageRepository languageRepo;
   /* @Autowired
    FilmCategoryRepository filmCategoryRepo;*/
    @Autowired
    FilmRepository filmRepo;
    private Film film;
    private Language lang;
    @BeforeEach
    void setup(){
        //filmCategoryRepo.deleteAll();
        filmRepo.deleteAll();
        languageRepo.deleteAll();

        lang = new Language();
        lang.setName("English");
        languageRepo.save(lang);
        film = new Film("Top Gun","*",2004,2, BigDecimal.valueOf(50.00),3,BigDecimal.valueOf(300.00), Rating.PG_13, new HashSet<>(),lang,lang, Timestamp.valueOf(LocalDateTime.now()));
        filmRepo.save(film);
    }

    @Test
    public void testGetLanguageFilms() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/films/search/findByLanguage_Id?id="+lang.getId()+"&size=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.films").exists());
    }
}
