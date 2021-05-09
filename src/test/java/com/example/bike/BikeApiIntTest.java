package com.example.bike;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.bike.api.request.AddMemberRequest;
import com.example.bike.dal.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

//@Tag("Slow")
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
//@Sql("classpath:sql/test-data.sql")
@TestMethodOrder(OrderAnnotation.class)
public class BikeApiIntTest {

  static final ObjectMapper mapper = new ObjectMapper();

  @Autowired
  MockMvc mockMvc;

  @Test
  @Order(1)
  public void can_add_member() throws Exception {
    var req = AddMemberRequest.builder()
        .name("TestFirst Last")
        .build();

    mockMvc.perform(
        MockMvcRequestBuilders.post("/v1/examples/bike/member")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", Constants.TEST_USER)
            .contentType("application/json")
            .content(mapper.writeValueAsBytes(req))
    )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", notNullValue()))
        .andExpect(jsonPath("$.name", is("TestFirst Last")));
  }

  @Test
  @Order(2)
  public void can_get_member() throws Exception {
    mockMvc.perform(
        MockMvcRequestBuilders.get("/v1/examples/bike/member")
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", Constants.TEST_USER)
    )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.members", notNullValue()))
        .andExpect(jsonPath("$.members", hasSize(2)))
        .andExpect(jsonPath("$.members").isArray());
  }

  @Test
  @Order(3)
  public void can_get_bike() throws Exception {
    mockMvc.perform(
        MockMvcRequestBuilders.get("/v1/examples/bike/bike")
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", Constants.TEST_USER)
    )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.bikes", notNullValue()))
        .andExpect(jsonPath("$.bikes", hasSize(6)))
        .andExpect(jsonPath("$.bikes").isArray());
  }

}
