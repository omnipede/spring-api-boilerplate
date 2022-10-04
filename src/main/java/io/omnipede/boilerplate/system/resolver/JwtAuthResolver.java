package io.omnipede.boilerplate.system.resolver;

import io.omnipede.boilerplate.domain.auth.AuthUseCase;
import io.omnipede.boilerplate.domain.user.User;
import io.omnipede.boilerplate.system.exception.ErrorCode;
import io.omnipede.boilerplate.system.exception.SystemException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
public class JwtAuthResolver implements HandlerMethodArgumentResolver {

    private final AuthUseCase authUseCase;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(JwtAuth.class) && parameter.getParameterType() == User.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        // Get token from HTTP request
        String token = webRequest.getHeader("Authorization");
        if (token == null)
            throw new SystemException(ErrorCode.UNAUTHORIZED, "Authorization token is required.");

        // Return authenticated user.
        return authUseCase.authenticate(token);
    }
}
