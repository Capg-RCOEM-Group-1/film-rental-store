package com.rcoem.filmrentalstore.repository;
import com.rcoem.filmrentalstore.entities.Staff;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class StaffRepositoryTest {

    @Autowired
    private StaffRepository staffRepository;

    private final Pageable firstPage = PageRequest.of(0, 10);

    @Test
    @DisplayName("Should return active staff members with pagination (Sakila Default)")
    public void testFindByActiveTrue() {
        // Sakila usually starts with 2 staff members, both active.
        Page<Staff> activeStaffPage = staffRepository.findByActive(true,firstPage);

        // Then
        assertThat(activeStaffPage.getContent()).isNotEmpty();
        assertThat(activeStaffPage.getContent().get(0).getActive()).isTrue();
    }

    @Test
    @DisplayName("Should return inactive staff members (Handling empty results)")
    public void testFindByActiveFalse() {
        // When
        Page<Staff> inactiveStaffPage = staffRepository.findByActive(false,firstPage);

        // In a fresh Sakila DB, there are usually 0 inactive staff.
        // If your repo works, it should return an empty content list, not null.
        assertThat(inactiveStaffPage).isNotNull();
        assertThat(inactiveStaffPage.getNumber()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should handle multiple pages by limiting size")
    public void testPaginationLimits() {
        // Given: Create a page size of 1
        Pageable smallPage = PageRequest.of(0, 1);

        // When
        Page<Staff> result = staffRepository.findAll(smallPage);

        // Then
        assertThat(result.getContent()).hasSize(1);
        // Sakila typically has 2 staff members total
        assertThat(result.getTotalElements()).isGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("Should reflect status change when staff is deactivated")
    public void testStatusChangeReflectedInPagination() {
        // 1. Grab an existing active staff member
        Staff staff = staffRepository.findByActive(true,PageRequest.of(0, 1))
                .getContent().get(0);

        // 2. Deactivate them
        staff.setActive(false);
        staffRepository.saveAndFlush(staff);

        // 3. Verify they show up in the inactive query
        Page<Staff> inactiveStaff = staffRepository.findByActive(false,firstPage);

        assertThat(inactiveStaff.getContent())
                .extracting(Staff::getStaffId)
                .contains(staff.getStaffId());
    }

    @Test
    @DisplayName("Should correctly count total pages based on size")
    public void testTotalPagesCalculation() {
        // Given we have at least 2 staff in Sakila
        long totalStaff = staffRepository.count();
        int pageSize = 1;
        Pageable pageable = PageRequest.of(0, pageSize);

        // When
        Page<Staff> result = staffRepository.findAll(pageable);

        // Then
        assertThat(result.getTotalPages()).isEqualTo((int) Math.ceil((double) totalStaff / pageSize));
    }
}