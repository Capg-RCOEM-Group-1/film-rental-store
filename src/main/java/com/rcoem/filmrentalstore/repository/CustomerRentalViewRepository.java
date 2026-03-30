package com.rcoem.filmrentalstore.repository;

import com.rcoem.filmrentalstore.entities.CustomerRentalView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(path = "customer-rentals")
public interface CustomerRentalViewRepository extends Repository<CustomerRentalView, Integer> {

    // Spring Data automatically generates the WHERE customer_id = ? query based on the method name
    @RestResource(path = "by-customer")
    Page<CustomerRentalView> findByCustomerId(@Param("customerId") Short customerId, Pageable pageable);
}