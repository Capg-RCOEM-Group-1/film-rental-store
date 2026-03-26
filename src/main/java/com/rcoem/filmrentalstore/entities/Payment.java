package com.rcoem.filmrentalstore.entities;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Payment {

    private Double amount;

    @CreationTimestamp
    private LocalDateTime date;

    @UpdateTimestamp
    @Column(name = "last_update", columnDefinition = "TIMESTAMP")
    private Timestamp last_update;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;
}
