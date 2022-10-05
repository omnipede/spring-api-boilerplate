package io.omnipede.boilerplate.domain.session;

import io.omnipede.boilerplate.domain.user.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class SessionTest {

    @Test
    public void testGetterSetter() {

        // Given
        Session session = new Session();
        User user = mock(User.class);

        // When
        session.setUser(user);

        // Then
        User another = session.getUser();
        assertThat(another == user).isTrue();
    }
}