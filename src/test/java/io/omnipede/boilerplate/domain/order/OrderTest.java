package io.omnipede.boilerplate.domain.order;

import io.omnipede.boilerplate.domain.product.Product;
import io.omnipede.boilerplate.domain.user.User;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class OrderTest {

    @Test
    public void testConstructor() {

        // Given
        Long id = 1L;
        User user = mock(User.class);
        Product product = mock(Product.class);
        Date createdAt = new Date();
        Date updatedAt = new Date();

        // When
        Order order = Order.builder()
                .id(id)
                .user(user)
                .product(product)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        // Then
        assertThat(order.getId()).isEqualTo(id);
        assertThat(order.getUser() == user).isTrue();
        assertThat(order.getProduct() == product).isTrue();
        assertThat(order.getCreatedAt()).isEqualTo(createdAt);
        assertThat(order.getUpdatedAt()).isEqualTo(updatedAt);
    }
}