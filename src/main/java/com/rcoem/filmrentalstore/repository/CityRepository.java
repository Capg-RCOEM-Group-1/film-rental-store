package com.rcoem.filmrentalstore.repository;

import com.rcoem.filmrentalstore.entities.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Short> {

}