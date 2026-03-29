package com.rcoem.filmrentalstore.api;

import com.rcoem.filmrentalstore.entities.*;
import com.rcoem.filmrentalstore.repository.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // Rolls back changes after each test
class PaymentSearchApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired private StaffRepository staffRepository;

    @Autowired private CustomerRepository customerRepository;

    @Autowired private PaymentRepository paymentRepository;

    @Autowired private AddressRepository addressRepository;

    @Autowired private StoreRepository storeRepository;

    private Staff createAndSaveStaff(String username) {
        // We must create Address and Store first because of your @NotNull constraints
        Address address = new Address();
        address.setAddress("s");
        address = addressRepository.save(address);

        Store store = new Store();
        store.setAddress(address);
        storeRepository.save(store);

        Staff s = new Staff("John", "Doe", "password123");
        s.setUsername(username);
        s.setEmail(username + "@example.com");
        s.setActive(true);
        s.setAddress(address);
        s.setStore(store);
        return staffRepository.save(s);
    }

    private Customer createAndSaveCustomer(String fName, String lName) {
        Address address = new Address();
        address.setAddress("s");
        address = addressRepository.save(address);

        Customer c = new Customer();
        c.setFirstName(fName);
        c.setLastName(lName);
        c.setActive('Y');
        c.setAddress(address);
        return customerRepository.save(c);
    }

    private void savePayment(Double amount, Staff staff, Customer customer) {
        Payment p = new Payment();
        p.setAmount(amount);
        p.setStaff(staff);
        p.setCustomer(customer);
        paymentRepository.save(p);
    }

    @Test
    void shouldReturnPaymentSummaryByStaffUsername() throws Exception {
        // 1. Setup Data using helper methods
        Staff staff = createAndSaveStaff("jdoe");
        Customer customer = createAndSaveCustomer("Jane", "Smith");

        // 2. Create and link the Payment
        savePayment(100.50, staff, customer);


        // 2. Perform the API call using the specific Projection name
        mockMvc.perform(get("/payments/search/findByStaff_Username")
                        .param("username", "jdoe")
                        .param("projection", "paymentSummary") // Explicitly requesting the summary
                        .contentType(MediaType.APPLICATION_JSON))

                // 3. Assertions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.payments[0].amount").value(100.50))
                .andExpect(jsonPath("$._embedded.payments[0].customerName").value("Jane Smith"))
                .andExpect(jsonPath("$._embedded.payments[0].paymentDate").exists())
                // Ensure staff_id or other sensitive fields are NOT present
                .andExpect(jsonPath("$._embedded.payments[0].staff").doesNotExist());
    }

    @Test
    void shouldReturnEmptyResponseWhenUsernameDoesNotExist() throws Exception {
        // 1. Perform call with a random username
        mockMvc.perform(get("/payments/search/findByStaff_Username")
                        .param("username", "ghost_user_999")
                        .param("projection", "paymentSummary")
                        .contentType(MediaType.APPLICATION_JSON))
                // 2. Assertions
                .andExpect(status().isOk())
                // The result should not contain any payment items
                .andExpect(jsonPath("$._embedded.payments").isArray())
                .andExpect(jsonPath("$._embedded.payments").isEmpty());
    }

    @Test
    void shouldBeCaseInsensitiveByDatabaseDefault() throws Exception {
        // 1. Setup: Create 'ADO'
        Staff staff = createAndSaveStaff("ADO");
        paymentRepository.save(new Payment(10.0, LocalDateTime.now(), staff, createAndSaveCustomer("A", "B")));

        // 2. Perform Call with lowercase 'ado'
        mockMvc.perform(get("/payments/search/findByStaff_Username")
                        .param("username", "ado") // Different case
                        .param("projection", "paymentSummary")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())

                // 3. Assertions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.payments").exists())
                .andExpect(jsonPath("$._embedded.payments[0].customerName").exists());
    }

    @Test
    void shouldReturnMultiplePaymentsForSingleStaff() throws Exception {
        Staff staff = createAndSaveStaff("jdoe_multi");
        Customer customer = createAndSaveCustomer("Jane", "Smith");

        // Create two payments
        savePayment(50.0, staff, customer);
        savePayment(75.0, staff, customer);

        mockMvc.perform(get("/payments/search/findByStaff_Username")
                        .param("username", "jdoe_multi")
                        .param("projection", "paymentSummary")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.payments.length()").value(2));
    }

    @Test
    void shouldReturnPaginatedPayments() throws Exception {
        // 1. Setup: Create 1 Staff and 3 Payments
        Staff staff = createAndSaveStaff("pager_user");
        Customer customer = createAndSaveCustomer("Jane", "Doe");

        savePayment(10.0, staff, customer);
        savePayment(20.0, staff, customer);
        savePayment(30.0, staff, customer);

        // 2. Perform Call requesting page 0 with a size of 2
        mockMvc.perform(get("/payments/search/findByStaff_Username")
                        .param("username", "pager_user")
                        .param("projection", "paymentSummary")
                        .param("size", "2")  // Limit to 2 results per page
                        .param("page", "0")  // Request the first page
                        .contentType(MediaType.APPLICATION_JSON))

                // 3. Assertions
                .andExpect(status().isOk())
                // Check that only 2 payments are in the current response body
                .andExpect(jsonPath("$._embedded.payments.length()").value(2))

                // 4. Verify Pagination Metadata
                .andExpect(jsonPath("$.page.size").value(2))
                .andExpect(jsonPath("$.page.totalElements").value(3))
                .andExpect(jsonPath("$.page.totalPages").value(2))
                .andExpect(jsonPath("$.page.number").value(0))

                // 5. Verify HATEOAS Links for navigation
                .andExpect(jsonPath("$._links.next.href").exists())
                .andExpect(jsonPath("$._links.last.href").exists());
    }
}