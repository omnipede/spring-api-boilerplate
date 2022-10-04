package io.omnipede.boilerplate.system.config;

import io.omnipede.boilerplate.domain.auth.AuthUseCase;
import io.omnipede.boilerplate.domain.session.SessionUseCase;
import io.omnipede.boilerplate.system.resolver.JwtAuthResolver;
import io.omnipede.boilerplate.system.resolver.LoginUserResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
class WebMvcConfig implements WebMvcConfigurer {

    private final AuthUseCase authUseCase;
    private final SessionUseCase sessionUseCase;

    /**
     * controller 에서 쓰는 argument resolver 등록
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new JwtAuthResolver(authUseCase));
        resolvers.add(new LoginUserResolver(sessionUseCase));
    }

    /**
     * CORS 설정
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .maxAge(3600);
    }
}