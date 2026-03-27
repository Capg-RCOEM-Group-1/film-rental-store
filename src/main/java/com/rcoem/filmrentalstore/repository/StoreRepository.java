package com.rcoem.filmrentalstore.repository;
<<<<<<< HEAD

import com.rcoem.filmrentalstore.entities.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store,Long> {
}
=======
import com.rcoem.filmrentalstore.entities.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

}
>>>>>>> 68dfbfa59dfdae07223b898a51bf2d4304cf9a28
