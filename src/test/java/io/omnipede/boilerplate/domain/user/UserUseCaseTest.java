package io.omnipede.boilerplate.domain.user;

import io.omnipede.boilerplate.system.exception.ErrorCode;
import io.omnipede.boilerplate.system.exception.SystemException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserUseCaseTest {

    private UserRepository userRepository;
    private UserUseCase userUseCase;

    @BeforeEach
    public void setup() {
        userRepository = mock(UserRepository.class);
        userUseCase = new UserUseCase(userRepository);
    }

    @Test
    @DisplayName("Should save correct user data.")
    public void testSignup() {
        // Given
        User user = mock(User.class);
        String email = "email";
        doReturn(email)
            .when(user)
            .getEmail();

        doReturn(Optional.empty())
                .when(userRepository)
                .findByEmail(email);

        doReturn(user)
                .when(userRepository)
                .save(any());

        // When
        userUseCase.signup(user);

        // Then
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Should throw exception when user already exists.")
    public void testSignup_2() {
        // Given
        User user = mock(User.class);
        String email = "email";
        doReturn(email)
                .when(user)
                .getEmail();

        doReturn(Optional.of(user))
                .when(userRepository)
                .findByEmail(email);

        // When
        SystemException systemException = assertThrows(SystemException.class, () -> userUseCase.signup(user));

        // Then
        assertThat(systemException).isNotNull();
        assertThat(systemException.getErrorCode()).isEqualTo(ErrorCode.CONFLICT);
    }
}