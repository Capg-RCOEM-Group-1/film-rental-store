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
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StaffRepositoryTest {

    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private StoreRepository storeRepository;

    @BeforeEach
    public void cleanup(){
        staffRepository.deleteAll();
        addressRepository.deleteAll();
        storeRepository.deleteAll();
    }

    @Test
    @DisplayName("Save Staff Test List")
    public void givenStudentObjects_whenSaveStaff_thenReturnStaffList() {
        Staff staff = new Staff();
        Address address = new Address();
        Store store = new Store();
        addressRepository.save(address);
        store.setAddress(address);
        storeRepository.save(store);
        staff.setFirstName("First Name");
        staff.setLastName("Last Name");
        staff.setEmail("Email");
        staff.setUsername("Username");
        staff.setPassword("Password");
        staff.setActive(true);
        staff.setAddress(address);
        staff.setStore(store);
        staff = staffRepository.save(staff);
        Staff staff1 = new Staff();
        staff1.setFirstName("First Name");
        staff1.setLastName("Last Name");
        staff1.setEmail("Email 1");
        staff1.setUsername("Username1");
        staff1.setPassword("Password");
        staff1.setActive(true);
        staff1.setStore(store);
        staff1.setAddress(address);
        staff1 = staffRepository.save(staff1);
        List<Staff> staffList = staffRepository.findAll();
        //check list has 2 objects
        assertThat(staffList.size()).isEqualTo(2);
        Set<Long> staffIds = staffList.stream()
                .map(Staff::getId)
                .collect(Collectors.toSet());


        //Check All staff ids exist in output
        assertThat(staffIds).contains(staff.getId(), staff1.getId());
    }

}
