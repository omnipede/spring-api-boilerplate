package io.omnipede.boilerplate.domain.order;

import io.omnipede.boilerplate.domain.product.Product;
import io.omnipede.boilerplate.domain.product.ProductRepository;
import io.omnipede.boilerplate.domain.user.User;
import io.omnipede.boilerplate.system.exception.ErrorCode;
import io.omnipede.boilerplate.system.exception.SystemException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class OrderUseCaseTest {

    private OrderUseCase orderUseCase;
    private OrderRepository orderRepository;
    private ProductRepository productRepository;

    @BeforeEach
    public void setup() {
        this.orderRepository = mock(OrderRepository.class);
        this.productRepository = mock(ProductRepository.class);
        this.orderUseCase = new OrderUseCase(orderRepository, productRepository);
    }

    @Test
    @DisplayName("Should return order list.")
    @SuppressWarnings("unchecked")
    public void testGetOrderList() {
        // Given
        Long userId = 1L;
        User user = mock(User.class);
        doReturn(userId)
                .when(user)
                .getId();
        Pageable pageable = mock(Pageable.class);

        Page<Order> orderList = (Page<Order>) mock(Page.class);
        doReturn(orderList)
                .when(orderRepository)
                .findAllByUserId(userId, pageable);

        // When
        Page<Order> result = orderUseCase.getOrderList(user, pageable);

        // Then
        assertThat(result == orderList).isTrue();
    }

    @Test
    @DisplayName("Should save order data correctly.")
    public void testOrder() {

        // Given
        Long productId = 1L;
        User user = mock(User.class);

        Product product = mock(Product.class);
        Order order = mock(Order.class);

        doReturn(Optional.of(product))
                .when(productRepository)
                .findById(productId);

        doReturn(order)
                .when(user)
                .order(product);

        // When
        orderUseCase.order(user, productId);

        // Then
        verify(productRepository, times(1)).findById(productId);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    @DisplayName("Should throw exception when product is not found.")
    public void testOrder_2() {
        // Given
        Long productId = 1L;
        User user = mock(User.class);

        doReturn(Optional.empty())
            .when(productRepository)
            .findById(productId);

        // When
        SystemException systemException = assertThrows(SystemException.class, () -> orderUseCase.order(user, productId));

        // Then
        assertThat(systemException).isNotNull();
        assertThat(systemException.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND);
    }
}