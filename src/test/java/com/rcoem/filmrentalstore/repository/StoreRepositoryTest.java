package com.rcoem.filmrentalstore.repository;

import com.rcoem.filmrentalstore.entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import com.rcoem.filmrentalstore.entities.Store;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import jakarta.validation.ConstraintViolationException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StoreRepositoryTest {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private StaffRepository staffRepository;

    private Store testStore;
    private Address testAddress;
    private Staff testManager;


    @BeforeEach
    void setup() {
        staffRepository.deleteAll();
        storeRepository.deleteAll();
        addressRepository.deleteAll();

        Address address = new Address();
        address.setAddress("123 Main St");
        address.setDistrict("Central");
        address.setPhone("555-0123");
        testAddress = addressRepository.save(address);

        Store store = new Store();
        store.setAddress(testAddress);
        testStore = storeRepository.save(store);

        Staff staff = new Staff();
        staff.setFirstName("John");
        staff.setLastName("Doe");
        staff.setAddress(testAddress);
        staff.setStore(testStore);
        staff.setActive(true);
        staff.setPassword("secret");
        testManager = staffRepository.save(staff);

        testStore.setManager(testManager);
        storeRepository.save(testStore);
    }


    @Test
    void testSaveStore() {
        Staff manager = staffRepository.save(testManager);
        Address address = addressRepository.save(new Address());

        Store store = new Store();
        store.setManager(manager);
        store.setAddress(address);
        Store saved = storeRepository.save(store);

        assertThat(saved).isNotNull();
        assertThat(saved.getStoreId()).isNotNull();
    }


    @Test
    void testViewAllStores() {
        List<Store> stores = storeRepository.findAll();
        assertThat(stores).isNotEmpty();
        assertThat(stores.size()).isGreaterThanOrEqualTo(1);
    }


    @Test
    void testUpdateStore() {
        Address newAddress = addressRepository.save(new Address());
        testStore.setAddress(newAddress);
        storeRepository.save(testStore);
        Store updated = storeRepository.findById(testStore.getStoreId()).get();
        assertThat(updated.getAddress()).isEqualTo(newAddress);
    }


    @Test
    void testFindStoreByAddress() {
        List<Store> found = storeRepository.findByAddress(testAddress);
        assertThat(found).isNotEmpty();
        assertThat(found.get(0).getAddress()).isEqualTo(testAddress);
    }


    @Test
    void testDeleteStore() {
        storeRepository.delete(testStore);
        Optional<Store> deleted = storeRepository.findById(testStore.getStoreId());
        assertThat(deleted).isEmpty();
    }


    // Negative and Null Cases :

    @Test
    void testSaveStore_NullAddress_ShouldThrowException() {
        // Create store without an address
        Store store = new Store();
        store.setManager(testManager);

        // saveAndFlush forces Hibernate to send the SQL to DB immediately to trigger the error
        assertThrows(ConstraintViolationException.class, () -> {
            storeRepository.saveAndFlush(store);
        });
    }

    @Test
    void testFindStoreByAddress_NonExistentAddress() {
        Address unsavedAddress = addressRepository.save(new Address());

        List<Store> found = storeRepository.findByAddress(unsavedAddress);

        assertThat(found).isEmpty();
    }

    @Test
    void testFindById_InvalidId() {
        // ID that definitely doesn't exist
        Optional<Store> found = storeRepository.findById((byte) 9999L);

        assertThat(found).isNotPresent();
    }

    //Could create circular dependency
    /*@Test
    void testUpdateStore_SetManagerToNull_ShouldFail() {
        testStore.setManager(null);

        assertThrows(ConstraintViolationException.class, () -> {
            storeRepository.saveAndFlush(testStore);
        });
    }*/
}