package io.omnipede.boilerplate.system.resolver;

import io.omnipede.boilerplate.domain.session.SessionUseCase;
import io.omnipede.boilerplate.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
public class LoginUserResolver implements HandlerMethodArgumentResolver {

    private final SessionUseCase sessionUseCase;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class) && parameter.getParameterType() == User.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        // 세션에 저장된 user 데이터를 반환한다.
        return sessionUseCase.auth();
    }
}
