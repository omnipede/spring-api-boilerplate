package io.omnipede.boilerplate.domain.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class ProductUseCaseTest {

    private ProductUseCase productUseCase;
    private ProductRepository productRepository;

    @BeforeEach
    public void setup() {
        productRepository = mock(ProductRepository.class);
        productUseCase = new ProductUseCase(productRepository);
    }

    @Test
    @DisplayName("Should return one product")
    public void testGetProduct() {
        // Given
        Long productId = 1L;
        Product product = mock(Product.class);

        doReturn(Optional.of(product))
                .when(productRepository)
                .findById(productId);

        // When
        Product result = productUseCase.getProduct(productId);

        // Then
        assertThat(result == product).isTrue();
    }

    @Test
    @DisplayName("Should return products")
    @SuppressWarnings("unchecked")
    public void testGetProductList() {
        // Given
        Pageable pageable = mock(Pageable.class);
        Page<Product> productList = (Page<Product>) mock(Page.class);
        doReturn(productList)
                .when(productRepository)
                .findAll(pageable);

        // When
        Page<Product> result = productUseCase.getProductList(pageable);

        // Then
        assertThat(result == productList).isTrue();
    }
}