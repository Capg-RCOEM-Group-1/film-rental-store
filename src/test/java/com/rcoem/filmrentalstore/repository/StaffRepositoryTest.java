package com.rcoem.filmrentalstore.repository;

import com.rcoem.filmrentalstore.entities.Staff;
import com.rcoem.filmrentalstore.entities.StaffView;
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

    @BeforeEach
    public void setup() {
        staffRepository.deleteAll();
        // Given: An active staff member exists
        Staff activeStaff = new Staff();
        activeStaff.setFirstName("John");
        activeStaff.setLastName("Doe");
        activeStaff.setActive(true);
        activeStaff.setPassword("pass123");
        // Given: An not active staff member exists
        Staff inactiveStaff = new Staff();
        inactiveStaff.setFirstName("Jane");
        inactiveStaff.setLastName("Smith");
        inactiveStaff.setActive(false);
        inactiveStaff.setPassword("pass456");

        staffRepository.saveAll(List.of(activeStaff, inactiveStaff));
    }


    @Test
    @DisplayName("Should return only active staff members")
    public void testFindByActiveTrue() {
        // When
        List<Staff> activeStaff = staffRepository.findByActiveTrue();

        // Then
        assertThat(activeStaff).hasSize(1);
        assertThat(activeStaff.get(0).getFirstName()).isEqualTo("John");
        assertThat(activeStaff.get(0).getLastName()).isEqualTo("Doe");
    }

    @Test
    @DisplayName("Should return only inactive staff members")
    public void testFindByActiveFalse() {
        // When
        List<Staff> inactiveStaff = staffRepository.findByActiveFalse();

        // Then
        assertThat(inactiveStaff).hasSize(1);
        assertThat(inactiveStaff.get(0).getFirstName()).isEqualTo("Jane");
        assertThat(inactiveStaff.get(0).getLastName()).isEqualTo("Smith");
    }

    @Test
    @DisplayName("Should return empty list when no active staff exist")
    public void testFindByActiveTrue_Empty() {
        // Given: Clear all data first
        staffRepository.deleteAll();

        // When
        List<Staff> result = staffRepository.findByActiveTrue();

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should return multiple records when more than one staff is active")
    public void testFindByActiveTrue_Multiple() {
        // Given: Add another active staff member
        Staff secondActive = new Staff();
        secondActive.setFirstName("Jane");
        secondActive.setLastName("Wonderland");
        secondActive.setPassword("securePass789");
        secondActive.setActive(true);
        staffRepository.save(secondActive);

        // When
        List<Staff> result = staffRepository.findByActiveTrue();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Staff::getFirstName)
                .containsExactlyInAnyOrder("John", "Jane");
    }

    @Test
    @DisplayName("Should reflect status change when staff is deactivated")
    public void testStatusChangeReflectedInProjection() {
        // Given: Find the active staff saved in @BeforeEach and deactivate them
        // Note: You may need a findByFirstName or similar in your Repo to grab the specific entity
        Staff john = staffRepository.findAll().stream()
                .filter(s -> s.getFirstName().equals("John"))
                .findFirst().get();

        john.setActive(false);
        staffRepository.saveAndFlush(john); // Force the update to DB

        // When
        List<Staff> activeStaff = staffRepository.findByActiveTrue();
        List<Staff> inactiveStaff = staffRepository.findByActiveFalse();

        // Then
        assertThat(activeStaff).isEmpty();
        assertThat(inactiveStaff).hasSize(2); // Jane + the newly deactivated John
    }

}
