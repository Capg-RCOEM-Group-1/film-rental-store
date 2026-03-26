package com.rcoem.filmrentalstore.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class StaffApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetAllStaff() throws Exception {
        mockMvc.perform(get("/staffs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.staffs").exists());
    }
}
