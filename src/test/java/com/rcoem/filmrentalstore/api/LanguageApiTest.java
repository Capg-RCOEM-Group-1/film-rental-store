package com.rcoem.filmrentalstore.api;

import com.rcoem.filmrentalstore.repository.FilmRepository;
import com.rcoem.filmrentalstore.repository.LanguageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class LanguageApiTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    LanguageRepository languageRepo;

    @Autowired
    FilmRepository filmRepo;

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
            "name": "Roman"
        }
    """;
        mockMvc.perform(post("/languages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateNullLanguage() throws Exception {
        mockMvc.perform(post("/languages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateDuplicateLanguage() throws Exception {
        String json = """
        {
            "name": "Spanish"
        }
    """;
        mockMvc.perform(post("/languages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        mockMvc.perform(post("/languages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isConflict());
    }

    @Test
    void testUpdateLanguage() throws Exception {
        String createJson = """
        { "name": "Java" }
    """;

        String response = mockMvc.perform(post("/languages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andReturn().getResponse().getHeader("Location");
        Long id = Long.parseLong(
                response.substring(response.lastIndexOf("/") + 1)
        );
        String updateJson = """
        { "name": "Java Updated" }
    """;

        mockMvc.perform(MockMvcRequestBuilders.put("/languages/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testUpdateNullLanguage() throws Exception{
        String createJson = """
        { "name": "Java" }
    """;

        String response = mockMvc.perform(post("/languages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andReturn().getResponse().getHeader("Location");
        Long id = Long.parseLong(
                response.substring(response.lastIndexOf("/") + 1)
        );
        mockMvc.perform(MockMvcRequestBuilders.put("/languages/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateInvalidIdLanguage() throws Exception{
        String createJson = """
        { "name": "Java" }
    """;
        mockMvc.perform(MockMvcRequestBuilders.put("/languages/" + 999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isBadRequest());
    }
}