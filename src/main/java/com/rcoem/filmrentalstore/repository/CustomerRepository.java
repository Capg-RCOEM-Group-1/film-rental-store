package com.rcoem.filmrentalstore.repository;

import com.rcoem.filmrentalstore.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<List<Customer>> findByFirstName(String firstName);
    Optional<List<Customer>> findByLastName(String lastName);
    Optional<Customer> findByEmail(String email);
    Optional<List<Customer>> findByActive(Character status);
    Optional<List<Customer>> findByCreateDate(Date date);

}
