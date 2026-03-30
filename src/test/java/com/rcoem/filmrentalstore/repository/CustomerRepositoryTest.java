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

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
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

    @BeforeEach
    public void setup() {
        customerRepository.deleteAll();
        testTimestamp = Timestamp.from(Instant.now());
        address = addressRepository.findById((short) 5).orElse(null);
        store = storeRepository.findById((byte) 1).orElse(null);
        Customer customer = new Customer("Tom", "Hanks", "tom@email.com", store,address);
        testCustomer = customerRepository.save(customer);
    }

    //Positive Tests

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
        List<Customer> foundList = customerRepository.findByFirstName(testCustomer.getFirstName());

        assertThat(foundList).isNotEmpty();
        assertThat(foundList.get(0).getLastName()).isEqualTo(testCustomer.getLastName());
    }

    @Test
    public void testFindByLastName_Valid() {

        List<Customer> foundList = customerRepository.findByLastName(testCustomer.getLastName());

        assertThat(foundList).isNotEmpty();
        assertThat(foundList.get(0).getFirstName()).isEqualTo(testCustomer.getFirstName());
    }

    @Test
    public void testFindByEmail_Valid() {

        Optional<Customer> found = customerRepository.findByEmail(testCustomer.getEmail());

        assertThat(found).isPresent();
        assertThat(found.get().getFirstName()).isEqualTo(testCustomer.getFirstName());
    }

    @Test
    public void testFindByActive_Valid() {

        List<Customer> foundList = customerRepository.findByActive(testCustomer.getActive());

        assertThat(foundList).isNotEmpty();
        assertThat(foundList.get(0).getFirstName()).isEqualTo(testCustomer.getFirstName());
    }

    @Test
    public void testFindByCreateDate_Valid() {
        Customer dbCustomer = customerRepository.findById(testCustomer.getCustomerId()).orElseThrow();
        LocalDateTime exactDbDate = dbCustomer.getCreateDate();

        List<Customer> foundList = customerRepository.findByCreateDate(exactDbDate);

        assertThat(foundList).isNotEmpty();
        assertThat(foundList.get(0).getFirstName()).isEqualTo(testCustomer.getFirstName());
    }

    //Negative Tests

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
