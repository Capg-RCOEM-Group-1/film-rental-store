package com.rcoem.filmrentalstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.rcoem.filmrentalstore.entities.Actor;

@RepositoryRestResource(path = "actors")
public interface ActorRepository extends JpaRepository<Actor,Long>{

}
