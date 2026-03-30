package com.rcoem.filmrentalstore.dto;

import org.springframework.data.rest.core.config.Projection;
import com.rcoem.filmrentalstore.entities.Payment;
import java.time.LocalDateTime;

@Projection(name = "paymentView", types = { Payment.class })
public interface PaymentView {
    LocalDateTime getPaymentDate();   // date column
    StaffSummary getStaff();          // staff who made entry
    Double getAmount();               // amount column

    interface StaffSummary {
        Long getId();                 // staff_id column
    }
}