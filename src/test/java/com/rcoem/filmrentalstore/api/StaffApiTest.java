package com.rcoem.filmrentalstore.api;

import com.rcoem.filmrentalstore.entities.Address;
import com.rcoem.filmrentalstore.entities.Staff;
import com.rcoem.filmrentalstore.entities.Store;
import com.rcoem.filmrentalstore.repository.AddressRepository;
import com.rcoem.filmrentalstore.repository.StaffRepository;
import com.rcoem.filmrentalstore.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class StaffApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private StoreRepository storeRepository;

    @BeforeEach
    public void setup() {
        // 1. Clear in correct order (child to parent to avoid FK violations)
        staffRepository.deleteAll();
        storeRepository.deleteAll();
        addressRepository.deleteAll();

        // 2. Setup Address
        Address address = new Address();
        address.setAddress("123 Spring St");
        address = addressRepository.save(address);

        // 3. Setup Store
        Store store = new Store();
        store.setAddress(address);
        store = storeRepository.save(store);

        // 4. Create Active Staff
        Staff activeStaff = new Staff();
        activeStaff.setFirstName("John");
        activeStaff.setLastName("Doe");
        activeStaff.setUsername("jdoe"); // Required for your identifier lookup
        activeStaff.setEmail("john.doe@test.com"); // Unique constraint
        activeStaff.setPassword("pass123");
        activeStaff.setActive(true);
        activeStaff.setAddress(address);
        activeStaff.setStore(store);
        // Optional: Blob picture could be null or empty byte array
        activeStaff.setPicture(null);

        // 5. Create Inactive Staff
        Staff inactiveStaff = new Staff();
        inactiveStaff.setFirstName("Jane");
        inactiveStaff.setLastName("Smith");
        inactiveStaff.setUsername("jsmith"); // Required for your identifier lookup
        inactiveStaff.setEmail("jane.smith@test.com"); // Unique constraint
        inactiveStaff.setPassword("pass456");
        inactiveStaff.setActive(false);
        inactiveStaff.setAddress(address);
        inactiveStaff.setStore(store);

        // 6. Persist
        staffRepository.saveAll(List.of(activeStaff, inactiveStaff));
    }

    @Test
    @DisplayName("GET /staffs with projection filters fields")
    public void shouldReturnProjectedStaff() throws Exception {
        // Given: An active staff member exists

        mockMvc.perform(get("/staffs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.staffs[0].firstName").value("John"))
                .andExpect(jsonPath("$._embedded.staffs[0].lastName").value("Doe"))
                // Verify sensitive/excluded fields are ABSENT
                .andExpect(jsonPath("$._embedded.staffs[0].password").doesNotExist())
                .andExpect(jsonPath("$._embedded.staffs[0].active").doesNotExist());
    }

    @Test
    void testGetAllStaff() throws Exception {
        mockMvc.perform(get("/staffs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.staffs").exists());
    }

    @Test
    @DisplayName("GET /staffs/search/findByActiveTrue - No records found")
    public void shouldReturnEmptyListWhenNoActiveStaff() throws Exception {
        // Given: Ensure no active staff exist
        staffRepository.deleteAll();

        mockMvc.perform(get("/staffs/search/findByActiveTrue"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.staffs").isArray())
                .andExpect(jsonPath("$._embedded.staffs").isEmpty());
    }

    @Test
    @DisplayName("GET /staffs - Fallback when invalid projection is provided")
    public void shouldFallbackWhenProjectionIsInvalid() throws Exception {
        mockMvc.perform(get("/staffs?projection=nonExistentView"))
                .andExpect(status().isOk())
                // It should still return data, likely the full entity
                .andExpect(jsonPath("$._embedded.staffs").exists());
    }

    @Test
    @DisplayName("GET /staffs")
    public void shouldReturnSortedProjectedStaff() throws Exception {
        mockMvc.perform(get("/staffs")
                        .param("sort", "firstName,desc")
                        )
                .andExpect(status().isOk())
                // With "John" and "Jane", DESC sort should put "John" first
                .andExpect(jsonPath("$._embedded.staffs[0].firstName").value("John"))
                .andExpect(jsonPath("$._embedded.staffs[1].firstName").value("Jane"));
    }

    @Test
    @DisplayName("GET /staffs/search/findByActiveFalse")
    public void shouldApplyProjectionToSearchMethod() throws Exception {
        mockMvc.perform(get("/staffs/search/findByActiveFalse"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.staffs[0].firstName").value("Jane"))
                .andExpect(jsonPath("$._embedded.staffs[0].active").doesNotExist()); // 'active' is not in staffView
    }
    @Test
    public void testAddStaff() throws Exception {
        // Get URIs for the required dependencies created in @BeforeEach
        String addressUri = "/addresses/" + addressRepository.findAll().get(0).getAddressId();
        String storeUri = "/stores/" + storeRepository.findAll().get(0).getStoreId();

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

        String location = mockMvc.perform(post("/staffs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newStaffJson))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn().getResponse().getHeader("Location");

        // Now GET the resource from the location returned
        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Robert"));
    }

    @Test
    public void testUpdateStaff() throws Exception {
        // Get the first staff member from the DB
        Staff existingStaff = staffRepository.findAll().get(0);

        // We use PATCH for partial updates (just changing the email and lastName)
        String updateJson = """
            {
                "lastName": "Updated-Name",
                "email": "updated.email@test.com"
            }
            """;

        mockMvc.perform(patch("/staffs/" + existingStaff.getStaffId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isNoContent()); // Data REST returns 204 on successful patch

        // Verify the change in the database
        Staff updated = staffRepository.findById(existingStaff.getStaffId()).get();
        assertEquals("Updated-Name", updated.getLastName());
        assertEquals("updated.email@test.com", updated.getEmail());
    }

    @Test
    public void testDeleteStaff() throws Exception {
        Staff staffToDelete = staffRepository.findAll().get(0);
        Byte id = staffToDelete.getStaffId();

        mockMvc.perform(delete("/staffs/" + id))
                .andExpect(status().isNoContent());

        // Verify the entity is gone
        assertFalse(staffRepository.existsById(id));
    }

    @Test
    public void testGetStaffWithProjection() throws Exception {
        Staff staff = staffRepository.findAll().get(0);

        mockMvc.perform(get("/staffs/" + staff.getStaffId())
                        .param("projection", "staffView"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.firstName").exists())
                .andExpect(jsonPath("$.username").exists())
                // Password should NOT be in the projection
                .andExpect(jsonPath("$.password").doesNotExist());
    }

}
