package io.omnipede.boilerplate.system.resolver;

import io.omnipede.boilerplate.domain.session.SessionUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class ResolverConfig {

    private final SessionUseCase sessionUseCase;

    @Bean
    public LoginUserResolver loginUserResolver() {

        return new LoginUserResolver(sessionUseCase);
    }
}
