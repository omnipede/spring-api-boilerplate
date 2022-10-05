package io.omnipede.boilerplate.controller.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.omnipede.boilerplate.controller.product.ProductDTO;
import io.omnipede.boilerplate.domain.order.Order;
import io.omnipede.boilerplate.domain.product.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
@Builder
class OrderDTO {

    @JsonProperty("product")
    private ProductDTO productDTO;

    @JsonProperty("ordered_at")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date orderedAt;

    public static OrderDTO fromEntity(Order entity) {
        Product product = entity.getProduct();
        ProductDTO productDTO = ProductDTO.fromEntity(product);

        return OrderDTO.builder()
                .productDTO(productDTO)
                .orderedAt(entity.getCreatedAt())
                .build();
    }
}
