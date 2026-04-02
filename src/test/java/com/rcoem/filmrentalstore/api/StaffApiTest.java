package com.rcoem.filmrentalstore.api;

import com.rcoem.filmrentalstore.entities.*;
import com.rcoem.filmrentalstore.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // Ensures the database stays in its initial state after each test
public class StaffApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private AddressRepository addressRepository;

    private Staff testStaff;
    private Store testStore;
    private Address testAddress;

    @BeforeEach
    public void setup() {
        testAddress = addressRepository.findAll().get(0);
        testStore = storeRepository.findAll().get(0);

        // Create one fresh staff member for predictable testing
        testStaff = new Staff();
        testStaff.setFirstName("Test");
        testStaff.setLastName("User");
        testStaff.setUsername("testuser_");
        testStaff.setEmail("test@sakila.com");
        testStaff.setPassword("password");
        testStaff.setActive(true);
        testStaff.setAddress(testAddress);
        testStaff.setStore(testStore);

        testStaff = staffRepository.save(testStaff);
    }

    @Test
    @DisplayName("GET /staffs - Verify HATEOAS structure and projection")
    public void shouldReturnProjectedStaff() throws Exception {
        mockMvc.perform(get("/staffs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.staffs").exists())
                // Verify first name exists but sensitive password does not
                .andExpect(jsonPath("$._embedded.staffs[0].firstName").exists())
                .andExpect(jsonPath("$._embedded.staffs[0].password").doesNotExist());
    }

    @Test
    @DisplayName("POST /staffs - Create new staff using existing Sakila resources")
    public void testAddStaff() throws Exception {
        String addressUri = "/addresses/" + testAddress.getAddressId();
        String storeUri = "/stores/" + testStore.getStoreId();

        String newStaffJson = """
            {
                "firstName": "Robert",
                "lastName": "Martin",
                "username": "unclebob",
                "email": "bob@example.com",
                "password": "clean-code-rules",
                "active": true,
                "address": "%s",
                "store": "%s"
            }
            """.formatted(addressUri, storeUri);

        mockMvc.perform(post("/staffs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newStaffJson))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    @DisplayName("PATCH /staffs/{id} - Partial update")
    public void testUpdateStaff() throws Exception {
        String updateJson = """
            {
                "lastName": "Skywalker"
            }
            """;

        mockMvc.perform(patch("/staffs/" + testStaff.getStaffId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isNoContent());

        Staff updated = staffRepository.findById(testStaff.getStaffId()).orElseThrow();
        assertEquals("Skywalker", updated.getLastName());
    }

    @Test
    @DisplayName("DELETE /staffs/{id}")
    public void testDeleteStaff() throws Exception {
        // Use the staff we created in setup
        mockMvc.perform(delete("/staffs/" + testStaff.getStaffId()))
                .andExpect(status().isNoContent());

        assertFalse(staffRepository.existsById(testStaff.getStaffId()));
    }

    @Test
    @DisplayName("GET /staffs/{id}?projection=staffView")
    public void testGetStaffWithProjection() throws Exception {
        mockMvc.perform(get("/staffs/" + testStaff.getStaffId())
                        .param("projection", "staffView"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Test"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    @DisplayName("GET /staffs - Verify Default Pagination")
    public void shouldReturnDefaultPagination() throws Exception {
        // Spring Data REST default size is usually 20
        mockMvc.perform(get("/staffs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.size").exists())
                .andExpect(jsonPath("$.page.number").value(0));
    }
}