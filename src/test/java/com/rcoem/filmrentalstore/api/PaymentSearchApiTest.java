package com.rcoem.filmrentalstore.api;

import com.rcoem.filmrentalstore.entities.*;
import com.rcoem.filmrentalstore.repository.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // Critical: Keeps Sakila clean by rolling back all test data
class PaymentSearchApiTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private StaffRepository staffRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private PaymentRepository paymentRepository;
    @Autowired private AddressRepository addressRepository;
    @Autowired private StoreRepository storeRepository;

    private Address sharedAddress;
    private Store sharedStore;

    @BeforeEach
    void setUpInfrastructure() {
        // Fetch or create a base address and store to avoid FK bloat
        sharedAddress = addressRepository.findAll().stream().findFirst()
                .orElseGet(() -> {
                    Address addr = new Address();
                    addr.setAddress("123 Test St");
                    addr.setDistrict("Test District");
                    addr.setPhone("123456");
                    // If your schema uses Spatial types (Point), ensure they are set here
                    return addressRepository.save(addr);
                });

        sharedStore = storeRepository.findAll().stream().findFirst()
                .orElseGet(() -> {
                    Store s = new Store();
                    s.setAddress(sharedAddress);
                    return storeRepository.save(s);
                });
    }

    private Staff createAndSaveStaff(String username) {
        Staff s = new Staff();
        s.setFirstName("Test");
        s.setLastName("Staff");
        s.setUsername(username);
        s.setPassword("secret");
        s.setEmail(username + "@sakila.com");
        s.setActive(true);
        s.setAddress(sharedAddress);
        s.setStore(sharedStore);
        return staffRepository.save(s);
    }

    private Customer createAndSaveCustomer(String fName, String lName) {
        Customer c = new Customer();
        c.setFirstName(fName);
        c.setLastName(lName);
        c.setEmail(fName + "." + lName + "@test.com");
        c.setActive(true);
        c.setAddress(sharedAddress);
        c.setStore(sharedStore); // Sakila customers belong to a store
        return customerRepository.save(c);
    }

    private void savePayment(Double amount, Staff staff, Customer customer) {
        Payment p = new Payment();
        p.setAmount(BigDecimal.valueOf(amount)); // Sakila usually uses BigDecimal
        p.setStaff(staff);
        p.setCustomer(customer);
        p.setPaymentDate(LocalDateTime.now()); // Required in most Sakila schemas
        paymentRepository.save(p);
    }

    @Test
    @DisplayName("GET /payments/search/findByStaff_Username - Success with Projection")
    void shouldReturnPaymentSummaryByStaffUsername() throws Exception {
        Staff staff = createAndSaveStaff("jdoe");
        Customer customer = createAndSaveCustomer("Jane", "Smith");
        savePayment(99.50, staff, customer);

        mockMvc.perform(get("/payments/search/findByStaff_Username")
                        .param("username", "jdoe")
                        .param("projection", "paymentSummary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.payments[0].amount").value(99.5))
                // Note: Ensure your PaymentSummary projection has a getCustomerName()
                // that concatenates customer.firstName + lastName
                .andExpect(jsonPath("$._embedded.payments[0].customerName").exists())
                .andExpect(jsonPath("$._embedded.payments[0].staff").doesNotExist());
    }

    @Test
    @DisplayName("GET /payments/search/findByStaff_Username - Pagination")
    void shouldReturnPaginatedPayments() throws Exception {
        Staff staff = createAndSaveStaff("pager_user");
        Customer customer = createAndSaveCustomer("Jane", "Doe");

        savePayment(10.0, staff, customer);
        savePayment(20.0, staff, customer);
        savePayment(30.0, staff, customer);

        mockMvc.perform(get("/payments/search/findByStaff_Username")
                        .param("username", "pager_user")
                        .param("size", "2")
                        .param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.payments.length()").value(2))
                .andExpect(jsonPath("$.page.totalElements").value(3))
                .andExpect(jsonPath("$._links.next").exists());
    }

    @Test
    @DisplayName("GET /payments/search/findByStaff_Username - Case Insensitivity")
    void shouldBeCaseInsensitive() throws Exception {
        Staff staff = createAndSaveStaff("UPPERCASE");
        savePayment(50.0, staff, createAndSaveCustomer("A", "B"));

        mockMvc.perform(get("/payments/search/findByStaff_Username")
                        .param("username", "uppercase"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.payments").isNotEmpty());
    }
}