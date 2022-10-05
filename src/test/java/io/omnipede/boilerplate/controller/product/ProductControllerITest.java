package io.omnipede.boilerplate.controller.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.servlet.http.Cookie;
import javax.transaction.Transactional;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class ProductControllerITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String PRODUCTS_ENDPOINT = "/api/v1/products";
    private static final String POST_SESSION_ENDPOINT = "/api/v1/session";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Should return all products")
    @Sql("/order-setup.sql")
    public void testGetAll() throws Exception {

        // Given

        // When
        MvcResult mvcResult = mockMvc.perform(get(PRODUCTS_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON))

                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(3))
                .andExpect(jsonPath("$.data").isArray())
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        String contentAsString = response.getContentAsString();
        assertThat(contentAsString)
            .isEqualTo("{\"total\":3,\"data\":[{\"id\":1,\"name\":\"Monitor\",\"price\":300000},{\"id\":2,\"name\":\"HDD\",\"price\":50000},{\"id\":3,\"name\":\"SSD\",\"price\":100000}]}");
    }

    @Test
    @DisplayName("Should return correct product.")
    @Sql("/order-setup.sql")
    public void testGet() throws Exception {
        // Given
        long productId = 2L;

        // When
        MvcResult mvcResult = mockMvc.perform(get(PRODUCTS_ENDPOINT + "/"+ productId)
                        .contentType(MediaType.APPLICATION_JSON))

                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap())
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        String contentAsString = response.getContentAsString();
        assertThat(contentAsString).isEqualTo("{\"id\":2,\"name\":\"HDD\",\"price\":50000}");
    }

    @Test
    @DisplayName("Should throw 404 error when product not exists")
    @Sql("/order-setup.sql")
    public void testGet_2() throws Exception {

        // Given
        long productId = 4L;

        // When
        mockMvc.perform(get(PRODUCTS_ENDPOINT + "/"+ productId)
                        .contentType(MediaType.APPLICATION_JSON))

                // Then
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.id").value(40400))
                .andReturn();
    }

    @Test
    @DisplayName("Should properly order one product")
    @Sql("/order-setup.sql")
    public void testPostOrder() throws Exception {

        // Given
        long productId = 3L;

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

        assertThrows(EmptyResultDataAccessException.class, () -> jdbcTemplate.queryForMap("SELECT * FROM T_ORDER WHERE PRODUCT_ID = ?", productId));

        // When
        mockMvc.perform(post(PRODUCTS_ENDPOINT + "/" + productId + "/orders" )
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(loginResponse.getCookies()))
                .andExpect(status().isOk());

        // Then
        Map<String, Object> afterPost = jdbcTemplate.queryForMap("SELECT * FROM T_ORDER WHERE PRODUCT_ID = ?", productId);
        assertThat(afterPost.get("user_id")).isEqualTo(1L);
        assertThat(afterPost.get("product_id")).isEqualTo(productId);
    }

    @Test
    @DisplayName("Should throw 404 error when product is not found")
    @Sql("/order-setup.sql")
    public void testPostOrder_2() throws Exception {

        // Given
        long productId = 4L;

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
        mockMvc.perform(post(PRODUCTS_ENDPOINT + "/" + productId + "/orders" )
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(loginResponse.getCookies()))

                // Then
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.id").value(40400));
    }

    @Test
    @DisplayName("Should throw 401 error when unauthorized")
    public void testPostOrder_3() throws Exception {

        // Given
        long productId = 2L;

        // When
        mockMvc.perform(post(PRODUCTS_ENDPOINT + "/" + productId + "/orders" )
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie("SESSION", "WRONG_SESSION")))

                // Then
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.id").value(40100));
    }
}