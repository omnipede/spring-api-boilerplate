package io.omnipede.boilerplate.domain.product;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTest {

    @Test
    public void testConstructor() {

        // Given
        Long id = 1L;
        String name = "name";
        Long price = 1L;
        Date createdAt = new Date();
        Date updatedAt = new Date();

        // When
        Product product = Product.builder()
                .id(id)
                .name(name)
                .price(price)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        // Then
        assertThat(product.getId()).isEqualTo(id);
        assertThat(product.getName()).isEqualTo(name);
        assertThat(product.getPrice()).isEqualTo(price);
        assertThat(product.getCreatedAt()).isEqualTo(createdAt);
        assertThat(product.getUpdatedAt()).isEqualTo(updatedAt);
    }
}