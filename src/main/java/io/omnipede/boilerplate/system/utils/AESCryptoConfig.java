package io.omnipede.boilerplate.system.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Configuration
@ConfigurationProperties("secret")
@EnableConfigurationProperties
@Validated
public class AESCryptoConfig {

    // 사용자 정보 암복호화시 사용하는 AES 키
    @NotNull
    private String aesKey;
}

