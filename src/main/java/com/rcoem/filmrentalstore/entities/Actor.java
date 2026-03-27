package com.rcoem.filmrentalstore.entities;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "actor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Actor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long actorId;

    private String firstName;
    private String lastName;

    @UpdateTimestamp
    @Column(nullable = false)
    private Timestamp lastUpdate;

    @ManyToMany(mappedBy = "actors")
    @JsonIgnore
    private List<Film> films;
}
