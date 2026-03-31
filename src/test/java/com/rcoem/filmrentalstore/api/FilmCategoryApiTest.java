package com.rcoem.filmrentalstore.api;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
<<<<<<< HEAD
import org.springframework.transaction.annotation.Transactional;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
=======
    MockMvc mockMvc;
>>>>>>> ameya

    @Test
    void shouldReturnFilmById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/films/search/byCategory?name=Action&size=5"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
