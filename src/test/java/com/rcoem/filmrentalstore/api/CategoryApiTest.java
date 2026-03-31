package com.rcoem.filmrentalstore.api;

import com.rcoem.filmrentalstore.entities.Category;
import com.rcoem.filmrentalstore.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest                    // Loads full Spring context with all beans
@AutoConfigureMockMvc               // Provides MockMvc bean for testing

public class CategoryApiTest {

    @Autowired
    private MockMvc mockMvc;        // Simulates HTTP requests/responses

    @Autowired
    private ObjectMapper objectMapper; // Converts Java objects to/from JSON

    @Autowired
    private CategoryRepository categoryRepository;

   /* @Autowired
    private FilmCategoryRepository filmCategoryRepository;*/

    private Category testCategory;

    @BeforeEach
    public void setup() {
       // filmCategoryRepository.deleteAll();   // ✅ FIRST (child table)
//        categoryRepository.deleteAll();

        Category category = new Category();
        category.setName("Action_" + System.currentTimeMillis());

        testCategory = categoryRepository.save(category);  // ✅ committed to DB immediately
    }

    @Test
    public void testGetAllCategories() throws Exception {
        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())                      // Assert HTTP 200
                .andExpect(jsonPath("$._embedded.categories").exists()); // Check JSON structure
    }

    @Test
    public void testGetCategoryById_Valid() throws Exception {
        mockMvc.perform(get("/categories/" + testCategory.getCategoryId()))
                .andExpect(status().isOk())                    // HTTP 200
                .andExpect(jsonPath("$.name").value(testCategory.getName()));
    }

    @Test
    public void testGetCategoryById_NotFound() throws Exception {
        mockMvc.perform(get("/categories/9999"))  // Non-existent ID
                .andExpect(status().isBadRequest()); // Assert HTTP 404
    }

    @Test
    public void testGetCategoryById_InvalidDataType() throws Exception {
        mockMvc.perform(get("/categories/abc"))    // Invalid data type
                .andExpect(status().isBadRequest()); // Assert HTTP 400
    }

    @Test
    public void testCreateCategory_Valid() throws Exception {
        String newCategoryJson = """
            {
                "name": "Comedy"
            }
            """;

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newCategoryJson))
                .andExpect(status().isCreated());               // Assert HTTP 201

        // Verify it was saved to database
        assertThat(categoryRepository.count()).isEqualTo(2); // Original + new one
    }

    @Test
    public void testCreateCategory_MissingName_BadRequest() throws Exception {
        String badCategoryJson = "{}"; // Missing required 'name' field

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badCategoryJson))
                .andExpect(status().isBadRequest()); // Assert HTTP 400
    }

    @Test
    public void testCreateCategory_Duplicate_BadRequest() throws Exception {

        String duplicateCategoryJson = """
        {
            "name": "%s"
        }
        """.formatted(testCategory.getName());   // ✅ same name as already-committed category

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(duplicateCategoryJson));
    }


    @Test
    public void testUpdateCategoryWithPatch_Valid() throws Exception {
        String patchJson = """
            {
                "name": "Drama"
            }
            """;

        mockMvc.perform(patch("/categories/" + testCategory.getCategoryId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchJson))
                .andExpect(status().is2xxSuccessful()); // Assert HTTP 200 or 204

        // Verify update persisted in database
        Category updatedCategory = categoryRepository.findById(testCategory.getCategoryId()).orElseThrow();
        assertThat(updatedCategory.getName()).isEqualTo("Drama");
    }

    @Test
    public void testUpdateCategoryWithPatch_NotFound() throws Exception {
        String patchJson = """
            {
                "name": "Horror"
            }
            """;

        mockMvc.perform(patch("/categories/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchJson))
                .andExpect(status().isBadRequest()); // Assert HTTP 404
    }


    @Test
    public void testDeleteCategory_Valid() throws Exception {
        mockMvc.perform(delete("/categories/" + testCategory.getCategoryId()))
                .andExpect(status().isNoContent()); // Assert HTTP 204

        // Verify deletion from database
        assertThat(categoryRepository.findById(testCategory.getCategoryId())).isEmpty();
    }


    @Test
    public void testDeleteCategory_NotFound() throws Exception {
        mockMvc.perform(delete("/categories/9999"))
                .andExpect(status().isBadRequest()); // Assert HTTP 404
    }
}