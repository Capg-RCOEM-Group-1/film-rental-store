package com.rcoem.filmrentalstore.repository;


import com.rcoem.filmrentalstore.dto.FilmView;
import com.rcoem.filmrentalstore.entities.Staff;
import com.rcoem.filmrentalstore.entities.StaffView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(excerptProjection = StaffView.class)
public interface StaffRepository extends JpaRepository<Staff, Byte> {

    // Required for /staffs/search/findByActive
    Page<Staff> findByActive(@Param("active") Boolean active, Pageable pageable);

    // Required for your search functionality
    Page<Staff> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrUsernameContainingIgnoreCase(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("email") String email,
            @Param("username") String username,
            Pageable pageable
    );
}
