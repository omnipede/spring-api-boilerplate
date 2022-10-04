package io.omnipede.boilerplate.domain.order;

import io.omnipede.boilerplate.domain.product.Product;
import io.omnipede.boilerplate.domain.product.ProductRepository;
import io.omnipede.boilerplate.domain.user.User;
import io.omnipede.boilerplate.system.exception.ErrorCode;
import io.omnipede.boilerplate.system.exception.SystemException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderUseCase {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public Page<Order> getOrderList(User user, Pageable pageable) {
        return orderRepository.findAllByUserId(user.getId(), pageable);
    }

    public void order(User user, Long productId) {
        // Find target product
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new SystemException(ErrorCode.NOT_FOUND, "Product id " + productId + " not found." ));

        // The user order the product
        Order order = user.order(product);
        orderRepository.save(order);
    }
}
