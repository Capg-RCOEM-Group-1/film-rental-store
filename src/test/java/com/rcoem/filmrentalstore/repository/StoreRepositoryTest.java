package com.rcoem.filmrentalstore.repository;

import com.rcoem.filmrentalstore.entities.Address;
import com.rcoem.filmrentalstore.entities.Staff;
import com.rcoem.filmrentalstore.entities.Store;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class StoreRepositoryTest {

    @Autowired
    StoreRepository storeRepo;
    @Autowired
    AddressRepository addressRepo;
    @Autowired
    StaffRepository staffRepo;

    Address address;
    Staff staff;
    Store store;

    // --------------------- Make a testing Entity that can be used for all the testCases ------------------------------
    @BeforeEach
    void setup() {

        store = storeRepo.findById((byte) 1).orElseThrow(() -> new RuntimeException(("Storeid Not found")));

        address = addressRepo.findById((short) 1)
                .orElseThrow(() -> new RuntimeException("Address ID 1 not found"));

        staff = staffRepo.findById((byte) 2)
                .orElseThrow(() -> new RuntimeException("Staff ID 1 not found"));
    }
    //------------------------------------------------------------------------------------------------------------------


    //--------- Test Cases to Save the Store ( Valid , Negative , Null , Without Manager , Without Address) ------------
    @Test
    @Transactional
    void testSaveStore() {
        Staff manager = new Staff();
        manager.setFirstName("Test");
        manager.setLastName("Manager");
        manager.setAddress(address);
        manager.setActive(true);
        manager.setStore(store);
        manager.setUsername("am");
        manager.setPassword("123");
        manager.setEmail("gd");

        Staff manager1 = staffRepo.save(manager);

        // Step 2: Create Store with this manager
        Store store1 = new Store();
        store1.setStoreId((byte) 2);
        store1.setAddress(address);
        store1.setManager(manager1);

        store1 = storeRepo.save(store1);

        manager1.setStore(store1);
        staffRepo.save(manager1);

        // Assertions
        Optional<Store> saved = storeRepo.findById(store.getStoreId());
        assertThat(saved).isPresent();
        assertThat(saved.get().getAddress().getAddress())
                .isEqualTo(address.getAddress());
    }

    @Test
    @Transactional
    void testNullSaveStore() {
        assertThatThrownBy(() -> storeRepo.save(null))
                .isInstanceOf(InvalidDataAccessApiUsageException.class);
    }

    @Test
    @Transactional
    void testSaveStoreMissingAddress() {
        Store store = new Store();
        store.setManager(staff);

        assertThatThrownBy(() -> storeRepo.saveAndFlush(store))
                .isInstanceOf(Exception.class);
    }

    @Test
    @Transactional
    void testSaveStoreWithoutManager() {
        Store store = new Store();
        store.setAddress(address);

        assertThatThrownBy(() -> storeRepo.saveAndFlush(store))
                .isInstanceOf(Exception.class);
    }

    @Test
    @Transactional
    void testSaveStoreWithoutAddress() {
        Store store = new Store();
        store.setManager(staff);

        assertThatThrownBy(() -> storeRepo.saveAndFlush(store))
                .isInstanceOf(Exception.class);
    }
    // -----------------------------------------------------------------------------------------------------------------


    //------------------------ Test Cases to View the Store ( All , ByAddress ,  ) ---------------------------------------
    @Test
    @Transactional
    void testViewAllStores() {
        List<Store> list = storeRepo.findAll();
        assertThat(list.size()).isGreaterThan(0);
    }

    @Test
    @Transactional
    void testFindStoreByAddress() {
        List<Store> found = storeRepo.findByAddress(address);

        assertThat(found).isNotNull();
        assertThat(found.size()).isGreaterThan(0);
    }
    // -----------------------------------------------------------------------------------------------------------------


    //------------------------ Test Cases to Update the Store ( Valid , Negative , Null , New Address ) ------------------------------
    @Test
    @Transactional
    void testUpdateStore() {

        Staff manager = new Staff();
        manager.setFirstName("Test");
        manager.setLastName("Manager");
        manager.setAddress(address);
        manager.setActive(true);
        manager.setStore(store);
        manager.setUsername("u1");
        manager.setPassword("123");
        manager.setEmail("e1");

        Staff manager1 = staffRepo.save(manager);

        Store store1 = new Store();
        store1.setStoreId((byte) 2);
        store1.setAddress(address);
        store1.setManager(manager1);

        store1 = storeRepo.save(store1);

        manager1.setStore(store1);
        staffRepo.save(manager1);

        Store saved = storeRepo.findById(store1.getStoreId()).get();
        saved.setAddress(address);

        storeRepo.save(saved);

        assertThat(saved.getStoreId()).isEqualTo(store1.getStoreId());
    }

    @Test
    @Transactional
    void testNullUpdateStore() {

        Staff manager = new Staff();
        manager.setFirstName("Test");
        manager.setLastName("Manager");
        manager.setAddress(address);
        manager.setActive(true);
        manager.setStore(store);
        manager.setUsername("u2");
        manager.setPassword("123");
        manager.setEmail("e2");

        Staff manager1 = staffRepo.save(manager);

        Store store1 = new Store();
        store1.setStoreId((byte) 2);
        store1.setAddress(address);
        store1.setManager(manager1);

        store1 = storeRepo.save(store1);

        manager1.setStore(store1);
        staffRepo.save(manager1);

        Store saved = storeRepo.findById(store1.getStoreId()).get();
        saved.setAddress(null);

        assertThatThrownBy(() -> storeRepo.saveAndFlush(saved))
                .isInstanceOf(Exception.class);
    }


    @Test
    @Transactional
    void testInvalidIdUpdateStore() {
        Optional<Store> missing = storeRepo.findById((byte) 99);
        assertThat(missing).isNotPresent();
    }

    @Test
    @Transactional
    void testUpdateStoreWithNewAddress() {
        Address newAddress = addressRepo.findById((short) 2)
                .orElseThrow(() -> new RuntimeException("Address ID 2 not found"));

        Staff manager = new Staff();
        manager.setFirstName("Test");
        manager.setLastName("Manager");
        manager.setAddress(address);
        manager.setActive(true);
        manager.setStore(store);
        manager.setUsername("u3");
        manager.setPassword("123");
        manager.setEmail("e3");

        Staff manager1 = staffRepo.save(manager);

        Store store1 = new Store();
        store1.setStoreId((byte) 2);
        store1.setAddress(address);
        store1.setManager(manager1);

        store1 = storeRepo.save(store1);

        manager1.setStore(store1);
        staffRepo.save(manager1);

        store1.setAddress(newAddress);
        Store updated = storeRepo.save(store1);

        assertThat(updated.getAddress().getAddress()).isEqualTo(newAddress.getAddress());
    }

    // -----------------------------------------------------------------------------------------------------------------


    //------------------------ Test Cases to Delete the Store ( Valid , Negative , Null , ALL ) ------------------------------
    @Test
    @Transactional
    void testDeleteStore() {
        Store store = storeRepo.findAll().get(0);

        storeRepo.deleteById(store.getStoreId());

        Optional<Store> deleted = storeRepo.findById(store.getStoreId());
        assertThat(deleted).isNotPresent();
    }

    @Test
    @Transactional
    void testDeleteStoreInvalidId() {
        storeRepo.deleteById((byte) 100);
        assertThat(storeRepo.findById((byte) 100)).isNotPresent();
    }

    @Test
    @Transactional
    void testDeleteNullStore() {
        assertThatThrownBy(() -> storeRepo.delete(null))
                .isInstanceOf(InvalidDataAccessApiUsageException.class);
    }



    // -----------------------------------------------------------------------------------------------------------------


    //---------------------------------- Methods Needed to For Test Cases ----------------------------------------------
    private Staff getAvailableManager() {
        List<Staff> all = staffRepo.findAll();

        List<Byte> managerIds = storeRepo.findAll()
                .stream()
                .map(s -> s.getManager().getStaffId())
                .toList();

        return all.stream()
                .filter(st -> !managerIds.contains(st.getStaffId()))
                .findFirst()
                .orElse(all.get(0));
    }

}