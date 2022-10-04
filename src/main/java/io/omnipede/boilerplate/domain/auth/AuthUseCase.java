package io.omnipede.boilerplate.domain.auth;

import io.omnipede.boilerplate.domain.user.User;
import io.omnipede.boilerplate.domain.user.UserRepository;
import io.omnipede.boilerplate.system.exception.ErrorCode;
import io.omnipede.boilerplate.system.exception.SystemException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthUseCase {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    public String login(String email, String password) {

        // Email & password 확인
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new SystemException(ErrorCode.UNAUTHORIZED, "Not found email."));

        if (!user.login(password))
            throw new SystemException(ErrorCode.UNAUTHORIZED, "Wrong password.");

        // Token 발행
        return tokenService.create(user.getEmail());
    }

    public void logout(String token) {

        // 이미 비활성화된 토큰인지 확인

        // 토큰 비활성화

        //
    }

    public User authenticate(String token) {

        // Token 확인
        String email = tokenService.validate(token)
                .orElseThrow(() -> new SystemException(ErrorCode.UNAUTHORIZED, "JWT token validation failed."));

        // User 확인
        // TODO logout 처리된 토큰인지 확인

        // User 반환
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new SystemException(ErrorCode.UNAUTHORIZED, "Not found email."));
    }
}
