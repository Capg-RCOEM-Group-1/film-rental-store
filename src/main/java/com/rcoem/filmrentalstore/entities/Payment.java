package com.rcoem.filmrentalstore.entities;

import jakarta.persistence.*;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "DECIMAL",nullable = false)
    private Double amount;

    @CreationTimestamp
    private LocalDateTime paymentDate;

    @UpdateTimestamp
    @Column(name = "last_update", columnDefinition = "TIMESTAMP")
    private Timestamp last_update;

    @ManyToOne
    @JoinColumn(name = "staff_id",nullable = false)
    private Staff staff;

    @ManyToOne
    @JoinColumn(name = "rental_id")
    private Rental rental;

    @ManyToOne
    @JoinColumn(name = "customer_id",nullable = false)
    private Customer customer;
}
