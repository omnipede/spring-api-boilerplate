package io.omnipede.boilerplate.controller.order;

import io.omnipede.boilerplate.domain.order.Order;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
class OrderListDTO {

    private final Long total;
    private final List<OrderDTO> data;

    public OrderListDTO(Page<Order> orderList) {
        this.total = orderList.getTotalElements();
        this.data = orderList.stream().map(OrderDTO::fromEntity)
            .collect(Collectors.toList());
    }
}
