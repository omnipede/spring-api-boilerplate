package io.omnipede.boilerplate.controller.auth;

import io.omnipede.boilerplate.domain.session.SessionUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
class AuthController {

    private final SessionUseCase sessionUseCase;

    @PostMapping("/login")
    public void login(@Valid @RequestBody AuthReqDTO dto) {
        sessionUseCase.login(dto.getEmail(), dto.getPassword());
    }

    @DeleteMapping("/logout")
    public void logout(HttpSession session) {
        session.invalidate();
    }
}
