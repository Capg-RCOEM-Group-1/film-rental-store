package com.rcoem.filmrentalstore.repository;

import com.rcoem.filmrentalstore.dto.CustomerDetails;
import com.rcoem.filmrentalstore.entities.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource(excerptProjection = CustomerDetails.class)
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Page<Customer> findByFirstName(String firstName, Pageable pageable);
    Page<Customer> findByLastName(String lastName, Pageable pageable);

    //email is unique it will only ever return 1 or 0 records.
    Optional<Customer> findByEmail(String email);
    Page<Customer> findByActive(Boolean status, Pageable pageable);
    Page<Customer> findByCreateDate(LocalDateTime createDate, Pageable pageable);

}
