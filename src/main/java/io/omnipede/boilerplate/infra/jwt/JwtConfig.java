package io.omnipede.boilerplate.infra.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Base64;

@Getter
@Setter
@Validated
@Configuration
@ConfigurationProperties("jwt")
class JwtConfig {

    @NotBlank
    @NotNull
    private String secretKey;

    @NotNull
    private Long duration;

    @PostConstruct
    public void init() {
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }
}
