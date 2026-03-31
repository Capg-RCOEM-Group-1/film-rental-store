package com.rcoem.filmrentalstore.repository;

import com.rcoem.filmrentalstore.entities.Address;
import com.rcoem.filmrentalstore.entities.Staff;
import com.rcoem.filmrentalstore.entities.Store;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StaffRepositoryTest {

    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private StoreRepository storeRepository;

    private final Pageable firstPage = PageRequest.of(0, 10);

    @BeforeEach
    public void setup() {
//        staffRepository.deleteAll();
//        storeRepository.deleteAll();
//        addressRepository.deleteAll();

        Address address = new Address();
        address.setAddress("123 Main St");
        address = addressRepository.save(address);

        Store store = new Store();
        store.setAddress(address);
        store = storeRepository.save(store);

        Staff activeStaff = new Staff();
        activeStaff.setFirstName("John");
        activeStaff.setLastName("Doe");
        activeStaff.setActive(true);
        activeStaff.setPassword("pass123");
        activeStaff.setAddress(address);
        activeStaff.setStore(store);

        Staff inactiveStaff = new Staff();
        inactiveStaff.setFirstName("Jane");
        inactiveStaff.setLastName("Smith");
        inactiveStaff.setActive(false);
        inactiveStaff.setPassword("pass456");
        inactiveStaff.setAddress(address);
        inactiveStaff.setStore(store);

        staffRepository.saveAll(List.of(activeStaff, inactiveStaff));
    }

    @Test
    @DisplayName("Should return only active staff members with pagination")
    public void testFindByActiveTrue() {
        // When
        Page<Staff> activeStaffPage = staffRepository.findByActiveTrue(firstPage);

        // Then
        assertThat(activeStaffPage.getContent()).hasSize(1);
        assertThat(activeStaffPage.getTotalElements()).isEqualTo(1);
        assertThat(activeStaffPage.getContent().get(0).getFirstName()).isEqualTo("John");
    }

    @Test
    @DisplayName("Should return only inactive staff members with pagination")
    public void testFindByActiveFalse() {
        // When
        Page<Staff> inactiveStaffPage = staffRepository.findByActiveFalse(firstPage);

        // Then
        assertThat(inactiveStaffPage.getContent()).hasSize(1);
        assertThat(inactiveStaffPage.getTotalElements()).isEqualTo(1);
        assertThat(inactiveStaffPage.getContent().get(0).getFirstName()).isEqualTo("Jane");
    }

    @Test
    @DisplayName("Should return empty page when no active staff exist")
    public void testFindByActiveTrue_Empty() {
        // Given
//        staffRepository.deleteAll();

        // When
        Page<Staff> result = staffRepository.findByActiveTrue(firstPage);

        // Then
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should handle multiple pages and limit results")
    public void testFindByActiveTrue_Multiple() {
        // Given: Add more active staff to test paging limits
        Pageable smallPage = PageRequest.of(0, 1); // Only 1 per page

        Staff secondActive = new Staff();
        secondActive.setFirstName("Alice");
        secondActive.setLastName("Wonderland");
        secondActive.setActive(true);
        secondActive.setPassword("secure789");
        secondActive.setAddress(addressRepository.findAll().get(0));
        secondActive.setStore(storeRepository.findAll().get(0));
        staffRepository.save(secondActive);

        // When
        Page<Staff> result = staffRepository.findByActiveTrue(smallPage);

        // Then
        assertThat(result.getContent()).hasSize(1); // Page size limit
        assertThat(result.getTotalElements()).isEqualTo(2); // Total count in DB
        assertThat(result.getTotalPages()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should reflect status change when staff is deactivated")
    public void testStatusChangeReflectedInPagination() {
        // Given
        Staff john = staffRepository.findAll().stream()
                .filter(s -> s.getFirstName().equals("John"))
                .findFirst().orElseThrow();

        john.setActive(false);
        staffRepository.saveAndFlush(john);

        // When
        Page<Staff> activeStaff = staffRepository.findByActiveTrue(firstPage);
        Page<Staff> inactiveStaff = staffRepository.findByActiveFalse(firstPage);

        // Then
        assertThat(activeStaff.getContent()).isEmpty();
        assertThat(inactiveStaff.getTotalElements()).isEqualTo(2);
    }
}