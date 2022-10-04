package io.omnipede.boilerplate.domain.product;

import io.omnipede.boilerplate.system.exception.ErrorCode;
import io.omnipede.boilerplate.system.exception.SystemException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductUseCase {

    private final ProductRepository productRepository;

    public Product getProduct(Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new SystemException(ErrorCode.NOT_FOUND, "Product id " + productId + " not found."));
    }

    public Page<Product> getProductList(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
}
