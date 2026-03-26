package com.rcoem.filmrentalstore.entities;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Film {

     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long filmId;
    private String title;


}
