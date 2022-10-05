package io.omnipede.boilerplate.domain.session;

import io.omnipede.boilerplate.domain.user.User;
import io.omnipede.boilerplate.domain.user.UserRepository;
import io.omnipede.boilerplate.system.exception.ErrorCode;
import io.omnipede.boilerplate.system.exception.SystemException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionUseCase {

    private final UserRepository userRepository;
    private final Session session;

    public void login(String email, String password) {

        // Find user info by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new SystemException(ErrorCode.UNAUTHORIZED, "Not found email."));

        // Check password correctness
        if (!user.login(password))
            throw new SystemException(ErrorCode.UNAUTHORIZED, "Wrong password.");

        // Store authenticated user data on session storage.
        session.setUser(user);
    }

    public User auth() {
        User user = session.getUser();
        if (user == null)
            throw new SystemException(ErrorCode.UNAUTHORIZED, "Not found login info.");
        return user;
    }
}
