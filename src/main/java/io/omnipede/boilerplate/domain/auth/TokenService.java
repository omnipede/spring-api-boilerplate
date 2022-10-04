package io.omnipede.boilerplate.domain.auth;

import java.util.Optional;

public interface TokenService {

    String create(String username);

    Optional<String> validate(String token);
}
