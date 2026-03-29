package com.rcoem.filmrentalstore.repository;

import com.rcoem.filmrentalstore.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.sql.Timestamp;
import java.util.List;

@RepositoryRestResource(path = "payments")
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // get payments by staff filtered by last_update
    @Query("SELECT p FROM Payment p WHERE p.staff.id = :staffId AND p.last_update > :lastUpdate")
    List<Payment> findByStaffIdAndLastUpdateAfter(
        @Param("staffId") Long staffId,
        @Param("lastUpdate") Timestamp lastUpdate
    );

    // get all payments for a store via staff
    @Query("SELECT p FROM Payment p WHERE p.staff.store.storeId = :storeId")
    List<Payment> findPaymentsByStoreId(@Param("storeId") Long storeId);

    // get payments for a store filtered by last_update
    @Query("SELECT p FROM Payment p WHERE p.staff.store.storeId = :storeId AND p.last_update >= :lastUpdate")
    List<Payment> findByStoreIdAndLastUpdateAfter(
        @Param("storeId") Long storeId,
        @Param("lastUpdate") Timestamp lastUpdate
    );

}