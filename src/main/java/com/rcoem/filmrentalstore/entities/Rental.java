package com.rcoem.filmrentalstore.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentalId;

    @CreationTimestamp
    @Column(columnDefinition = "TIMESTAMP", nullable = false, updatable = false)
    private Timestamp rentalDate;

    @Column(columnDefinition = "TIMESTAMP")
    private Timestamp returnDate;

    @ManyToOne
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;

    public Rental() {
    }

    public Rental(Timestamp returnDate, Inventory inventory, Customer customer, Staff staff) {
        this.returnDate = returnDate;
        this.inventory = inventory;
        this.customer = customer;
        this.staff = staff;
    }

}
