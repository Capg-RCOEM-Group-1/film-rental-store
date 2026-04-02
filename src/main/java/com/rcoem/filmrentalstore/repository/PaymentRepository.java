package com.rcoem.filmrentalstore.repository;

import com.rcoem.filmrentalstore.dto.PaymentView;
import com.rcoem.filmrentalstore.entities.Payment;
import com.rcoem.filmrentalstore.entities.PaymentSummary;
import com.rcoem.filmrentalstore.entities.StaffView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.time.LocalDateTime;
import java.util.List;



@RepositoryRestResource(excerptProjection = PaymentSummary.class)
public interface PaymentRepository extends JpaRepository<Payment, Short> {

   /* List<PaymentView> findByStaff_Store_StoreId(
        @Param("storeId") Byte storeId
    );*/

    List<PaymentView> findByStaff_Store_StoreIdAndPaymentDate(
        @Param("storeId") Byte storeId,
        @Param("paymentDate") LocalDateTime paymentDate
    );

    @RestResource(path = "findPaymentsByStaff_Store_StoreId")
    List<Payment> findPaymentsByStaff_Store_StoreId(
        @Param("storeId") Byte storeId
    );

    @RestResource(path = "findPaymentsByStaff_Store_StoreIdAndPaymentDate")
    List<Payment> findPaymentsByStaff_Store_StoreIdAndPaymentDate(
        @Param("storeId") Byte storeId,
        @Param("paymentDate") LocalDateTime paymentDate
    );

    List<Payment> findByStaff_Username(
        @Param("username") String username
    );
}