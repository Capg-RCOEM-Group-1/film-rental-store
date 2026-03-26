
package com.rcoem.filmrentalstore.StoreAPITesting;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class StoreAPITest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void testGetAllStores() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/stores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.stores").exists());
    }
}