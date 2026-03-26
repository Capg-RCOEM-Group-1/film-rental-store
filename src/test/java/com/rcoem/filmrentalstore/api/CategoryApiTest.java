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
import org.springframework.transaction.annotation.Transactional;
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
@Transactional                      // Rolls back DB changes after each test
public class CategoryApiTest {

    @Autowired
    private MockMvc mockMvc;        // Simulates HTTP requests/responses

    @Autowired
    private ObjectMapper objectMapper; // Converts Java objects to/from JSON

    @Autowired
    private CategoryRepository categoryRepository;

    private Category testCategory;

    /**
     * Setup method - runs BEFORE each @Test method
     * Creates fresh test data for each test
     */
    @BeforeEach
    public void setup() {
        categoryRepository.deleteAll(); // Clean slate for each test
        Category category = new Category();
        category.setName("Action");
        testCategory = categoryRepository.save(category);
    }


    // ==================== GET ALL CATEGORIES ====================

    /**
     * Test: Get all categories
     * Expected: HTTP 200 OK with list of categories embedded in response

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

     * Explanation:
     * - jsonPath("$.categoryId") → Navigates to categoryId field in JSON
     * - .value(testCategory.getCategoryId()) → Asserts the value matches
     */
    @Test
    public void testGetCategoryById_Valid() throws Exception {
        mockMvc.perform(get("/categories/" + testCategory.getCategoryId()))
                .andExpect(status().isOk())                    // HTTP 200
                .andExpect(jsonPath("$.name").value("Action")); // Verify name is "Action"
    }

    /**
     * Test: Get category with non-existent ID
     * Expected: HTTP 404 Not Found

     * Explanation:
     * - Using ID 9999 which doesn't exist in database
     * - status().isNotFound() asserts HTTP 404 response
     */
    @Test
    public void testGetCategoryById_NotFound() throws Exception {
        mockMvc.perform(get("/categories/9999"))  // Non-existent ID
                .andExpect(status().isNotFound()); // Assert HTTP 404
    }

    /**
     * Test: Get category with invalid data type for ID
     * Expected: HTTP 400 Bad Request

     * Explanation:
     * - Passing "abc" (String) instead of numeric ID
     * - Spring's type conversion fails → 400 Bad Request
     * - status().isBadRequest() asserts HTTP 400
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

     * MockMvc Methods:
     * - post("/categories") → Creates POST request
     * - .contentType(MediaType.APPLICATION_JSON) → Sets Content-Type header
     * - .content(categoryJson) → Sets request body as JSON string
     * - status().isCreated() → Asserts HTTP 201 response
     */
    @Test
    public void testCreateCategory_Valid() throws Exception {
        String newCategoryJson = """
            {
                "name": "Comedy"
            }
            """;

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON) // Header: application/json
                        .content(newCategoryJson))                // Body: JSON string
                .andExpect(status().isCreated());               // Assert HTTP 201
                 // Verify returned name

        // Verify it was saved to database
        assertThat(categoryRepository.count()).isEqualTo(2); // Original + new one
    }

    /**
     * Test: Create category with null/empty name
     * Expected: HTTP 400 Bad Request (validation error)

     * Explanation:
     * - Sending empty JSON object (no name field)
     * - Database constraint or validation annotation rejects it
     * - assertTrue that error status is returned
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
     * Test: Create duplicate category (if unique constraint exists)
     * Expected: HTTP 400 Bad Request or 409 Conflict

     * Note: Only works if you add @Column(unique = true) to Category.name
     */
    @Test
    public void testCreateCategory_Duplicate_BadRequest() throws Exception {
        String duplicateCategoryJson = """
            {
                "name": "Action"
            }
            """;

        // Try to create another "Action" category (testCategory already has "Action")
        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(duplicateCategoryJson))
                .andExpect(status().isConflict()); // Assert HTTP 409
    }


    // ==================== UPDATE (PATCH) ====================

    /**
     * Test: Update category with PATCH (partial update)
     * Expected: HTTP 200 OK or 204 No Content

     * MockMvc Methods:
     * - patch("/categories/{id}") → Creates PATCH request
     * - Assertions after the request verify the update in database

     * Explanation:
     * - assertThat(updatedCategory.getName()).isEqualTo("Drama")
     *   verifies the change was persisted
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
     * Expected: HTTP 204 No Content (successful deletion, no content to return)

     * Explanation:
     * - status().isNoContent() asserts HTTP 204
     * - categoryRepository.findById() verifies record is gone from DB
     * - .isEmpty() confirms Optional is empty
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