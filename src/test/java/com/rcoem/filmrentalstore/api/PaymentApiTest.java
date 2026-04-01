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

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PaymentApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    private Store store;
    private Payment testPayment;

    @BeforeEach
    public void setup() {
        // 1. Safely fetch OR create a Store
        store = storeRepository.findAll().stream().findFirst().orElseGet(() -> {
            Store newStore = new Store();
            // Add any @NotNull fields your Store entity requires here
            return storeRepository.saveAndFlush(newStore);
        });

        // 2. Safely fetch OR create a Staff
        Staff staff = staffRepository.findAll().stream().findFirst().orElseGet(() -> {
            Staff newStaff = new Staff();
            newStaff.setFirstName("Test");
            newStaff.setLastName("Staff");
            newStaff.setStore(store); // Link to store
            return staffRepository.saveAndFlush(newStaff);
        });

        // 3. Safely fetch OR create a Customer
        Customer customer = customerRepository.findAll().stream().findFirst().orElseGet(() -> {
            Customer newCustomer = new Customer();
            newCustomer.setFirstName("Test");
            newCustomer.setLastName("Customer");
            return customerRepository.saveAndFlush(newCustomer);
        });

        // Ensure the staff is actually linked to the store we are searching for
        staff.setStore(store);
        staffRepository.saveAndFlush(staff);

        // 4. Create the dummy Payment
        Payment payment = new Payment();
        payment.setAmount(new BigDecimal("9.99"));
        payment.setPaymentDate(LocalDateTime.now());
        payment.setCustomer(customer);
        payment.setStaff(staff);

        // 5. Save and flush so the API can see it
        testPayment = paymentRepository.saveAndFlush(payment);
    }

    @Test
    @DisplayName("GET /payments/search/... - Verify search by Store ID")
    void testGetPaymentsByStore() throws Exception {
        mockMvc.perform(get("/payments/search/findPaymentsByStaff_Store_StoreId")
                        .param("storeId", store.getStoreId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.payments").exists())
                .andExpect(jsonPath("$._embedded.payments").isArray())
                .andExpect(jsonPath("$._embedded.payments").isNotEmpty()); // Ensures it's not an empty array
    }

    @Test
    @DisplayName("Verify paymentDate existence in search results")
    void testPaymentDateExists() throws Exception {
        mockMvc.perform(get("/payments/search/findPaymentsByStaff_Store_StoreId")
                        .param("storeId", store.getStoreId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.payments[0].paymentDate").exists());
    }

    @Test
    @DisplayName("Verify staff relationship link or ID exists")
    void testPaymentStaffIdExists() throws Exception {
        mockMvc.perform(get("/payments/search/findPaymentsByStaff_Store_StoreId")
                        .param("storeId", store.getStoreId().toString()))
                .andExpect(status().isOk())
                // Verify the HATEOAS link to the staff exists
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

    /*@Test
    @DisplayName("Verify invalid store ID returns empty embedded list")
    void testGetPaymentsByStore_InvalidStore() throws Exception {
        mockMvc.perform(get("/payments/search/findPaymentsByStaff_Store_StoreId")
                        .param("storeId", "99999")) // Guaranteed not to exist
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.payments").isEmpty()); // Should be empty
    }*/

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
                .andExpect(jsonPath("$.page").exists());
    }
}