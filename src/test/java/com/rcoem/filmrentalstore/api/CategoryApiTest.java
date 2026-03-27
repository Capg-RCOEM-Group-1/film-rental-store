package com.rcoem.filmrentalstore.api;

import com.rcoem.filmrentalstore.entities.Category;
import com.rcoem.filmrentalstore.repository.CategoryRepository;
import com.rcoem.filmrentalstore.repository.FilmCategoryRepository;
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

/**
 * API Tests for Category endpoint
 * Tests HTTP REST endpoints: GET, POST, PATCH, DELETE
 * Uses MockMvc to simulate HTTP requests without starting a real server
 */
@SpringBootTest                    // Loads full Spring context with all beans
@AutoConfigureMockMvc               // Provides MockMvc bean for testing
// ✅ REMOVED @Transactional — it prevented the unique constraint from firing
// in testCreateCategory_Duplicate_BadRequest because @BeforeEach data was
// never flushed/committed to the DB before MockMvc's POST ran.
// Cleanup is handled manually via deleteAll() in @BeforeEach instead.
public class CategoryApiTest {

    @Autowired
    private MockMvc mockMvc;        // Simulates HTTP requests/responses

    @Autowired
    private ObjectMapper objectMapper; // Converts Java objects to/from JSON

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FilmCategoryRepository filmCategoryRepository;

    private Category testCategory;

    /**
     * Setup method - runs BEFORE each @Test method
     * Creates fresh test data for each test.
     * deleteAll() here replaces the rollback that @Transactional used to provide.
     */
    @BeforeEach
    public void setup() {
        filmCategoryRepository.deleteAll();   // ✅ FIRST (child table)
        categoryRepository.deleteAll();

        Category category = new Category();
        category.setName("Action_" + System.currentTimeMillis());

        testCategory = categoryRepository.save(category);  // ✅ committed to DB immediately
    }


    // ==================== GET ALL CATEGORIES ====================

    /**
     * Test: Get all categories
     * Expected: HTTP 200 OK with list of categories embedded in response
     *
     * MockMvc Methods:
     * - get("/categories") → Creates GET request
     * - .andExpect(status().isOk()) → Asserts HTTP 200 response
     * - jsonPath("$._embedded.categories") → Checks if embedded categories exist in JSON
     *   (Spring Data REST wraps collections in _embedded)
     */
    @Test
    public void testGetAllCategories() throws Exception {
        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())                      // Assert HTTP 200
                .andExpect(jsonPath("$._embedded.categories").exists()); // Check JSON structure
    }


    // ==================== GET BY ID ====================

    /**
     * Test: Get category by valid ID
     * Expected: HTTP 200 OK with category details
     */
    @Test
    public void testGetCategoryById_Valid() throws Exception {
        mockMvc.perform(get("/categories/" + testCategory.getCategoryId()))
                .andExpect(status().isOk())                    // HTTP 200
                .andExpect(jsonPath("$.name").value(testCategory.getName()));
    }

    /**
     * Test: Get category with non-existent ID
     * Expected: HTTP 404 Not Found
     */
    @Test
    public void testGetCategoryById_NotFound() throws Exception {
        mockMvc.perform(get("/categories/9999"))  // Non-existent ID
                .andExpect(status().isNotFound()); // Assert HTTP 404
    }

    /**
     * Test: Get category with invalid data type for ID
     * Expected: HTTP 400 Bad Request
     */
    @Test
    public void testGetCategoryById_InvalidDataType() throws Exception {
        mockMvc.perform(get("/categories/abc"))    // Invalid data type
                .andExpect(status().isBadRequest()); // Assert HTTP 400
    }


    // ==================== CREATE (POST) ====================

    /**
     * Test: Create category with valid data
     * Expected: HTTP 201 Created with new category in response
     */
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

    /**
     * Test: Create category with null/empty name
     * Expected: HTTP 400 Bad Request (validation error)
     */
    @Test
    public void testCreateCategory_MissingName_BadRequest() throws Exception {
        String badCategoryJson = "{}"; // Missing required 'name' field

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badCategoryJson))
                .andExpect(status().isBadRequest()); // Assert HTTP 400
    }

    /**
     * Test: Create duplicate category
     * Expected: HTTP 409 Conflict
     *
     * ✅ This works correctly only WITHOUT @Transactional on the class.
     * Reason: @BeforeEach saves testCategory and commits it to the DB.
     * When this POST runs, the DB sees the existing name and throws
     * DataIntegrityViolationException → GlobalExceptionHandler returns 409.
     */
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


    // ==================== UPDATE (PATCH) ====================

    /**
     * Test: Update category with PATCH (partial update)
     * Expected: HTTP 200 OK or 204 No Content
     */
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

    /**
     * Test: Update non-existent category
     * Expected: HTTP 404 Not Found
     */
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
                .andExpect(status().isNotFound()); // Assert HTTP 404
    }


    // ==================== DELETE ====================

    /**
     * Test: Delete category with valid ID
     * Expected: HTTP 204 No Content
     */
    @Test
    public void testDeleteCategory_Valid() throws Exception {
        mockMvc.perform(delete("/categories/" + testCategory.getCategoryId()))
                .andExpect(status().isNoContent()); // Assert HTTP 204

        // Verify deletion from database
        assertThat(categoryRepository.findById(testCategory.getCategoryId())).isEmpty();
    }

    /**
     * Test: Delete non-existent category
     * Expected: HTTP 404 Not Found
     */
    @Test
    public void testDeleteCategory_NotFound() throws Exception {
        mockMvc.perform(delete("/categories/9999"))
                .andExpect(status().isNotFound()); // Assert HTTP 404
    }
}