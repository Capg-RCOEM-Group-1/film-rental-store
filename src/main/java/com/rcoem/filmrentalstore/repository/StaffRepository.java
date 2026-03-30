package com.rcoem.filmrentalstore.repository;


import com.rcoem.filmrentalstore.entities.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {

    // Fetches only required columns for active staff
    List<Staff> findByActiveTrue();

    // Fetches only required columns for inactive staff
    List<Staff> findByActiveFalse();
}
