package com.rcoem.filmrentalstore.repository;

import com.rcoem.filmrentalstore.dto.CustomerDetails;
import com.rcoem.filmrentalstore.entities.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource(excerptProjection = CustomerDetails.class)
public interface CustomerRepository extends JpaRepository<Customer, Short> {
    Page<Customer> findByFirstName(String firstName, Pageable pageable);
    Page<Customer> findByLastName(String lastName, Pageable pageable);
    Optional<Customer> findByEmail(String email);
    Page<Customer> findByActive(Boolean status, Pageable pageable);
    Page<Customer> findByCreateDate(LocalDateTime createDate, Pageable pageable);

    @RestResource(path = "search-all", rel = "search-all")
    @Query("SELECT c FROM Customer c LEFT JOIN c.address a WHERE " +
            "LOWER(c.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(CONCAT(c.firstName, ' ', c.lastName)) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(a.address) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(a.district) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(a.postalCode) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Customer> searchGlobal(@Param("keyword") String keyword, Pageable pageable);
}
