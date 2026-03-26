<<<<<<<< HEAD:src/test/java/com/rcoem/filmrentalstore/StoreRepositoryTesting/StoreRepositoryTest.java
package com.rcoem.filmrentalstore.StoreRepositoryTesting;
========
package com.rcoem.filmrentalstore.repository;
>>>>>>>> 2e96c63bded1ba6715820a2b18c32e6290cd4a6f:src/test/java/com/rcoem/filmrentalstore/repository/StoreRepositoryTest.java

import com.rcoem.filmrentalstore.entities.Store;
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
    public void testStoreId(){
        Store store = new Store(null , "AK Films");
        storeRepository.save(store);
        Optional<Store> store1 = storeRepository.findById(store.getId());
        assertThat(store1).isPresent();
        assertThat(store1.get().getStore_name()).isEqualTo("AK Films");
    }

}
