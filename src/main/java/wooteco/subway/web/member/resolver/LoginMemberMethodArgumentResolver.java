package wooteco.subway.web.member.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import wooteco.subway.domain.member.LoginEmail;
import wooteco.subway.web.member.auth.Authentication;
import wooteco.subway.web.member.auth.LoginMember;

@Component
public class LoginMemberMethodArgumentResolver implements HandlerMethodArgumentResolver {
    private final Authentication authentication;

    public LoginMemberMethodArgumentResolver(final Authentication authentication) {
        this.authentication = authentication;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class) && parameter.getParameterType()
            .equals(LoginEmail.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String email = authentication.getAuthentication(webRequest, String.class);
        return new LoginEmail(email);
    }
}
