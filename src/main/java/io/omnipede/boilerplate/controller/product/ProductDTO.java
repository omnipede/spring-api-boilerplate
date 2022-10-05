package io.omnipede.boilerplate.controller.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.omnipede.boilerplate.domain.product.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Date;

@Builder
@AllArgsConstructor
public class ProductDTO {

    @JsonProperty("id")
    private Long id;

    @NotBlank
    @NotNull
    @JsonProperty("name")
    private String name;

    @NotNull
    @JsonProperty("price")
    private Long price;

    static public ProductDTO fromEntity(Product entity) {
        return ProductDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .price(entity.getPrice())
                .build();
    }
}
