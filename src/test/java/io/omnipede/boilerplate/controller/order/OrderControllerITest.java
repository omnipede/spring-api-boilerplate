package io.omnipede.boilerplate.controller.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class OrderControllerITest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String POST_SESSION_ENDPOINT = "/api/v1/session";
    private static final String GET_ALL_ORDERS_ENDPOINT = "/api/v1/orders";

    @Test
    @DisplayName("Should return correct order list")
    @Sql("/order-setup.sql")
    public void testGetAll() throws Exception {
        // Given (login)
        Map<String, String> loginRequest = Map.of(
                "email", "omnipede@naver.com",
                "password", "password"
        );
        String loginRequestBody = objectMapper.writeValueAsString(loginRequest);
        MvcResult mvcResult = mockMvc.perform(post(POST_SESSION_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestBody))
                .andExpect(status().isOk())
                .andReturn();
        MockHttpServletResponse loginResponse = mvcResult.getResponse();

        // When
        MvcResult result = mockMvc.perform(get(GET_ALL_ORDERS_ENDPOINT)
            .contentType(MediaType.APPLICATION_JSON)
            .cookie(loginResponse.getCookies()))
        // Then
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.total").value(2))
        .andExpect(jsonPath("$.data").isArray())
        .andReturn();

        String contentAsString = result.getResponse().getContentAsString();
        assertThat(contentAsString)
            .isEqualTo("{\"total\":2,\"data\":[{\"product\":{\"id\":1,\"name\":\"Monitor\",\"price\":300000},\"ordered_at\":\"2022-10-03 07:23:34\"},{\"product\":{\"id\":2,\"name\":\"HDD\",\"price\":50000},\"ordered_at\":\"2022-10-03 07:23:48\"}]}");
    }

    @Test
    @DisplayName("Should throw error when unauthorized")
    public void testGetAll_2() throws Exception {

        // Given

        // When
        mockMvc.perform(get(GET_ALL_ORDERS_ENDPOINT)
            .contentType(MediaType.APPLICATION_JSON))

        // Then
        .andExpect(status().is(401))
        .andExpect(jsonPath("$.id").value(40100))
        .andExpect(jsonPath("$.message").value("Unauthorized"));
    }
}