package wooteco.subway.web.member.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import wooteco.subway.exception.InvalidAuthenticationException;
import wooteco.subway.infra.TokenProvider;
import wooteco.subway.web.member.AuthorizationExtractor;

@Component
public class BearerAuthInterceptor implements HandlerInterceptor {
    private final AuthorizationExtractor authExtractor;
    private final TokenProvider tokenProvider;

    public BearerAuthInterceptor(AuthorizationExtractor authExtractor, TokenProvider tokenProvider) {
        this.authExtractor = authExtractor;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
        HttpServletResponse response, Object handler) {
        if (isCreateMember(request)) {
            return true;
        }
        String authHeaderValue = authExtractor.extract(request, "bearer");
        if (authHeaderValue.isEmpty()) {
            throw new InvalidAuthenticationException("로그인이 되지 않았습니다.");
        }
        if (isInvalidToken(authHeaderValue)) {
            throw new InvalidAuthenticationException("유효하지 않은 토큰입니다.");
        }
        String email = tokenProvider.getSubject(authHeaderValue);

        request.setAttribute("loginMemberEmail", email);
        return true;
    }

    private boolean isInvalidToken(String authHeaderValue) {
        return !tokenProvider.validateToken(authHeaderValue);
    }

    private boolean isCreateMember(HttpServletRequest request) {
        return request.getMethod().equalsIgnoreCase("POST")
            && request.getRequestURI().equals("/members");
    }
}
