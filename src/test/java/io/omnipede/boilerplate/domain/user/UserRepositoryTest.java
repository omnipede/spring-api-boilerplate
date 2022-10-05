package io.omnipede.boilerplate.domain.user;

import io.omnipede.boilerplate.system.utils.AESCryptoConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@EnableConfigurationProperties(AESCryptoConfig.class)
@ActiveProfiles("test")
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("Should select user by email.")
    public void testFindByEmail() {

        // Given
        String email = "test@email.com";
        User user = User.builder()
            .email(email)
            .password("password")
            .build();
        User saved = userRepository.save(user);

        // When
        User selected = userRepository.findByEmail(email)
                .orElse(null);

        // Then
        assertThat(selected).isNotNull();
        assertThat(selected).isEqualTo(saved);
    }

    @Test
    @DisplayName("Should return empty when user email is incorrect.")
    public void testFindByEmail2() {

        // Given
        String wrongEmail = "wrong@email.com";

        // When
        User selected = userRepository.findByEmail(wrongEmail)
                .orElse(null);

        // Then
        assertThat(selected).isNull();
    }

    @Test
    @DisplayName("Should save user data. And password should be encrypted.")
    public void testSave() {

        // Given
        String password = "password";
        User user = User.builder()
            .email("email")
            .password(password)
            .build();

        // When
        User saved = userRepository.save(user);

        // Then
        String insertedPassword = jdbcTemplate
            .queryForObject("SELECT password FROM T_USER WHERE id = " + saved.getId(), String.class);

        assertThat(insertedPassword).isNotEqualTo(password);
        System.out.println(insertedPassword);
        assertThat(saved.getPassword()).isEqualTo(password);
    }
}