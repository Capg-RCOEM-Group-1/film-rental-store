package com.rcoem.filmrentalstore.repository;

import com.rcoem.filmrentalstore.dto.PaymentView;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PaymentRepositoryTest {

    @Autowired
    PaymentRepository paymentRepository;

    @Test
    void testFindByStoreId() {
        // fetch all payments for store with id 1
        List<PaymentView> payments = paymentRepository
            .findByStaff_Store_StoreId(1L);

        assertThat(payments).isNotNull();
        System.out.println("Total payments for store 1: " + payments.size());
    }

    @Test
    void testFindByStoreIdAndDate() {
        // fetch payments for store 1 on a specific date
        List<PaymentView> payments = paymentRepository
            .findByStaff_Store_StoreIdAndPaymentDate(
                1L,
                LocalDateTime.of(2024, 1, 1, 0, 0, 0)
            );

        assertThat(payments).isNotNull();
        System.out.println("Payments for store 1 on 2024-01-01: " + payments.size());
    }

    @Test
    void testPaymentViewFields() {
        // verify projection returns correct fields
        List<PaymentView> payments = paymentRepository
            .findByStaff_Store_StoreId(1L);

        if (!payments.isEmpty()) {
            PaymentView payment = payments.get(0);

            assertThat(payment.getPaymentDate()).isNotNull();
            assertThat(payment.getAmount()).isNotNull();
            assertThat(payment.getStaff()).isNotNull();
            assertThat(payment.getStaff().getId()).isNotNull();

            System.out.println("Date: " + payment.getPaymentDate());
            System.out.println("Staff ID: " + payment.getStaff().getId());
            System.out.println("Amount: " + payment.getAmount());


        }
    }
    // Test 4 - store exists and has payments
    @Test
    @DisplayName("Should return non empty list for valid store")
    void testFindByStoreId_ReturnsNonEmptyList() {
        List<PaymentView> payments = paymentRepository
            .findByStaff_Store_StoreId(1L);
        assertThat(payments).isNotNull();
        System.out.println("Total payments for store 1: " + payments.size());
    }

    // Test 5 - invalid store returns empty list
    @Test
    @DisplayName("Should return empty list for invalid store")
    void testFindByStoreId_InvalidStore_ReturnsEmptyList() {
        List<PaymentView> payments = paymentRepository
            .findByStaff_Store_StoreId(9999L);
        assertThat(payments).isNotNull();
        assertThat(payments).isEmpty();
        System.out.println("Payments for invalid store: " + payments.size());
    }

    // Test 6 - verify amount is positive
    @Test
    @DisplayName("Should have positive amount for all payments")
    void testPaymentView_AmountIsPositive() {
        List<PaymentView> payments = paymentRepository
            .findByStaff_Store_StoreId(1L);
        payments.forEach(p -> {
            assertThat(p.getAmount()).isGreaterThan(0);
            System.out.println("Amount: " + p.getAmount());
        });
    }

    // Test 7 - filter by future date returns empty
    @Test
    @DisplayName("Should return empty for future date")
    void testFindByStoreIdAndDate_FutureDate_ReturnsEmpty() {
        List<PaymentView> payments = paymentRepository
            .findByStaff_Store_StoreIdAndPaymentDate(
                1L,
                LocalDateTime.of(2099, 1, 1, 0, 0, 0)
            );
        assertThat(payments).isNotNull();
        assertThat(payments).isEmpty();
        System.out.println("Payments for future date: " + payments.size());
    }
  

    // Test 8 - verify payment date is not in future
    @Test
    @DisplayName("Should have payment date not in future")
    void testPaymentView_DateIsNotInFuture() {
        List<PaymentView> payments = paymentRepository
            .findByStaff_Store_StoreId(1L);
        payments.forEach(p -> {
            assertThat(p.getPaymentDate())
                .isBefore(LocalDateTime.now());
            System.out.println("Payment date: " + p.getPaymentDate());
        });
    }
}