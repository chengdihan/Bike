package com.newengen.starterservice.api.examples.lothric;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.newengen.starterservice.Constants;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@Tag("Slow")
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class LothricApiIntTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void can_get_jove_account() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/v1/examples/lothric/accounts/37")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", Constants.TEST_USER)
            )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(37)))
                .andExpect(jsonPath("$.name", is("Dev test client")));
    }
}
