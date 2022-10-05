package io.omnipede.boilerplate.controller.order;


import io.omnipede.boilerplate.domain.order.Order;
import io.omnipede.boilerplate.domain.order.OrderUseCase;
import io.omnipede.boilerplate.domain.product.Product;
import io.omnipede.boilerplate.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    private OrderController orderController;
    private OrderUseCase orderUseCase;

    @BeforeEach
    public void setup() {
        orderUseCase = mock(OrderUseCase.class);
        orderController = new OrderController(orderUseCase);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetAll() {

        // Given
        List<Order> orders = givenOrderList();
        Page<Order> orderList = new PageImpl<>(orders);

        User user = mock(User.class);
        int page = 0;
        int size = 100;

        doReturn(orderList)
            .when(orderUseCase)
            .getOrderList(user, PageRequest.of(page, size));

        // When
        OrderListDTO result = orderController.getAll(user, page, size);

        // Then
//        assertThat(result.getTotal()).isEqualTo(orderList.getTotalElements());
//        assertThat(result.getData())
//                .hasSize()
//                .extracting(OrderDTO::getProductDTO)
//                .e
    }

    private List<Order> givenOrderList() {
        Product product = Product.builder()
                .id(1L)
                .name("name")
                .price(100L)
                .build();

        Order order = Order.builder()
                .product(product)
                .build();

        return List.of(order, order);
    }
}