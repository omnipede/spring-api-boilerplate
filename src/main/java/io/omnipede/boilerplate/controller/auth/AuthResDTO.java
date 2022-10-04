package io.omnipede.boilerplate.controller.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
class AuthResDTO {
    private final String token;
}
