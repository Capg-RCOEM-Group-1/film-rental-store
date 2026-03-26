package com.rcoem.filmrentalstore.api;


import com.rcoem.filmrentalstore.entities.Customer;
import com.rcoem.filmrentalstore.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional //rolls back DB changes after each test
public class CustomerApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerRepository customerRepository;

    private Customer testCustomer;
    private Date testDate;
    private Timestamp testTimestamp;

    @BeforeEach
    public void setup() {
        customerRepository.deleteAll();
        testDate = Date.valueOf(LocalDate.now());
        testTimestamp = Timestamp.from(Instant.now());
        Customer customer = new Customer("Tom", "Hanks", "tom@email.com", 'Y', testDate, testTimestamp);
        testCustomer = customerRepository.save(customer);
    }


    //get all and get by id

    @Test
    public void testGetAllCustomers() throws Exception {
        mockMvc.perform(get("/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.customers").exists());
    }

    @Test
    public void testGetCustomerById_Valid() throws Exception {
        mockMvc.perform(get("/customers/" + testCustomer.getCustomerId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Tom"));
    }

    @Test
    public void testGetCustomerById_NotFound() throws Exception {
        mockMvc.perform(get("/customers/9999")) // Invalid ID
                .andExpect(status().isNotFound()); // Expect 404
    }

    @Test
    public void testGetCustomerById_InvalidDataType() throws Exception {
        mockMvc.perform(get("/customers/abc")) // Passing String to Long ID
                .andExpect(status().isBadRequest()); // Expect 400
    }


    //search endpoints

    @Test
    public void testSearchByFirstName_Valid() throws Exception {
        mockMvc.perform(get("/customers/search/findByFirstName")
                        .param("firstName", "Tom"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.customers[0].firstName").value("Tom"));
    }

    @Test
    public void testSearchByEmail_Valid() throws Exception {
        mockMvc.perform(get("/customers/search/findByEmail")
                        .param("email", "tom@email.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("tom@email.com"));
    }

    @Test
    public void testSearchByActive_Valid() throws Exception {
        mockMvc.perform(get("/customers/search/findByActive")
                        .param("active", "Y"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.customers").exists());
    }


    //POST

    @Test
    public void testCreateCustomer_Valid() throws Exception {
        String newCustomerJson = """
            {
                "firstName": "Jerry",
                "lastName": "Seinfeld",
                "email": "jerry@email.com",
                "active": "Y",
                "createDate": "%s",
                "timestamp": "%s"
            }
            """.formatted(testDate.toString(), testTimestamp.toString().replace(" ", "T"));

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newCustomerJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void testCreateCustomer_MissingFirstName_BadRequest() throws Exception {
        // Omitting firstName to trigger @NotNull constraint violation
        String badCustomerJson = """
            {
                "lastName": "Seinfeld",
                "email": "jerry@email.com",
                "active": "Y"
            }
            """;

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badCustomerJson))
                .andExpect(status().isBadRequest()); // Expect 400
    }


    //PATCH/PUT

    @Test
    public void testUpdateCustomerWithPatch_Valid() throws Exception {
        String patchJson = "{\"firstName\": \"UpdatedTom\"}";

        mockMvc.perform(patch("/customers/" + testCustomer.getCustomerId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchJson))
                .andExpect(status().is2xxSuccessful()); // 200 or 204

        // Verify update in DB
        Customer updatedCustomer = customerRepository.findById(testCustomer.getCustomerId()).orElseThrow();
        assertThat(updatedCustomer.getFirstName()).isEqualTo("UpdatedTom");
    }

    @Test
    public void testUpdateCustomerWithPatch_NotFound() throws Exception {
        String patchJson = "{\"firstName\": \"Ghost\"}";

        mockMvc.perform(patch("/customers/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchJson))
                .andExpect(status().isNotFound()); // Expect 404
    }


    //DELETE

    @Test
    public void testDeleteCustomer_Valid() throws Exception {
        mockMvc.perform(delete("/customers/" + testCustomer.getCustomerId()))
                .andExpect(status().isNoContent()); // Expect 204 No Content

        // Verify it is gone from DB
        assertThat(customerRepository.findById(testCustomer.getCustomerId())).isEmpty();
    }

    @Test
    public void testDeleteCustomer_NotFound() throws Exception {
        mockMvc.perform(delete("/customers/9999"))
                .andExpect(status().isNotFound()); // Expect 404
    }
}