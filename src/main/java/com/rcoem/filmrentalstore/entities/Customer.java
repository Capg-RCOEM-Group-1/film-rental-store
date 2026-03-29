package com.rcoem.filmrentalstore.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long customerId;

    @NotNull
    @Column(nullable = false, length = 45)
    private String firstName;

    @NotNull
    @Column(nullable = false, length = 45)
    private String lastName;

    @Email
    @Column(unique = true, length = 50)
    private String email;

    @Column( nullable = false)
    private Boolean active = true;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime createDate;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime lastUpdate;

//    @ManyToOne(optional = false) // optional=false enforces the NOT NULL at JPA level
//    @JoinColumn(nullable = false)

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

//    @ManyToOne(optional = false)
//    @JoinColumn(nullable = false)
    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    public Customer() {
    }

    public Customer(String firstName, String lastName, String email, Store store, Address address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.store = store;
        this.address = address;
        this.active = true;
    }
}