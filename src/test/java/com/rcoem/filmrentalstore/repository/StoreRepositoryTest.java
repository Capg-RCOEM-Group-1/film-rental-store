package com.rcoem.filmrentalstore.repository;

import com.rcoem.filmrentalstore.entities.Store;
import com.rcoem.filmrentalstore.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.security.PublicKey;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StoreRepositoryTest {
    @Autowired
    StoreRepository storeRepository;

    @BeforeEach
    public void clean(){
        storeRepository.deleteAll();
    }

    @Test
    public void testSaveStore(){
        Store store = new Store();
        Store savedStore = storeRepository.save(store);
        Optional<Store> found = storeRepository.findById(savedStore.getStoreId());
        assertThat(found).isPresent();
    }

}
