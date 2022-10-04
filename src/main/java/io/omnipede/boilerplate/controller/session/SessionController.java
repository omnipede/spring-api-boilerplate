package io.omnipede.boilerplate.controller.session;

import io.omnipede.boilerplate.domain.session.SessionUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/session")
@RequiredArgsConstructor
class SessionController {

    private final SessionUseCase sessionUseCase;

    @PostMapping
    public void post(@Valid @RequestBody SessionReqDTO dto) {
        sessionUseCase.login(dto.getEmail(), dto.getPassword());
    }

    @DeleteMapping
    public void delete(HttpSession session) {
        session.invalidate();
    }
}
