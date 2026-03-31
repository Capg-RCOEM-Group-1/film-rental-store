package com.rcoem.filmrentalstore.api;

import com.rcoem.filmrentalstore.entities.Address;
import com.rcoem.filmrentalstore.entities.Staff;
import com.rcoem.filmrentalstore.entities.Store;
import com.rcoem.filmrentalstore.repository.AddressRepository;
import com.rcoem.filmrentalstore.repository.StaffRepository;
import com.rcoem.filmrentalstore.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class StoreAPITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    StoreRepository storeRepo;

    @Autowired
    StaffRepository staffRepo;

    @Autowired
    AddressRepository addressRepo;

    Address address;
    Staff staff;

    @BeforeEach
    void setup() {
        address = new Address();
        address.setAddressId((short) 1);

        staff = new Staff();
        staff.setStaffId((byte) 2);

    }

    // ---------------- Save Store Tests ----------------

    @Test
    void testSaveStore() throws Exception {
        String body = """
        {
            "address": "/addresses/1",
            "manager": "/staff/2"
        }
        """;
        mockMvc.perform(post("/stores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict());
    }

    @Test
    void testNullSaveStore() throws Exception {
        mockMvc.perform(post("/stores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")) // Empty object instead of empty string
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testSaveStoreMissingAddress() throws Exception {
        String body = """
        { "manager": "/staff/2" }
        """;
        mockMvc.perform(post("/stores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testSaveStoreWithoutManager() throws Exception {
        String body = """
        { "address": "/addresses/1" }
        """;
        mockMvc.perform(post("/stores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().is4xxClientError());
    }

    // ---------------- View Store Tests ----------------

    @Test
    void testViewAllStores() throws Exception {
        mockMvc.perform(get("/stores")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testFindStoreByAddress() throws Exception {
        mockMvc.perform(get("/stores/search/findByAddress")
                        .param("address", "/addresses/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    // ---------------- Update Store Tests ----------------

    @Test
    void testUpdateStore() throws Exception {
        String body = """
        {
            "address": "/addresses/1",
            "manager": "/staff/2"
        }
        """;
        mockMvc.perform(put("/stores/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void testNullUpdateStore() throws Exception {
        String body = """
        { "address": null }
        """;
        mockMvc.perform(put("/stores/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNoContent());
    }

    @Test
    void testInvalidIdUpdateStore() throws Exception {
        String body = """
        { "address": "/addresses/1", "manager": "/staff/2" }
        """;
        mockMvc.perform(put("/stores/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict());
    }

    @Test
    void testUpdateStoreWithNewAddress() throws Exception {
        String body = """
        { "address": "/addresses/2", "manager": "/staff/2" }
        """;
        mockMvc.perform(put("/stores/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().is2xxSuccessful());
    }

    // ---------------- Delete Store Tests ----------------

    private void safeDeleteStore(Byte storeId) {
        Store store = storeRepo.findById(storeId).orElseThrow();
        Staff manager = store.getManager();
        if(manager != null){
            manager.setStore(null);
            staffRepo.save(manager);
        }
        storeRepo.delete(store);
    }

    @Test
    void testDeleteStoreInvalidId() throws Exception {
        mockMvc.perform(delete("/stores/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteNullStore() throws Exception {
        mockMvc.perform(delete("/stores")) // remove trailing slash
                .andExpect(status().is4xxClientError());
    }
}