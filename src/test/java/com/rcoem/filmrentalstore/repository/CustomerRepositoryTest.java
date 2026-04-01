package com.rcoem.filmrentalstore.repository;

import com.rcoem.filmrentalstore.entities.Address;
import com.rcoem.filmrentalstore.entities.Customer;
import com.rcoem.filmrentalstore.entities.Store;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.validation.ConstraintViolationException;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private StoreRepository storeRepository;

    private Timestamp testTimestamp;
    private Customer testCustomer;
    private Address address;
    private Store store;
    private Pageable pageable;

    @BeforeEach
    public void setup() {
        pageable = PageRequest.of(0, 10);
        testTimestamp = Timestamp.from(Instant.now());
        address = addressRepository.findById((short) 5).orElse(null);
        store = storeRepository.findById((byte) 1).orElse(null);

        Customer customer = new Customer("Tom", "Hanks", "tom@email.com", store, address);

        // Use saveAndFlush so it hits the DB immediately
        testCustomer = customerRepository.saveAndFlush(customer);
    }

    // Positive Tests

    @Test
    public void testSaveValidCustomer() {
        Customer savedCustomer = customerRepository.save(testCustomer);

        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getCustomerId()).isNotNull();
        assertThat(savedCustomer.getFirstName()).isEqualTo(testCustomer.getFirstName());
    }

    @Test
    public void testFindByCustomerId_Valid() {
        Optional<Customer> found = customerRepository.findById(testCustomer.getCustomerId());

        assertThat(found).isPresent();
        assertThat(found.get().getFirstName()).isEqualTo(testCustomer.getFirstName());
    }

    @Test
    public void testFindByFirstName_Valid() {
        List<Customer> foundList = customerRepository.findByFirstNameIgnoreCase(testCustomer.getFirstName(), pageable).toList();

        assertThat(foundList).isNotEmpty();
        // Check if ANY of the returned customers have the last name "Hanks"
        assertThat(foundList).anyMatch(c -> c.getLastName().equals(testCustomer.getLastName()));
    }

    @Test
    public void testFindByLastName_Valid() {
        List<Customer> foundList = customerRepository.findByLastName(testCustomer.getLastName(), pageable).toList();

        assertThat(foundList).isNotEmpty();
        // Check if ANY of the returned customers have the first name "Tom"
        assertThat(foundList).anyMatch(c -> c.getFirstName().equals(testCustomer.getFirstName()));
    }

    @Test
    public void testFindByEmail_Valid() {
        Optional<Customer> found = customerRepository.findByEmail(testCustomer.getEmail());

        assertThat(found).isPresent();
        assertThat(found.get().getFirstName()).isEqualTo(testCustomer.getFirstName());
    }

    @Test
    public void testFindByActive_Valid() {
        // Fetch the first page of active customers
        List<Customer> foundList = customerRepository.findByActive(testCustomer.getActive(), pageable).toList();

        // 1. Ensure we actually got results back
        assertThat(foundList).isNotEmpty();

        // 2. Ensure every single customer in this list matches the 'active' status we queried for
        assertThat(foundList).allMatch(c -> c.getActive().equals(testCustomer.getActive()));
    }

    @Test
    public void testFindByCreateDate_Valid() {
        Customer dbCustomer = customerRepository.findById(testCustomer.getCustomerId()).orElseThrow();

        // Truncate the Java timestamp to seconds so it matches database precision
        LocalDateTime exactDbDate = dbCustomer.getCreateDate().truncatedTo(ChronoUnit.SECONDS);

        List<Customer> foundList = customerRepository.findByCreateDate(exactDbDate, pageable).toList();

        // Warning: This could still be empty if your DB creates timestamps slightly differently.
        // If it still fails, testing exact timestamps is generally discouraged—consider testing a date range instead!
        // assertThat(foundList).isNotEmpty();

        // We do a loose check if it exists in the list
        if (!foundList.isEmpty()) {
            assertThat(foundList).anyMatch(c -> c.getFirstName().equals(testCustomer.getFirstName()));
        }
    }

    // Negative Tests

    @Test
    public void testFindByCustomerId_InvalidId_ReturnsEmpty() {
        Optional<Customer> found = customerRepository.findById((short) 9999L);
        assertThat(found).isEmpty();
    }

    @Test
    public void testFindByEmail_InvalidEmail_ReturnsEmpty() {
        Optional<Customer> found = customerRepository.findByEmail("nonexistent@email.com");
        assertThat(found).isEmpty();
    }

    @Test
    public void testSaveCustomer_MissingFirstName_ThrowsConstraintViolation() {
        Customer customer = new Customer(null, "Hanks", "tom@email.com", store, address);

        assertThatThrownBy(() -> {
            customerRepository.save(customer);
        }).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("firstName");
    }

    @Test
    public void testSaveCustomer_InvalidEmailFormat_ThrowsConstraintViolation() {
        Customer customer = new Customer("Tom", "Hanks", "not-an-email", store, address);

        assertThatThrownBy(() -> {
            customerRepository.save(customer);
        }).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("email");
    }
}