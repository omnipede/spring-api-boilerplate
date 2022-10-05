package io.omnipede.boilerplate.controller.session;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
class SessionControllerITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String SESSION_ENDPOINT = "/api/v1/session";

    @Test
    @DisplayName("Should correctly post user session data")
    @Sql("/order-setup.sql")
    public void testPost() throws Exception {

        // Given
        String email = "omnipede@naver.com";
        String password = "password";
        Map<String, String> requestBody = Map.of(
                "email", email,
                "password", password
        );
        String content = objectMapper.writeValueAsString(requestBody);

        // When
        mockMvc.perform(post(SESSION_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))

                // Then
                .andExpect(status().isOk())
                .andExpect(cookie().exists("SESSION"));
    }

    @ParameterizedTest(name = "Should throw bad request error if request body is invalid")
    @MethodSource("testPost_2_args")
    public void testPost_2(String email, String password) throws Exception {
        // Given
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", email);
        requestBody.put("password", password);
        String content = objectMapper.writeValueAsString(requestBody);

        // When
        mockMvc.perform(post(SESSION_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))

                // Then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id").value(40000))
                .andExpect(jsonPath("$.message").isString())
                .andExpect(jsonPath("$.detail").isString());
    }

    private static Stream<Arguments> testPost_2_args() {
        return Stream.of(
                Arguments.of("wrongemail.com", "password"),
                Arguments.of("", "password"),
                Arguments.of(null, "password"),
                Arguments.of("correct@email.com", ""),
                Arguments.of("correct@email.com", null)
        );
    }

    @Test
    @DisplayName("Should throw error when login after logout")
    @Sql("/order-setup.sql")
    public void testDelete() throws Exception {

        // Given
        String email = "omnipede@naver.com";
        String password = "password";
        Map<String, String> requestBody = Map.of(
                "email", email,
                "password", password
        );
        String content = objectMapper.writeValueAsString(requestBody);

        MvcResult loginResult = mockMvc.perform(post(SESSION_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse loginResponse = loginResult.getResponse();

        // When
        mockMvc.perform(delete(SESSION_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(loginResponse.getCookies()))

                // Then
                .andExpect(status().isOk());
    }
}