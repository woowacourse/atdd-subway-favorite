package wooteco.subway.web.member.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import wooteco.subway.infra.TokenProvider;
import wooteco.subway.web.member.AuthorizationExtractor;

@Component
public class BearerAuthInterceptor implements HandlerInterceptor {
    private static final String LOGIN_MEMBER_ATTRIBUTE_NAME = "loginMemberEmail";
    private static final String AUTHORIZATION_TYPE = "bearer";

    private final AuthorizationExtractor authExtractor;
    private final TokenProvider tokenProvider;

    public BearerAuthInterceptor(AuthorizationExtractor authExtractor, TokenProvider tokenProvider) {
        this.authExtractor = authExtractor;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
        HttpServletResponse response, Object handler) {
        String authHeaderValue = authExtractor.extract(request, AUTHORIZATION_TYPE);
        String email = tokenProvider.getSubject(authHeaderValue);

        request.setAttribute(LOGIN_MEMBER_ATTRIBUTE_NAME, email);
        return true;
    }
}
