package com.rcoem.filmrentalstore.repository;

import com.rcoem.filmrentalstore.dto.PaymentView;
import com.rcoem.filmrentalstore.entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class StaffPaymentTest {

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private CountryRepository countryRepository;

    private Staff staff;
    private Customer customer;
    private Store store;

    @BeforeEach
    public void setup() {
        paymentRepository.deleteAll();
        staffRepository.deleteAll();
        customerRepository.deleteAll();
        storeRepository.deleteAll();
        addressRepository.deleteAll();
        cityRepository.deleteAll();
        countryRepository.deleteAll();

        // create country
        Country country = new Country();
        country.setCountry("TestCountry");
        country = countryRepository.save(country);

        // create city
        City city = new City();
        city.setCity("TestCity");
        city.setCountry(country);
        city = cityRepository.save(city);

        // create address
        Address address = new Address();
        address.setAddress("123 Test Street");
        address.setDistrict("Test District");
        address.setPhone("1234567890");
        address.setCity(city);
        address = addressRepository.save(address);

        // create store
        store = new Store();
        store.setAddress(address);
        store = storeRepository.save(store);

        // create staff
        staff = new Staff();
        staff.setFirstName("Test");
        staff.setLastName("Staff");
        staff.setPassword("pass123");
        staff.setActive(true);
        staff.setAddress(address);
        staff.setStore(store);
        staff = staffRepository.save(staff);

        // create customer
        customer = new Customer();
        customer.setFirstName("Test");
        customer.setLastName("Customer");
        customer.setActive(true);
        customer.setStore(store);
        customer.setAddress(address);
        customer = customerRepository.save(customer);
    }

    // ==================== CREATE ====================

    @Test
    @DisplayName("Staff should be able to add a payment")
    public void testStaffAddsPayment() {
        Payment payment = new Payment();
        payment.setAmount(500.00);
        payment.setStaff(staff);
        payment.setCustomer(customer);

        Payment saved = paymentRepository.save(payment);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getAmount()).isEqualTo(500.00);
        assertThat(saved.getStaff().getId()).isEqualTo(staff.getId());
        System.out.println("Payment added by staff: " + staff.getId());
    }

    @Test
    @DisplayName("Staff should be able to add multiple payments")
    public void testStaffAddsMultiplePayments() {
        Payment payment1 = new Payment();
        payment1.setAmount(300.00);
        payment1.setStaff(staff);
        payment1.setCustomer(customer);

        Payment payment2 = new Payment();
        payment2.setAmount(700.00);
        payment2.setStaff(staff);
        payment2.setCustomer(customer);

        paymentRepository.saveAll(List.of(payment1, payment2));

        List<Payment> payments = paymentRepository.findAll();
        assertThat(payments).hasSizeGreaterThanOrEqualTo(2);
        System.out.println("Total payments added: " + payments.size());
    }

    // ==================== READ ====================

    @Test
    @DisplayName("Should fetch payment by id")
    public void testFindPaymentById() {
        Payment payment = new Payment();
        payment.setAmount(500.00);
        payment.setStaff(staff);
        payment.setCustomer(customer);
        Payment saved = paymentRepository.save(payment);

        Optional<Payment> found = paymentRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getAmount()).isEqualTo(500.00);
        System.out.println("Found payment amount: " + found.get().getAmount());
    }

    @Test
    @DisplayName("Should fetch all payments by store")
    public void testFindPaymentsByStore() {
        Payment payment = new Payment();
        payment.setAmount(500.00);
        payment.setStaff(staff);
        payment.setCustomer(customer);
        paymentRepository.save(payment);

        List<PaymentView> payments = paymentRepository
            .findByStaff_Store_StoreId(store.getStoreId());

        assertThat(payments).isNotEmpty();
        System.out.println("Payments for store: " + payments.size());
    }

    @Test
    @DisplayName("Should verify payment belongs to correct staff")
    public void testPaymentBelongsToStaff() {
        Payment payment = new Payment();
        payment.setAmount(500.00);
        payment.setStaff(staff);
        payment.setCustomer(customer);
        Payment saved = paymentRepository.save(payment);

        Optional<Payment> found = paymentRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getStaff().getId()).isEqualTo(staff.getId());
        System.out.println("Payment belongs to staff: " + found.get().getStaff().getId());
    }

    // ==================== UPDATE ====================

    @Test
    @DisplayName("Staff should be able to update payment amount")
    public void testStaffUpdatesPaymentAmount() {
        Payment payment = new Payment();
        payment.setAmount(500.00);
        payment.setStaff(staff);
        payment.setCustomer(customer);
        Payment saved = paymentRepository.save(payment);

        saved.setAmount(750.00);
        Payment updated = paymentRepository.save(saved);

        assertThat(updated.getAmount()).isEqualTo(750.00);
        System.out.println("Updated payment amount: " + updated.getAmount());
    }

    @Test
    @DisplayName("Staff should be able to reassign payment to another staff")
    public void testStaffReassignsPayment() {
        // create second staff
        Address address = addressRepository.findAll().get(0);
        Staff anotherStaff = new Staff();
        anotherStaff.setFirstName("Another");
        anotherStaff.setLastName("Staff");
        anotherStaff.setPassword("pass456");
        anotherStaff.setActive(true);
        anotherStaff.setAddress(address);
        anotherStaff.setStore(store);
        anotherStaff = staffRepository.save(anotherStaff);

        Payment payment = new Payment();
        payment.setAmount(500.00);
        payment.setStaff(staff);
        payment.setCustomer(customer);
        Payment saved = paymentRepository.save(payment);

        saved.setStaff(anotherStaff);
        Payment updated = paymentRepository.save(saved);

        assertThat(updated.getStaff().getId()).isEqualTo(anotherStaff.getId());
        System.out.println("Payment reassigned to staff: " + updated.getStaff().getId());
    }

    // ==================== DELETE ====================

    @Test
    @DisplayName("Staff should be able to delete a payment")
    public void testStaffDeletesPayment() {
        Payment payment = new Payment();
        payment.setAmount(500.00);
        payment.setStaff(staff);
        payment.setCustomer(customer);
        Payment saved = paymentRepository.save(payment);

        paymentRepository.deleteById(saved.getId());

        Optional<Payment> found = paymentRepository.findById(saved.getId());
        assertThat(found).isEmpty();
        System.out.println("Payment deleted successfully");
    }

    @Test
    @DisplayName("Staff should be able to delete all payments")
    public void testStaffDeletesAllPayments() {
        Payment payment1 = new Payment();
        payment1.setAmount(300.00);
        payment1.setStaff(staff);
        payment1.setCustomer(customer);

        Payment payment2 = new Payment();
        payment2.setAmount(700.00);
        payment2.setStaff(staff);
        payment2.setCustomer(customer);

        paymentRepository.saveAll(List.of(payment1, payment2));
        paymentRepository.deleteAll();

        List<Payment> payments = paymentRepository.findAll();
        assertThat(payments).isEmpty();
        System.out.println("All payments deleted");
    }

    // ==================== COUNT ====================

    @Test
    @DisplayName("Should count staff in DB")
    void checkStaffCount() {
        long count = staffRepository.count();
        System.out.println("Staff count in DB: " + count);
        assertThat(count).isGreaterThan(0);
    }
}