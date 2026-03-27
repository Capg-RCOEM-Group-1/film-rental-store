package com.rcoem.filmrentalstore.repository;

import com.rcoem.filmrentalstore.entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import com.rcoem.filmrentalstore.entities.Store;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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
        storeRepository.deleteAll();
        addressRepository.deleteAll();
        staffRepository.deleteAll();

        testManager = new Staff("Aditya", "Chomya", "696969");
        testManager = staffRepository.save(testManager);
        testAddress = new Address();
        testAddress = addressRepository.save(testAddress);

        Store store = new Store();
        store.setManager(testManager);
        store.setAddress(testAddress);

        testStore = storeRepository.save(store);
    }


    @Test
    void testSaveStore() {
        Staff manager = staffRepository.save(new Staff("Ameya", "Bhau", "12345"));
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
}