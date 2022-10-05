package io.omnipede.boilerplate.domain.user;

import io.omnipede.boilerplate.system.exception.ErrorCode;
import io.omnipede.boilerplate.system.exception.SystemException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository userRepository;

    public void signup(User user) {

        // 중복 가입 확인
        userRepository.findByEmail(user.getEmail())
                .ifPresent(s -> {
                    throw new SystemException(ErrorCode.CONFLICT, "User " + user.getEmail() + " already exists.");
                });

        // 주의. 현재 password encryption 은 column level 에서 진행하고 있다.
        // User class 참조.

        // 신규 유저 등록
        userRepository.save(user);
    }
}
