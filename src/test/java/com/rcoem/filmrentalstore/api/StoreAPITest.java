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
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class StoreAPITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private StaffRepository staffRepository;

    private Store testStore;
    private Address testAddress;
    private Staff testManager;

    @BeforeEach
    void setup() {
        // 1. Clear existing data in correct dependency order
        staffRepository.deleteAll();
        storeRepository.deleteAll();
        addressRepository.deleteAll();

        // 2. Create and SAVE the Address first
        // This gives the address an ID so the @NotNull check in Store passes
        Address address = new Address();
        address.setAddress("123 Main St");
        address.setDistrict("Central");
        address.setPhone("555-0123");
        // Add any other required address fields here
        testAddress = addressRepository.save(address);

        // 3. Create the Store and link the saved Address
        Store store = new Store();
        store.setAddress(testAddress);
        // Do NOT set the manager yet, as the staff doesn't exist
        testStore = storeRepository.save(store);

        // 4. Create the Staff, link to Address and Store, then SAVE
        Staff staff = new Staff();
        staff.setFirstName("John");
        staff.setLastName("Doe");
        staff.setAddress(testAddress);
        staff.setStore(testStore);
        staff.setActive(true);
        staff.setPassword("secret");
        testManager = staffRepository.save(staff);

        // 5. Update the Store to set the Manager
        testStore.setManager(testManager);
        storeRepository.save(testStore);
    }

    // GET Endpoints

    @Test
    void testGetAllStores() throws Exception {
        mockMvc.perform(get("/stores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.stores").exists());
    }

    @Test
    void testGetStoreById_Valid() throws Exception {
        mockMvc.perform(get("/stores/" + testStore.getStoreId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void testGetStoreById_NotFound() throws Exception {
        mockMvc.perform(get("/stores/9999"))
                .andExpect(status().isBadRequest());
    }

    // POST Endpoints

    @Test
    void testCreateStore_Valid() throws Exception {
        // Create new prerequisites for the new store
        Staff newManager = staffRepository.save(testManager);
        Address newAddress = addressRepository.save(new Address());

        // In Spring Data REST, we often pass the URIs of related entities
        String newStoreJson = String.format("""
            {
                "manager": "/staff/%d",
                "address": "/addresses/%d"
            }
            """, newManager.getStaffId(), newAddress.getAddressId());

        mockMvc.perform(post("/stores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newStoreJson))
                .andExpect(status().isCreated());
    }

    // PATCH Endpoints

    @Test
    void testUpdateStoreAddress_Valid() throws Exception {
        Address updatedAddress = addressRepository.save(new Address());
        String patchJson = String.format("{\"address\": \"/addresses/%d\"}", updatedAddress.getAddressId());

        mockMvc.perform(patch("/stores/" + testStore.getStoreId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchJson))
                .andExpect(status().is2xxSuccessful());

        // Verify in DB
        Store updated = storeRepository.findById(testStore.getStoreId()).orElseThrow();
        assertThat(updated.getAddress().getAddressId()).isEqualTo(updatedAddress.getAddressId());
    }

    // DELETE Store

    @Test
    void testDeleteStore_Valid() throws Exception {
        mockMvc.perform(delete("/stores/" + testStore.getStoreId()))
                .andExpect(status().isNoContent());

        assertThat(storeRepository.findById(testStore.getStoreId())).isEmpty();
    }


    // Negative Test Cases for the Endpoints : -

    @Test
    public void testGetStoreById_InvalidDataType() throws Exception {
        // Passing a String "abc" instead of a Long ID
        mockMvc.perform(get("/stores/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateStore_InvalidPayloadFormat() throws Exception {
        // Sending malformed JSON (missing quotes or curly braces)
        String malformedJson = "{ \"manager\": /staff/1 ";

        mockMvc.perform(patch("/stores/" + testStore.getStoreId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andExpect(status().isBadRequest());
    }

    /*@Test
    public void testCreateStore_MissingManager_BadRequest() throws Exception {
        // Omitting the manager field entirely
        String bodyWithoutManager = String.format("""
            {
                "address": "/addresses/%d"
            }
            """, testAddress.getAddressId());

        mockMvc.perform(post("/stores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyWithoutManager))
                .andExpect(status().isBadRequest());
    }
*/

    @Test
    public void testCreateStore_NonExistentManager() throws Exception {
        // Pointing to a Staff ID (9999) that does not exist
        String bodyWithGhostManager = String.format("""
        {
            "manager": "/staff/9999L",
            "address": "/addresses/%d"
        }
        """, testAddress.getAddressId());

        mockMvc.perform(post("/stores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyWithGhostManager))
                .andExpect(status().isBadRequest()); // Change from .isNotFound() to .isBadRequest()
    }
}