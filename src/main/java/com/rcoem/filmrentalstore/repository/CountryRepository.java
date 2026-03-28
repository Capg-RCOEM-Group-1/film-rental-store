package com.rcoem.filmrentalstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rcoem.filmrentalstore.entities.Country;

public interface CountryRepository extends JpaRepository<Country,Long>{

}
