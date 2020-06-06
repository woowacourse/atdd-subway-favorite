package wooteco.subway.web.member.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.web.member.AuthorizationExtractor;
import wooteco.subway.web.member.InvalidAuthenticationException;

@Component
public class BearerAuthInterceptor implements HandlerInterceptor {
    private static final String INVALID_TOKEN_MESSAGE = "유효하지 않은 토큰입니다!";
    private AuthorizationExtractor authExtractor;
    private JwtTokenProvider jwtTokenProvider;

    @Value("${web.request.login.member.email.key}")
    private String loginMemberEmailKey;

    public BearerAuthInterceptor(AuthorizationExtractor authExtractor, JwtTokenProvider jwtTokenProvider) {
        this.authExtractor = authExtractor;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
        HttpServletResponse response, Object handler) {
        String credentials = authExtractor.extractBearer(request);

        if (!jwtTokenProvider.validateToken(credentials)) {
            throw new InvalidAuthenticationException(INVALID_TOKEN_MESSAGE);
        }

        String email = jwtTokenProvider.getSubject(credentials);

        request.setAttribute(loginMemberEmailKey, email);
        return true;
    }
}
