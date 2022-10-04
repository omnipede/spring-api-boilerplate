package io.omnipede.boilerplate.system.component;

import io.omnipede.boilerplate.domain.product.Product;
import io.omnipede.boilerplate.domain.product.ProductRepository;
import io.omnipede.boilerplate.domain.user.User;
import io.omnipede.boilerplate.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class DataLoader implements ApplicationRunner {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) {

        // TODO save if not exists
//        log.info("Loading default products...");
//        Product toy = Product.builder()
//                .name("Monitor")
//                .price(100000L)
//                .build();
//
//        Product food = Product.builder()
//                .name("HDD")
//                .price(50000L)
//                .build();
//
//        productRepository.save(toy);
//        productRepository.save(food);
//
//        log.info("Loading default users...");
//        User user = User.builder()
//                .email("omnipede@naver.com")
//                .password("password")
//                .build();
//
//        userRepository.save(user);
    }
}
