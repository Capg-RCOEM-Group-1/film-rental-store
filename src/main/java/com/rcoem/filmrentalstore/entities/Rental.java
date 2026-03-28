package com.rcoem.filmrentalstore.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentalId;

    @CreationTimestamp
    @Column( nullable = false, updatable = false)
    private Timestamp rentalDate;
    @Column(nullable = false)
    private Timestamp returnDate;

    @UpdateTimestamp
    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private Timestamp lastUpdate;

    @ManyToOne()
    @JoinColumn(name = "inventory_id",nullable = false)
    private Inventory inventory;

    @ManyToOne()
    @JoinColumn(name = "customer_id",nullable = false)
    private Customer customer;

    @ManyToOne()
    @JoinColumn(name = "staff_id",nullable = false)
    private Staff staff;

    public Rental(Timestamp returnDate, Timestamp lastUpdate) {
        this.returnDate = returnDate;
        this.lastUpdate = lastUpdate;
    }

}
