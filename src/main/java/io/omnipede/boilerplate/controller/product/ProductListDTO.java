package io.omnipede.boilerplate.controller.product;

import io.omnipede.boilerplate.domain.product.Product;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
class ProductListDTO {

    private final Long total;
    private final List<ProductDTO> data;

    public ProductListDTO(Page<Product> products) {
        this.total = products.getTotalElements();
        this.data = products.stream()
            .map(ProductDTO::fromEntity)
            .collect(Collectors.toList());
    }
}
