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
        // 1. Clear existing data in correct dependency order
        staffRepository.deleteAll();
        storeRepository.deleteAll();
        addressRepository.deleteAll();

        // 2. Create and SAVE the Address first
        // This gives the address an ID so the @NotNull check in Store passes
        Address address = new Address();
        address.setAddress("123 Main St");
        address.setDistrict("Central");
        address.setPhone("555-0123");
        // Add any other required address fields here
        testAddress = addressRepository.save(address);

        // 3. Create the Store and link the saved Address
        Store store = new Store();
        store.setAddress(testAddress);
        // Do NOT set the manager yet, as the staff doesn't exist
        testStore = storeRepository.save(store);

        // 4. Create the Staff, link to Address and Store, then SAVE
        Staff staff = new Staff();
        staff.setFirstName("John");
        staff.setLastName("Doe");
        staff.setAddress(testAddress);
        staff.setStore(testStore);
        staff.setActive(true);
        staff.setPassword("secret");
        testManager = staffRepository.save(staff);

        // 5. Update the Store to set the Manager
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
        Optional<Store> found = storeRepository.findByAddress(testAddress);
        assertThat(found).isPresent();
        assertThat(found.get().getAddress()).isEqualTo(testAddress);
    }


    @Test
    void testDeleteStore() {
        storeRepository.delete(testStore);
        Optional<Store> deleted = storeRepository.findById(testStore.getStoreId());
        assertThat(deleted).isEmpty();
    }


    // Negative and Null Cases :

    //Could Create a Circular dependency
    /*@Test
    void testSaveStore_NullManager_ShouldThrowException() {
        // Create store without a manager
        Store store = new Store();
        store.setAddress(testAddress);
        // manager is null by default

        // Expecting a DataIntegrityViolation because manager_id is likely @NotNull in DB
        assertThrows(ConstraintViolationException.class, () -> {
            storeRepository.saveAndFlush(store);
        });
    }*/

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
        // Create a new address but DON'T save a store linked to it
        Address unsavedAddress = addressRepository.save(new Address());

        Optional<Store> found = storeRepository.findByAddress(unsavedAddress);

        assertThat(found).isEmpty();
    }

    @Test
    void testFindById_InvalidId() {
        // ID that definitely doesn't exist
        Optional<Store> found = storeRepository.findById(9999L);

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