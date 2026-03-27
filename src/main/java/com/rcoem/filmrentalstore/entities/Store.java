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

    @OneToOne
    @JoinColumn(name = "manager_staff_id")
    @Column(nullable = false)
    private Staff manager;

    @UpdateTimestamp
    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private Timestamp lastUpdate;


    @OneToOne(optional = false)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;


    @OneToMany
    private List<Staff> staffs;

    @OneToMany
    private List<Inventory> inventories;



    public Store() {
    }

    public Store(Long storeId) {
        this.storeId = storeId;
    }

}
