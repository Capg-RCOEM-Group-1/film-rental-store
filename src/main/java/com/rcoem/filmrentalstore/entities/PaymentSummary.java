package com.rcoem.filmrentalstore.entities;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.time.LocalDateTime;

@Projection(name = "paymentSummary", types = { Payment.class })
public interface PaymentSummary {
    Double getAmount();
    LocalDateTime getPaymentDate();

    // Traverses Payment -> Customer to get the full name
    @Value("#{target.customer.firstName + ' ' + target.customer.lastName}")
    String getCustomerName();
}