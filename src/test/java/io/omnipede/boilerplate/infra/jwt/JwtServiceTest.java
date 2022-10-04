package io.omnipede.boilerplate.infra.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    @Test
    public void tc1() {

        // When
        String token = jwtService.create("omnipede@naver.com");

        // Then
        System.out.println(token);
    }
}