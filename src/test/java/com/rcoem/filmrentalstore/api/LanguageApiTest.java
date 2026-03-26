package com.rcoem.filmrentalstore.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
public class LanguageApiTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    void testGetAllLanguages() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/languages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.languages").exists());
    }
}