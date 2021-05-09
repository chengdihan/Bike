package com.newengen.starterservice.api.examples.people;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@Tag("Slow")
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Sql("classpath:sql/test-data.sql")
public class PeopleApiIntTest {

    static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    MockMvc mockMvc;

    @Test
    public void can_get_people() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/v1/examples/people")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", Constants.TEST_USER)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.people", notNullValue()))
            .andExpect(jsonPath("$.people", hasSize(2)))
            .andExpect(jsonPath("$.people").isArray())
            .andExpect(jsonPath("$.people[0].fullName", is("Doe, Jane")))
            .andExpect(jsonPath("$.people[1].fullName", is("Doe, John")));
    }

    @Test
    public void can_get_person() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/v1/examples/people/1")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", Constants.TEST_USER)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName", is("John")))
            .andExpect(jsonPath("$.lastName", is("Doe")));
    }

    @Test
    public void can_add_person() throws Exception {
        var req = AddPersonRequest.builder()
            .firstName("TestFirst")
            .lastName("TestLast")
            .build();

        mockMvc.perform(
            MockMvcRequestBuilders.post("/v1/examples/people")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", Constants.TEST_USER)
                .contentType("application/json")
                .content(mapper.writeValueAsBytes(req))
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", notNullValue()))
            .andExpect(jsonPath("$.firstName", is("TestFirst")))
            .andExpect(jsonPath("$.lastName", is("TestLast")));
    }


    @Test
    public void can_update_person() throws Exception {
        var req = UpdatePersonRequest.builder()
            .firstName("TestFirst")
            .lastName("TestLast")
            .build();

        mockMvc.perform(
            MockMvcRequestBuilders.put("/v1/examples/people/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", Constants.TEST_USER)
                .contentType("application/json")
                .content(mapper.writeValueAsBytes(req))
        )
            .andExpect(status().isNoContent());

        mockMvc.perform(
            MockMvcRequestBuilders.get("/v1/examples/people/1")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", Constants.TEST_USER)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName", is("TestFirst")))
            .andExpect(jsonPath("$.lastName", is("TestLast")));
    }

    @Test
    public void can_delete_person() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders.delete("/v1/examples/people/1")
                .header("Authorization", Constants.TEST_USER)
        )
            .andExpect(status().isNoContent());

        mockMvc.perform(
            MockMvcRequestBuilders.get("/v1/examples/people/1")
                .header("Authorization", Constants.TEST_USER)
        )
            .andExpect(status().isNotFound());
    }

    @Test
    public void can_get_greeting() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/v1/examples/people/1/greeting")
                .accept(MediaType.TEXT_PLAIN)
                .header("Authorization", Constants.TEST_USER)
        )
            .andExpect(status().isOk())
            .andExpect(content().string("Hello, John Doe!"));
    }
}
