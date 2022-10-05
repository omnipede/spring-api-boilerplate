package io.omnipede.boilerplate.system.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.crypto.Cipher;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AESCryptoConverterTest {
    private AESCryptoConverter converter;

    @BeforeEach
    public void setup() {

        AESCryptoConfig config = mock(AESCryptoConfig.class);
        String AESKey = "AES_KEY";
        doReturn(AESKey)
                .when(config)
                .getAesKey();

        converter = new AESCryptoConverter();
        converter.setKey(config);
    }

    @Test
    @DisplayName("AES key 를 이용해 평문을 암복호화할 수 있어야 한다")
    public void tc1() {

        // Given
        String plainText = "password2";

        // When
        String encrypted = converter.convertToDatabaseColumn(plainText);

        // Then
        assertThat(encrypted).isNotEqualTo(plainText);
        System.out.println(encrypted);

        // When
        String decrypted = converter.convertToEntityAttribute(encrypted);

        // Then
        assertThat(decrypted).isEqualTo(plainText);
    }

    @Test
    @DisplayName("암호화 중 문제가 생겼을 경우 에러가 발생해야 한다")
    public void tc2() {

        try(
                MockedStatic<Cipher> mocked = mockStatic(Cipher.class)
        ) {
            // Given
            mocked.when(() -> Cipher.getInstance(any()))
                    .thenThrow(NoSuchAlgorithmException.class);
            String plainText = UUID.randomUUID().toString();

            //When
            Throwable throwable = catchThrowable(() -> {
                converter.convertToDatabaseColumn(plainText);
            });

            // Then
            assertThat(throwable).isInstanceOf(RuntimeException.class);
            assertThat(throwable).hasCauseInstanceOf(NoSuchAlgorithmException.class);
        }
    }

    @Test
    @DisplayName("복호화 중 문제가 생겼을 경우 에러가 발생해야 한다")
    public void tc3() {

        try(
                MockedStatic<Cipher> mocked = mockStatic(Cipher.class)
        ) {
            // Given
            mocked.when(() -> Cipher.getInstance(any()))
                    .thenThrow(NoSuchAlgorithmException.class);
            String encrypted = UUID.randomUUID().toString();

            //When
            Throwable throwable = catchThrowable(() -> {
                converter.convertToEntityAttribute(encrypted);
            });

            // Then
            assertThat(throwable).isInstanceOf(RuntimeException.class);
            assertThat(throwable).hasCauseInstanceOf(NoSuchAlgorithmException.class);
        }
    }
}