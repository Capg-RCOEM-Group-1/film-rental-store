package com.rcoem.filmrentalstore.repository;

import com.rcoem.filmrentalstore.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByFirstName(String firstName);
    List<Customer> findByLastName(String lastName);
    Optional<Customer> findByEmail(String email);
    List<Customer> findByActive(Boolean status);
    List<Customer> findByCreateDate(LocalDateTime createDate);

}
