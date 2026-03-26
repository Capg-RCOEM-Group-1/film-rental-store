package com.rcoem.filmrentalstore.api;

import com.rcoem.filmrentalstore.repository.LanguageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
public class LanguageApiTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    LanguageRepository languageRepo;
    @BeforeEach
    public void clean(){
        languageRepo.deleteAll();
    }

    @Test
    void testGetAllLanguages() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/languages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.languages").exists());
    }

    @Test
    void testCreateLanguage() throws Exception {
        String json = """
        {
            "name": "Spanish"
        }
    """;
        mockMvc.perform(post("/languages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }
}