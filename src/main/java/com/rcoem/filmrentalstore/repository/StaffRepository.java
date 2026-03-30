package com.rcoem.filmrentalstore.repository;


import com.rcoem.filmrentalstore.entities.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Byte> {

    // Fetches only required columns for active staff
    Page<Staff> findByActiveTrue(Pageable pageable);

    // Fetches only required columns for inactive staff
    Page<Staff> findByActiveFalse(Pageable pageable);
}
