package com.rcoem.filmrentalstore.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    private Byte storeId;

    @UpdateTimestamp
    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private Timestamp lastUpdate;


    public Store() {
    }

    public Store(Byte storeId) {
        this.storeId = storeId;
    }

    @OneToMany(mappedBy = "store",fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Staff> staffs;

    @OneToMany(mappedBy = "store",fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Inventory> inventories;


    @OneToOne
    @JoinColumn(name = "manager_staff_id")
    private Staff manager;

    @NotNull
    @OneToOne
    @JoinColumn(name = "address_id",nullable = false)
    private Address address;
}
