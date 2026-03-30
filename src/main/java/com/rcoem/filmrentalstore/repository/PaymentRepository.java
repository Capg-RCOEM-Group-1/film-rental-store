package com.rcoem.filmrentalstore.repository;

import com.rcoem.filmrentalstore.dto.PaymentView;
import com.rcoem.filmrentalstore.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDateTime;
import java.util.List;

@RepositoryRestResource(path = "payments")
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // all payments for a store
    List<PaymentView> findByStaff_Store_StoreId(
        @Param("storeId") Long storeId
    );

    // payments for a store filtered by date
    List<PaymentView> findByStaff_Store_StoreIdAndPaymentDate(
        @Param("storeId") Long storeId,
        @Param("paymentDate") LocalDateTime paymentDate
    );
}