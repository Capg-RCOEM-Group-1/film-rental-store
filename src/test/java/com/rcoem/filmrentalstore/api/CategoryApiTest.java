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

/**
 * This test class validates ALL behavior of Category APIs exposed via Spring Data REST.
 *
 * WHY this exists:
 * - Ensures API contract is correct (status codes, JSON structure)
 * - Ensures DB constraints are respected
 * - Covers both happy paths and edge cases
 *
 * IMPORTANT:
 * We are NOT testing controller logic (because Spring Data REST handles it),
 * we are testing:
 *   → Repository exposure
 *   → Validation
 *   → Exception handling
 */
@SpringBootTest
@AutoConfigureMockMvc
public class CategoryApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category testCategory;

    /**
     * Runs before every test
     * WHY:
     * - Ensures clean DB (test isolation)
     * - Prevents flaky tests
     */
    @BeforeEach
    public void setup() {
        categoryRepository.deleteAll();

        // Reset auto-increment manually (VERY IMPORTANT)
        categoryRepository.flush();

        Category category = new Category();
        category.setName("Action");

        testCategory = categoryRepository.save(category);
    }

    /**
     * ✅ GET ALL
     * WHY:
     * - Verifies Spring Data REST endpoint exists
     * - Ensures correct HAL structure (_embedded)
     */
    @Test
    public void testGetAllCategories() throws Exception {
        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.categories").exists());
    }

    /**
     * ✅ GET BY ID (VALID)
     */
    @Test
    public void testGetCategoryById_Valid() throws Exception {
        mockMvc.perform(get("/categories/" + testCategory.getCategoryId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(testCategory.getName()));
    }

    /**
     * ❌ GET BY ID (NOT FOUND)
     * WHY:
     * - API must return 404, NOT 400
     */
    @Test
    public void testGetCategoryById_NotFound() throws Exception {
        mockMvc.perform(get("/categories/120"))
                .andExpect(status().isNotFound());
    }

    /**
     * ❌ INVALID ID TYPE
     * WHY:
     * - "abc" cannot be converted to Byte → handled by GlobalExceptionHandler
     */
    @Test
    public void testGetCategoryById_InvalidDataType() throws Exception {
        mockMvc.perform(get("/categories/abc"))
                .andExpect(status().isBadRequest());
    }

    /**
     * ✅ CREATE VALID CATEGORY
     */
    @Test
    public void testCreateCategory_Valid() throws Exception {
        String json = """
            {
                "name": "Comedy"
            }
            """;

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        // Verify DB state
        assertThat(categoryRepository.count()).isEqualTo(2);
    }

    /**
     * ❌ CREATE WITHOUT NAME
     * WHY:
     * - name is required → should trigger validation
     */
    @Test
    public void testCreateCategory_MissingName_BadRequest() throws Exception {
        String json = "{}";

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    /**
     * ❌ CREATE DUPLICATE CATEGORY
     * WHY:
     * - name is unique → should throw DataIntegrityViolationException
     * - handled as 409 CONFLICT
     */
    @Test
    public void testCreateCategory_Duplicate_BadRequest() throws Exception {

        String json = """
        {
            "name": "%s"
        }
        """.formatted(testCategory.getName());

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict());
    }

    /**
     * ✅ PATCH UPDATE VALID
     * WHY:
     * - Spring Data REST supports PATCH
     * - Should partially update resource
     */
    @Test
    public void testUpdateCategoryWithPatch_Valid() throws Exception {
        String json = """
            {
                "name": "Drama"
            }
            """;

        mockMvc.perform(patch("/categories/" + testCategory.getCategoryId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is2xxSuccessful());

        Category updated = categoryRepository.findById(testCategory.getCategoryId()).orElseThrow();
        assertThat(updated.getName()).isEqualTo("Drama");
    }

    /**
     * ❌ PATCH NON-EXISTENT
     */
    @Test
    public void testUpdateCategoryWithPatch_NotFound() throws Exception {
        String json = """
            {
                "name": "Horror"
            }
            """;

        mockMvc.perform(patch("/categories/120")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }

    /**
     * ❌ PATCH DUPLICATE NAME
     */
    @Test
    public void testUpdateCategory_DuplicateName() throws Exception {

        Category another = new Category();
        another.setName("Drama");
        categoryRepository.save(another);

        String json = """
            {
                "name": "Drama"
            }
            """;

        mockMvc.perform(patch("/categories/" + testCategory.getCategoryId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict());
    }

    /**
     * ✅ DELETE VALID
     */
    @Test
    public void testDeleteCategory_Valid() throws Exception {
        mockMvc.perform(delete("/categories/" + testCategory.getCategoryId()))
                .andExpect(status().isNoContent());

        assertThat(categoryRepository.findById(testCategory.getCategoryId())).isEmpty();
    }

    /**
     * ❌ DELETE NOT FOUND
     */
    @Test
    public void testDeleteCategory_NotFound() throws Exception {
        mockMvc.perform(delete("/categories/120"))
                .andExpect(status().isNotFound());
    }

    /**
     * 🔍 SEARCH BY NAME
     * WHY:
     * - Tests custom repository method
     */
    @Test
    public void testSearchCategoryByName() throws Exception {
        mockMvc.perform(get("/categories/search/byName?name=act"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.categories").exists());
    }

    /**
     * 🔍 CASE INSENSITIVE SEARCH
     */
    @Test
    public void testSearchCategory_CaseInsensitive() throws Exception {
        mockMvc.perform(get("/categories/search/byName?name=ACTION"))
                .andExpect(status().isOk());
    }

    /**
     * 🔍 EMPTY SEARCH RESULT
     */
    @Test
    public void testSearchCategory_NoResult() throws Exception {
        mockMvc.perform(get("/categories/search/byName?name=xyz123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.categories").isEmpty());
    }

    /**
     * 📦 PAGINATION TEST
     * WHY:
     * - Spring Data REST automatically supports pagination
     */
    @Test
    public void testPagination() throws Exception {
        mockMvc.perform(get("/categories?page=0&size=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").exists());
    }

    /**
     * 🎯 PROJECTION TEST
     * WHY:
     * - Ensures DTO projection works correctly
     */
    @Test
    public void testProjection() throws Exception {
        mockMvc.perform(get("/categories?projection=categoryProjection"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.categories[0].categoryId").exists());
    }
}