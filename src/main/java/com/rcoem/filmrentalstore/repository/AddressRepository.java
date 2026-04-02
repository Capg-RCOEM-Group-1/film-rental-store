package com.rcoem.filmrentalstore.repository;

import com.rcoem.filmrentalstore.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource(path = "addresses")
public interface AddressRepository extends JpaRepository<Address, Short> {
}