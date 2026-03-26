package com.rcoem.filmrentalstore.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FilmApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnAllFilms() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/films"))
                .andExpect(status().isOk());
    }
}
