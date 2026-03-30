package com.rcoem.filmrentalstore.repository;

import com.rcoem.filmrentalstore.entities.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PaymentRepository extends JpaRepository<Payment, Short> {
    Page<Payment> findByStaff_Username(@Param("username") String username, Pageable pageable);
}
