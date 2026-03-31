package com.rcoem.filmrentalstore.api;

import com.rcoem.filmrentalstore.entities.Address;
import com.rcoem.filmrentalstore.entities.Customer;
import com.rcoem.filmrentalstore.entities.Store;
import com.rcoem.filmrentalstore.repository.AddressRepository;
import com.rcoem.filmrentalstore.repository.CustomerRepository;
import com.rcoem.filmrentalstore.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.sql.Timestamp;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // rolls back DB changes after each test
public class CustomerApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private StoreRepository storeRepository;

    private Customer testCustomer;
    private Timestamp testTimestamp;
    private Address address;
    private Store store;

    @BeforeEach
    public void setup() {
        testTimestamp = Timestamp.from(Instant.now());
        address = addressRepository.findById((short) 5L).orElse(null);
        store = storeRepository.findById((byte) 1L).orElse(null);
        Customer customer = new Customer("Tom", "Hanks", "tom@email.com", store, address);

        // FIX 1: Use saveAndFlush so custom repository queries (like findByFirstName)
        // can see the newly created record in the database immediately.
        testCustomer = customerRepository.saveAndFlush(customer);
    }

    // get all and get by id

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

    // search endpoints

    @Test
    public void testSearchByFirstNameIgnoreCase_Valid() throws Exception {
        mockMvc.perform(get("/customers/search/findByFirstNameIgnoreCase")
                        .param("firstName", "MARY")
                        // Explicitly ask for the projection
                        .param("projection", "customerDetailsProjection"))
                .andExpect(status().isOk())
                // Now "name" will exist in the JSON
                .andExpect(jsonPath("$._embedded.customers[0].name").value("MARY SMITH"));
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
                        .param("active", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.customers").exists());
    }

    // POST

    @Test
    public void testCreateCustomer_Valid() throws Exception {
        // FIX 2: Added store and address resource URIs to fulfill database constraints.
        // Without these, Spring Data REST returns a 409 Conflict.
        String newCustomerJson = """
            {
                "firstName": "Jerry",
                "lastName": "Seinfeld",
                "email": "jerry@email.com",
                "store": "/stores/1",
                "address": "/addresses/5"
            }
            """;

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newCustomerJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void testCreateCustomer_MissingFirstName_BadRequest() throws Exception {
        // Included store and address here as well so the test fails for the RIGHT reason
        // (missing firstName) rather than missing associations.
        String badCustomerJson = """
            {
                "lastName": "Seinfeld",
                "email": "jerry@email.com",
                "store": "/stores/1",
                "address": "/addresses/5"
            }
            """;

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badCustomerJson))
                .andExpect(status().isBadRequest()); // Expect 400
    }

    // PATCH/PUT

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

    // DELETE

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