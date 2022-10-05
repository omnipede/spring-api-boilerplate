package io.omnipede.boilerplate.controller.user;

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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
class UserControllerITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String USER_ENDPOINT = "/api/v1/users";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Should correctly post user data.")
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
        mockMvc.perform(post(USER_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))

                // Then
                .andExpect(status().isOk());

        Map<String, Object> afterPost = jdbcTemplate.queryForMap("SELECT * FROM T_USER WHERE EMAIL = ?", email);
        assertThat(afterPost.get("email")).isEqualTo(email);
        assertThat(afterPost.get("password")).isEqualTo("71ab2e2b8c02d1b3ba061cef5c4dd3a8");
    }

    @ParameterizedTest(name = "Should throw bad request error when request body format is invalid.")
    @MethodSource("testPost_2_args")
    public void testPost_2(String email, String password) throws Exception {

        // Given
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", email);
        requestBody.put("password", password);
        String content = objectMapper.writeValueAsString(requestBody);

        // When
        mockMvc.perform(post(USER_ENDPOINT)
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
}