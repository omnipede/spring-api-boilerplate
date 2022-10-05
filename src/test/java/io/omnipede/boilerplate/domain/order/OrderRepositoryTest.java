package io.omnipede.boilerplate.domain.order;

import io.omnipede.boilerplate.domain.product.Product;
import io.omnipede.boilerplate.domain.user.User;
import io.omnipede.boilerplate.domain.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
class OrderRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("Should select orders with products")
    @Sql("/order-setup.sql")
    public void testFindAllByUserId() {

        // Given
        User user = userRepository.findById(1L)
            .orElseThrow(() -> new RuntimeException("Not found user."));

        // When
        Page<Order> orderList = orderRepository.findAllByUserId(user.getId(), PageRequest.of(0, 10));

        // Then
        assertThat(orderList)
            .hasSize(2)
            .extracting(Order::getProduct)
            .extracting(Product::getId)
            .containsExactlyInAnyOrder(1L, 2L);

        assertThat(orderList)
            .extracting(Order::getUser)
            .extracting(User::getId)
            .containsExactlyInAnyOrder(user.getId(), user.getId());
    }

    @Test
    @DisplayName("Should return empty list when not found.")
    public void testFindAllByUserId_2() {
        // Given
        Long userId = 3L;

        // When
        Page<Order> orderList = orderRepository.findAllByUserId(userId, PageRequest.of(0, 10));

        // Then
        assertThat(orderList).hasSize(0);
    }
}