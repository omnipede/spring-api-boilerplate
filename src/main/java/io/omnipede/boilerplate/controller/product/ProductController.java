package io.omnipede.boilerplate.controller.product;

import io.omnipede.boilerplate.domain.order.OrderUseCase;
import io.omnipede.boilerplate.domain.product.Product;
import io.omnipede.boilerplate.domain.product.ProductUseCase;
import io.omnipede.boilerplate.domain.user.User;
import io.omnipede.boilerplate.system.resolver.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Validated
public class ProductController {

    private final ProductUseCase productUseCase;
    private final OrderUseCase orderUseCase;

    @GetMapping
    public ProductListDTO getAll(
        @RequestParam(value = "page", defaultValue = "0") @Min(0) int page,
        @RequestParam(value = "size", defaultValue = "100") @Min(1) int size
    ) {
        Page<Product> productList = productUseCase.getProductList(PageRequest.of(page, size));
        return new ProductListDTO(productList);
    }

    @GetMapping("/{id}")
    public ProductDTO get(@PathVariable Long id) {
        Product product = productUseCase.getProduct(id);
        return ProductDTO.fromEntity(product);
    }

    @PostMapping("/{id}/orders")
    public void postOrder(
        @LoginUser User user,
        @PathVariable(name = "id") Long productId
    ) {
        orderUseCase.order(user, productId);
    }
}
