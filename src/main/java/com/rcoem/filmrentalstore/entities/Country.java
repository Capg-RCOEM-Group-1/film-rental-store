package com.rcoem.filmrentalstore.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short countryId;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    @UpdateTimestamp
    private LocalDateTime lastUpdate;
    @OneToMany(mappedBy = "country")
    List<City> cities;
}
