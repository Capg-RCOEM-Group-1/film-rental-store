package com.rcoem.filmrentalstore.repository;

import com.rcoem.filmrentalstore.dto.StoreSummary;
import com.rcoem.filmrentalstore.entities.Address;
import com.rcoem.filmrentalstore.entities.Staff;
import com.rcoem.filmrentalstore.entities.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "stores", excerptProjection = StoreSummary.class)
public interface StoreRepository extends JpaRepository<Store, Byte> {

    List<Store> findByAddress(Address address);

    List<Store> findByManager(Staff manager);
}
