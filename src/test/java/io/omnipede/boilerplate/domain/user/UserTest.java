package io.omnipede.boilerplate.domain.user;

import io.omnipede.boilerplate.domain.order.Order;
import io.omnipede.boilerplate.domain.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Date;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    public void testConstructor() {

        // Given
        Long id = 1L;
        String email = "email";
        String password = "password";
        Date createdAt = new Date();
        Date updatedAt = new Date();

        // When
        User user = User.builder()
                .id(id)
                .email(email)
                .password(password)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        // Then
        assertThat(user.getId()).isEqualTo(id);
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.getCreatedAt()).isEqualTo(createdAt);
        assertThat(user.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    @DisplayName("Should return correct order data.")
    public void testOrder() {

        // Given
        User user = User.builder().build();
        Product product = Product.builder().build();

        // When
        Order order = user.order(product);

        // Then
        assertThat(order.getUser()).isSameAs(user);
        assertThat(order.getProduct()).isSameAs(product);
    }

    @ParameterizedTest(name = "Should return true with correct password: {index}")
    @MethodSource("testLogin_args")
    public void testLogin(String password1, String password2, boolean expected) {

        // Given
        User user = User.builder()
            .password(password1)
            .build();

        // When
        Boolean result = user.login(password2);

        // Then
        assertThat(result).isEqualTo(expected);
    }

    private static Stream<Arguments> testLogin_args() {
        return Stream.of(
                Arguments.of("password", "password", true),
                Arguments.of("password", "wrong_password", false)
        );
    }
}