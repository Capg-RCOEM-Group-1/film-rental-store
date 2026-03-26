package com.rcoem.filmrentalstore.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @Email
    private String email;

    @NotNull
    private Character active;

    @NotNull
    @CreationTimestamp
    @Column(updatable = false)
    private Date createDate;

    @NotNull
    @UpdateTimestamp
    @Column(columnDefinition = "TIMESTAMP")
    private Timestamp timestamp;

    public Customer() {
    }

    public Customer(String firstName, String lastName, String email, Character active, Date createDate, Timestamp timestamp) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.active = active;
        this.createDate = createDate;
        this.timestamp = timestamp;
    }



}
