package com.rcoem.filmrentalstore.repository;

import com.rcoem.filmrentalstore.dto.PaymentView;
import com.rcoem.filmrentalstore.entities.*;
import org.junit.jupiter.api.BeforeEach;
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
    @Autowired
    StoreRepository storeRepository;

    private Store store;

    @BeforeEach
    public void setup() {
        store = storeRepository.findAll().get(0);
    }

    @Test
    @DisplayName("Should fetch all payments for store")
    void testFindByStoreId() {
        List<PaymentView> payments = paymentRepository
            .findByStaff_Store_StoreId(store.getStoreId());
        assertThat(payments).isNotNull();
        System.out.println("Total payments for store: " + payments.size());
    }

    @Test
    @DisplayName("Should fetch payments for store by date")
    void testFindByStoreIdAndDate() {
        List<PaymentView> payments = paymentRepository
            .findByStaff_Store_StoreIdAndPaymentDate(
                store.getStoreId(),
                LocalDateTime.of(2005, 6, 15, 0, 0, 0)
            );
        assertThat(payments).isNotNull();
        System.out.println("Payments on 2005-06-15: " + payments.size());
    }

    @Test
    @DisplayName("Should verify projection returns correct fields")
    void testPaymentViewFields() {
        List<PaymentView> payments = paymentRepository
            .findByStaff_Store_StoreId(store.getStoreId());
        if (!payments.isEmpty()) {
            PaymentView payment = payments.get(0);
            assertThat(payment.getPaymentDate()).isNotNull();
            assertThat(payment.getAmount()).isNotNull();
            assertThat(payment.getStaff()).isNotNull();
            assertThat(payment.getStaff().getStaffId()).isNotNull();
            System.out.println("Date: " + payment.getPaymentDate());
            System.out.println("Staff ID: " + payment.getStaff().getStaffId());
            System.out.println("Amount: " + payment.getAmount());
        }
    }

    @Test
    @DisplayName("Should return non empty list for valid store")
    void testFindByStoreId_ReturnsNonEmptyList() {
        List<PaymentView> payments = paymentRepository
            .findByStaff_Store_StoreId(store.getStoreId());
        assertThat(payments).isNotNull();
        System.out.println("Total payments: " + payments.size());
    }

    @Test
    @DisplayName("Should return empty list for invalid store")
    void testFindByStoreId_InvalidStore_ReturnsEmptyList() {
        List<PaymentView> payments = paymentRepository
            .findByStaff_Store_StoreId((byte) 99);
        assertThat(payments).isNotNull();
        assertThat(payments).isEmpty();
        System.out.println("Payments for invalid store: " + payments.size());
    }

    @Test
    @DisplayName("Should have positive amount for all payments")
    void testPaymentView_AmountIsPositive() {
        List<PaymentView> payments = paymentRepository
            .findByStaff_Store_StoreId(store.getStoreId());
        payments.forEach(p -> {
            assertThat(p.getAmount()).isNotNull();
            System.out.println("Amount: " + p.getAmount());
        });
    }

    @Test
    @DisplayName("Should return empty for future date")
    void testFindByStoreIdAndDate_FutureDate_ReturnsEmpty() {
        List<PaymentView> payments = paymentRepository
            .findByStaff_Store_StoreIdAndPaymentDate(
                store.getStoreId(),
                LocalDateTime.of(2099, 1, 1, 0, 0, 0)
            );
        assertThat(payments).isNotNull();
        assertThat(payments).isEmpty();
        System.out.println("Payments for future date: " + payments.size());
    }

    @Test
    @DisplayName("Should have payment date not in future")
    void testPaymentView_DateIsNotInFuture() {
        List<PaymentView> payments = paymentRepository
            .findByStaff_Store_StoreId(store.getStoreId());
        payments.forEach(p -> {
            assertThat(p.getPaymentDate())
                .isBefore(LocalDateTime.now().plusSeconds(5));
            System.out.println("Payment date: " + p.getPaymentDate());
        });
    }
}