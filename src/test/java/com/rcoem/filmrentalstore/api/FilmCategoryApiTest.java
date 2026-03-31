package com.rcoem.filmrentalstore.api;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class FilmCategoryApiTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldReturnFilmById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/films/search/byCategory?name=Action&size=5"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnFilmByName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/films/search/byCategory?name=Action&size=5"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}