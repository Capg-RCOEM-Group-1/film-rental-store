package com.rcoem.filmrentalstore.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
@Setter
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;

    @UpdateTimestamp
    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private Timestamp lastUpdate;


    public Store() {
    }

    public Store(Long storeId) {
        this.storeId = storeId;
    }

    @OneToMany
    private List<Staff> staffs;

    @OneToMany
    private List<Inventory> inventories;

    @OneToOne
    @JoinColumn(name = "manager_staff_id")
    private Staff manager;
}
