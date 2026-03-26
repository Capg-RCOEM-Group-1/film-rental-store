
package com.rcoem.filmrentalstore.StoreRepositoryTesting;

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

//    @Test
//    public void testStoreId(){
//        Store store = new Store(null , "AK Films");
//        storeRepository.save(store);
//        Optional<Store> store1 = storeRepository.findById(store.getId());
//        assertThat(store1).isPresent();
//        assertThat(store1.get().getStore_name()).isEqualTo("AK Films");
//    }

}
