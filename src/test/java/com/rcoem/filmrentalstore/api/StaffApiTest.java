package com.rcoem.filmrentalstore.api;

import com.rcoem.filmrentalstore.entities.Staff;
import com.rcoem.filmrentalstore.repository.StaffRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class StaffApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StaffRepository staffRepository;

    @BeforeEach
    public void setup() {
        staffRepository.deleteAll();
        // Given: An active staff member exists
        Staff activeStaff = new Staff();
        activeStaff.setFirstName("John");
        activeStaff.setLastName("Doe");
        activeStaff.setActive(true);
        activeStaff.setPassword("pass123");
        // Given: An not active staff member exists
        Staff inactiveStaff = new Staff();
        inactiveStaff.setFirstName("Jane");
        inactiveStaff.setLastName("Smith");
        inactiveStaff.setActive(false);
        inactiveStaff.setPassword("pass456");

        staffRepository.saveAll(List.of(activeStaff, inactiveStaff));
    }

    @Test
    @DisplayName("GET /staffs with projection filters fields")
    public void shouldReturnProjectedStaff() throws Exception {
        // Given: An active staff member exists

        mockMvc.perform(get("/staffs")
                        .param("projection", "staffView"))
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
    @DisplayName("GET /staffs?sort=firstName,desc&projection=staffView")
    public void shouldReturnSortedProjectedStaff() throws Exception {
        mockMvc.perform(get("/staffs")
                        .param("sort", "firstName,desc")
                        .param("projection", "staffView"))
                .andExpect(status().isOk())
                // With "John" and "Jane", DESC sort should put "John" first
                .andExpect(jsonPath("$._embedded.staffs[0].firstName").value("John"))
                .andExpect(jsonPath("$._embedded.staffs[1].firstName").value("Jane"));
    }

    @Test
    @DisplayName("GET /staffs/search/findByActiveFalse?projection=staffView")
    public void shouldApplyProjectionToSearchMethod() throws Exception {
        mockMvc.perform(get("/staffs/search/findByActiveFalse?projection=staffView"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.staffs[0].firstName").value("Jane"))
                .andExpect(jsonPath("$._embedded.staffs[0].active").doesNotExist()); // 'active' is not in staffView
    }

}
