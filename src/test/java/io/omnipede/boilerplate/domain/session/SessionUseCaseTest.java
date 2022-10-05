package io.omnipede.boilerplate.domain.session;

import io.omnipede.boilerplate.domain.user.User;
import io.omnipede.boilerplate.domain.user.UserRepository;
import io.omnipede.boilerplate.system.exception.ErrorCode;
import io.omnipede.boilerplate.system.exception.SystemException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class SessionUseCaseTest {

    private SessionUseCase sessionUseCase;

    private UserRepository userRepository;

    private Session session;

    @BeforeEach
    public void setup() {

        userRepository = mock(UserRepository.class);
        session = mock(Session.class);
        sessionUseCase = new SessionUseCase(userRepository, session);
    }

    @Test
    @DisplayName("Should save correct user info on the session.")
    public void testLogin() {

        // Given
        String email = "email";
        String password = "password";

        User user = mock(User.class);
        doReturn(Optional.of(user))
            .when(userRepository)
            .findByEmail(email);

        doReturn(true)
                .when(user)
                .login(password);

        doNothing()
            .when(session)
            .setUser(user);

        // When
        sessionUseCase.login(email, password);

        // Then
        verify(session, times(1)).setUser(user);
    }

    @Test
    @DisplayName("Should throw exception when user data is not found from the DB.")
    public void testLogin_2() {

        // Given
        String email = "email";
        String password = "password";

        doReturn(Optional.empty())
                .when(userRepository)
                .findByEmail(email);

        // When
        SystemException systemException = assertThrows(SystemException.class, () -> sessionUseCase.login(email, password));

        // Then
        assertThat(systemException).isNotNull();
        assertThat(systemException.getErrorCode()).isEqualTo(ErrorCode.UNAUTHORIZED);
    }

    @Test
    @DisplayName("Should throw exception when password is incorrect.")
    public void testLogin_3() {

        // Given
        String email = "email";
        String password = "password";

        User user = mock(User.class);
        doReturn(Optional.of(user))
                .when(userRepository)
                .findByEmail(email);

        doReturn(false)
                .when(user)
                .login(password);

        // When
        SystemException systemException = assertThrows(SystemException.class, () -> sessionUseCase.login(email, password));

        // Then
        assertThat(systemException).isNotNull();
        assertThat(systemException.getErrorCode()).isEqualTo(ErrorCode.UNAUTHORIZED);
    }

    @Test
    @DisplayName("Should authenticate user by using session data.")
    public void testAuth() {

        // Given
        User user = mock(User.class);
        doReturn(user)
                .when(session)
                .getUser();

        // When
        User result = sessionUseCase.auth();

        // Then
        assertThat(user == result).isTrue();
    }

    @Test
    @DisplayName("Should throw exception when appropriate user data is not found from the session.")
    public void testAuth_2() {

        // Given
        doReturn(null)
                .when(session)
                .getUser();

        // When
        SystemException systemException = assertThrows(SystemException.class, () -> sessionUseCase.auth());

        // Then
        assertThat(systemException).isNotNull();
        assertThat(systemException.getErrorCode()).isEqualTo(ErrorCode.UNAUTHORIZED);
    }
}