package io.omnipede.boilerplate.controller.order;

import io.omnipede.boilerplate.domain.order.Order;
import io.omnipede.boilerplate.domain.order.OrderUseCase;
import io.omnipede.boilerplate.domain.user.User;
import io.omnipede.boilerplate.system.resolver.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Validated
class OrderController {

    private final OrderUseCase orderUseCase;

    @GetMapping
    public OrderListDTO getAll(
            @LoginUser User user,
            @RequestParam(value = "page", defaultValue = "0") @Min(0) Integer page,
            @RequestParam(value = "size", defaultValue = "100") @Min(1) Integer size
    ) {
        Page<Order> orderList = orderUseCase.getOrderList(user, PageRequest.of(page, size));
        return new OrderListDTO(orderList);
    }
}
