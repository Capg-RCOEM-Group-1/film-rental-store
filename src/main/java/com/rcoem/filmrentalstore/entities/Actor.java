package com.rcoem.filmrentalstore.entities;

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "actor")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Actor {

    Long actorId;
    String firstName;
    String lastName;
    Timestamp lastUpdate;
    



}
