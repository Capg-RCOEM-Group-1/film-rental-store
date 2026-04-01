package com.rcoem.filmrentalstore.api;

import com.rcoem.filmrentalstore.entities.*;
import com.rcoem.filmrentalstore.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // Added to keep tests isolated and clean
public class PaymentApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StoreRepository storeRepository;

    private Store store;

    @BeforeEach
    public void setup() {
        // Fetch an existing store from the Sakila DB to use for search queries
        store = storeRepository.findAll().get(0);
    }

    @Test
    @DisplayName("GET /payments/search/... - Verify search by Store ID")
    void testGetPaymentsByStore() throws Exception {
        mockMvc.perform(get("/payments/search/findPaymentsByStaff_Store_StoreId")
                        .param("storeId", store.getStoreId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.payments").exists())
                .andExpect(jsonPath("$._embedded.payments").isArray());
    }

    @Test
    @DisplayName("Verify paymentDate existence in search results")
    void testPaymentDateExists() throws Exception {
        mockMvc.perform(get("/payments/search/findPaymentsByStaff_Store_StoreId")
                        .param("storeId", store.getStoreId().toString()))
                .andExpect(status().isOk())
                // Verify the field exists in the first element of the embedded list
                .andExpect(jsonPath("$._embedded.payments[0].paymentDate").exists());
    }

    @Test
    @DisplayName("Verify staff relationship link or ID exists")
    void testPaymentStaffIdExists() throws Exception {
        mockMvc.perform(get("/payments/search/findPaymentsByStaff_Store_StoreId")
                        .param("storeId", store.getStoreId().toString()))
                .andExpect(status().isOk())
                /* Note: In Spring Data REST, nested objects are often represented as 
                   links unless using a specific projection. 
                   If PaymentView includes 'staff', check for staff object. 
                */
                .andExpect(jsonPath("$._embedded.payments[0]._links.staff").exists());
    }

    @Test
    @DisplayName("Verify amount format and existence")
    void testPaymentAmountExists() throws Exception {
        mockMvc.perform(get("/payments/search/findPaymentsByStaff_Store_StoreId")
                        .param("storeId", store.getStoreId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.payments[0].amount").exists())
                .andExpect(jsonPath("$._embedded.payments[0].amount").isNumber());
    }

    @Test
    @DisplayName("Verify invalid store ID returns empty embedded list")
    void testGetPaymentsByStore_InvalidStore() throws Exception {
        // Using a high ID that shouldn't exist
        mockMvc.perform(get("/payments/search/findPaymentsByStaff_Store_StoreId")
                        .param("storeId", "127")) 
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.payments").isEmpty());
    }

    @Test
    @DisplayName("Verify future date search returns empty results")
    void testGetPaymentsByStoreAndDate_FutureDate() throws Exception {
        mockMvc.perform(get("/payments/search/findPaymentsByStaff_Store_StoreIdAndPaymentDate")
                        .param("storeId", store.getStoreId().toString())
                        .param("paymentDate", "2099-01-01T00:00:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.payments").isEmpty());
    }

    @Test
    @DisplayName("GET /payments - Verify collection access and HATEOAS")
    void testGetAllPayments() throws Exception {
        mockMvc.perform(get("/payments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.payments").exists())
                .andExpect(jsonPath("$.page").exists()); // Check for pagination metadata
    }
}