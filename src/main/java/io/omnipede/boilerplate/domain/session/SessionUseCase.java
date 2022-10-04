package io.omnipede.boilerplate.domain.session;

import io.omnipede.boilerplate.domain.user.User;
import io.omnipede.boilerplate.domain.user.UserRepository;
import io.omnipede.boilerplate.system.exception.ErrorCode;
import io.omnipede.boilerplate.system.exception.SystemException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@RequiredArgsConstructor
public class SessionUseCase {

    private final UserRepository userRepository;

    @Resource
    private Session session;

    public void login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new SystemException(ErrorCode.UNAUTHORIZED, "Not found email."));

        if (!user.login(password))
            throw new SystemException(ErrorCode.UNAUTHORIZED, "Wrong password.");

        // Store user data on session storage.
        session.setUser(user);
    }

    public void logout() {
        session.clear();
    }

    public User auth() {
        User user = session.getUser();
        if (user == null)
            throw new SystemException(ErrorCode.UNAUTHORIZED, "Not found login info.");
        return user;
    }
}
