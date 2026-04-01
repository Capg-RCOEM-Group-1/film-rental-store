package com.rcoem.filmrentalstore.dto;

import org.springframework.data.rest.core.config.Projection;
import com.rcoem.filmrentalstore.entities.Payment;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Projection(name = "paymentView", types = { Payment.class })
public interface PaymentView {
    LocalDateTime getPaymentDate();
    BigDecimal getAmount();
    StaffSummary getStaff();

    interface StaffSummary {
        Byte getStaffId();
    }
}