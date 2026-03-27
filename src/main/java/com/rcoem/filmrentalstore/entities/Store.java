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
    @Column(nullable = false)
    private Long storeId;

    @UpdateTimestamp
    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private Timestamp lastUpdate;

    @OneToOne
    @JoinColumn(name = "manager_staff_id",nullable = false)
    private Staff manager;

    @OneToOne(optional = false)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    public Store() {
    }

    public Store(Long storeId) {
        this.storeId = storeId;
    }

    @OneToMany(mappedBy = "store")
    private List<Staff> staffs;

    @OneToMany(mappedBy = "store")
    private List<Inventory> inventories;


}
