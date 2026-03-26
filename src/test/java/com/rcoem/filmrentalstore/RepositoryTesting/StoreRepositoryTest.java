package com.rcoem.filmrentalstore.RepositoryTesting;

import com.rcoem.filmrentalstore.entities.Store;
import com.rcoem.filmrentalstore.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

// @DataJpaTest
@SpringBootTest 
public class StoreRepositoryTest {

    @Autowired
    StoreRepository storeRepository;

    @BeforeEach
    public void clean() {
        storeRepository.deleteAll();
    }

    @Test
    public void test() {
        Store store = new Store( "AK Films");

        Store saved = storeRepository.save(store);
        Optional<Store> store1 = storeRepository.findById(saved.getId());

        assertThat(store1).isPresent();
        assertThat(store1.get().getStoreName()).isEqualTo("AK Films");
    }
}