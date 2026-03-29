package com.rcoem.filmrentalstore.repository;

import com.rcoem.filmrentalstore.entities.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RepositoryRestResource(path = "staffs")
public interface StaffRepository extends JpaRepository<Staff, Long> {

    // find all staff belonging to a specific store
    List<Staff> findByStore_StoreId(@Param("storeId") Long storeId);

}