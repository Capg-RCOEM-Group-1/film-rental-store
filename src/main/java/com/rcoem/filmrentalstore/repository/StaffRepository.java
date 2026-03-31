package com.rcoem.filmrentalstore.repository;


import com.rcoem.filmrentalstore.dto.FilmView;
import com.rcoem.filmrentalstore.entities.Staff;
import com.rcoem.filmrentalstore.entities.StaffView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(excerptProjection = StaffView.class)
public interface StaffRepository extends JpaRepository<Staff, Byte> {

    // Fetches only required columns for active staff
    Page<Staff> findByActiveTrue(Pageable pageable);

    // Fetches only required columns for inactive staff
    Page<Staff> findByActiveFalse(Pageable pageable);
}
