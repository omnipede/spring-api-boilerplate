package io.omnipede.boilerplate.system.component;

import io.omnipede.boilerplate.domain.product.Product;
import io.omnipede.boilerplate.domain.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 서버 실행 시 데모 데이터를 로딩하기 위한 컴포넌트
 */
@Slf4j
@Component
@RequiredArgsConstructor
class DataLoader implements ApplicationRunner {

    private final ProductRepository productRepository;

    @Override
    public void run(ApplicationArguments args) {

        // Save demo data if DB is empty
        if (productRepository.count() > 0)
            return;

        log.info("Loading default products...");
        Product monitor = Product.builder()
                .name("Monitor")
                .price(100000L)
                .build();

        Product hdd = Product.builder()
                .name("HDD")
                .price(50000L)
                .build();

        Product ssd = Product.builder()
                .name("SSD")
                .price(80000L)
                .build();

        productRepository.save(monitor);
        productRepository.save(hdd);
        productRepository.save(ssd);
    }
}
