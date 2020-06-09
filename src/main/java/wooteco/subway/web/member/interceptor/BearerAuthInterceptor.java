package wooteco.subway.web.member.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.web.member.AuthorizationExtractor;

@Component
public class BearerAuthInterceptor implements HandlerInterceptor {
    public static final String LOGIN_KEY = "loginMemberEmail";
    public static final String BEARER = "BEARER";

    private final AuthorizationExtractor authExtractor;
    private final JwtTokenProvider jwtTokenProvider;

    public BearerAuthInterceptor(AuthorizationExtractor authExtractor, JwtTokenProvider jwtTokenProvider) {
        this.authExtractor = authExtractor;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String authorizationHeaderValue = authExtractor.extract(request, BEARER);
        if (jwtTokenProvider.validateToken(authorizationHeaderValue)) {
            request.setAttribute(LOGIN_KEY, jwtTokenProvider.getSubject(authorizationHeaderValue));
        }
        return true;
    }
}
