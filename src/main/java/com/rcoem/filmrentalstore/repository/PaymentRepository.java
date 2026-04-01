// package com.rcoem.filmrentalstore.repository;

// import com.rcoem.filmrentalstore.dto.PaymentView;
// import com.rcoem.filmrentalstore.entities.Payment;
// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.repository.query.Param;
// import org.springframework.data.rest.core.annotation.RepositoryRestResource;

// import java.time.LocalDateTime;
// import java.util.List;

// @RepositoryRestResource(path = "payments", excerptProjection = PaymentView.class)
// public interface PaymentRepository extends JpaRepository<Payment, Short> {

//     List<PaymentView> findByStaff_Store_StoreId(
//         @Param("storeId") Byte storeId
//     );

//     List<PaymentView> findByStaff_Store_StoreIdAndPaymentDate(
//         @Param("storeId") Byte storeId,
//         @Param("paymentDate") LocalDateTime paymentDate
//     );

//     // FIXED: return List<Payment> not List<PaymentView>
//     // Spring Data REST will apply excerptProjection automatically
//     List<Payment> findByStaff_Username(
//         @Param("username") String username
//     );
// }
// package com.rcoem.filmrentalstore.repository;

// import com.rcoem.filmrentalstore.dto.PaymentView;
// import com.rcoem.filmrentalstore.entities.Payment;
// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.repository.query.Param;
// import org.springframework.data.rest.core.annotation.RepositoryRestResource;

// import java.time.LocalDateTime;
// import java.util.List;

// @RepositoryRestResource(path = "payments", excerptProjection = PaymentView.class)
// public interface PaymentRepository extends JpaRepository<Payment, Short> {

//     // EXISTING (used for projection)
//     List<PaymentView> findByStaff_Store_StoreId(
//         @Param("storeId") Byte storeId
//     );

//     // EXISTING (used for projection)
//     List<PaymentView> findByStaff_Store_StoreIdAndPaymentDate(
//         @Param("storeId") Byte storeId,
//         @Param("paymentDate") LocalDateTime paymentDate
//     );

//     // REQUIRED for PaymentApiTest
//     List<Payment> findPaymentsByStaff_Store_StoreId(
//         @Param("storeId") Byte storeId
//     );

//     // REQUIRED for PaymentApiTest
//     List<Payment> findPaymentsByStaff_Store_StoreIdAndPaymentDate(
//         @Param("storeId") Byte storeId,
//         @Param("paymentDate") LocalDateTime paymentDate
//     );

//     // REQUIRED for PaymentSearchApiTest
//     List<Payment> findByStaff_Username(
//         @Param("username") String username
//     );
// }
package com.rcoem.filmrentalstore.repository;

import com.rcoem.filmrentalstore.dto.PaymentView;
import com.rcoem.filmrentalstore.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.time.LocalDateTime;
import java.util.List;

@RepositoryRestResource(path = "payments", excerptProjection = PaymentView.class)
public interface PaymentRepository extends JpaRepository<Payment, Short> {

    List<PaymentView> findByStaff_Store_StoreId(
        @Param("storeId") Byte storeId
    );

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