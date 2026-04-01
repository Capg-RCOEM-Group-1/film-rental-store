package com.rcoem.filmrentalstore.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.UpdateTimestamp;
import org.locationtech.jts.geom.Point;

@Entity
@Table(name = "address")
@Getter
@Setter

public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Short addressId;

    @Column(name = "address")
    private String address;

    @Column(name = "address2", nullable = true)
    private String address2;

    @Column(name = "district")
    private String district;

    @Column(name = "postal_code", nullable = true)
    private String postalCode;

    @Column(name = "phone")
    private String phone;

    @NotNull
    @JsonIgnore
    @Column(name = "location", columnDefinition = "GEOMETRY")
    private Point location;

    @UpdateTimestamp
    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private Timestamp lastUpdate;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    @OneToMany(mappedBy = "address")
    private List<Customer> customers;

    @OneToMany(mappedBy = "address")
    private List<Staff> staffs;
}
