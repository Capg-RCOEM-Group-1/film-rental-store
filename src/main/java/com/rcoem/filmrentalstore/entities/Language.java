package com.rcoem.filmrentalstore.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "language_id")
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    @Column(name = "last_update", columnDefinition = "TIMESTAMP",nullable = false)
    @UpdateTimestamp
    private LocalDateTime lastUpdate;
    @OneToMany(mappedBy = "language")
    List<Film> films;
    @OneToMany(mappedBy = "originalLanguage")
    List<Film> originalLanguageFilms;
}
