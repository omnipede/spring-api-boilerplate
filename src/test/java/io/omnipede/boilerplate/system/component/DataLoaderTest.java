package io.omnipede.boilerplate.system.component;

import io.omnipede.boilerplate.domain.product.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.ApplicationArguments;

import static org.mockito.Mockito.*;

class DataLoaderTest {

    private DataLoader dataLoader;
    private ProductRepository productRepository;

    @BeforeEach
    public void setup() {

        productRepository = mock(ProductRepository.class);
        dataLoader = new DataLoader(productRepository);
    }

    @Test
    @DisplayName("Should correctly load demo data")
    public void testRun() {

        // Given
        doReturn(0L)
                .when(productRepository)
                .count();

        ApplicationArguments args = mock(ApplicationArguments.class);

        // When
        dataLoader.run(args);

        // Then
        verify(productRepository, times(3)).save(any());
    }

    @Test
    @DisplayName("Should not load data if DB is not empty")
    public void testRun_2() {

        // Given
        doReturn(1L)
                .when(productRepository)
                .count();

        ApplicationArguments args = mock(ApplicationArguments.class);

        // When
        dataLoader.run(args);

        // Then
        verify(productRepository, times(0)).save(any());
    }
}