package wooteco.subway.web.member.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.web.member.AuthorizationExtractor;

@Component
public class BearerAuthInterceptor implements HandlerInterceptor {
    private final AuthorizationExtractor authExtractor;
    private final JwtTokenProvider jwtTokenProvider;

    public BearerAuthInterceptor(AuthorizationExtractor authExtractor, JwtTokenProvider jwtTokenProvider) {
        this.authExtractor = authExtractor;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
        HttpServletResponse response, Object handler) {
        String authHeaderValue = authExtractor.extract(request, "bearer");
        jwtTokenProvider.validateToken(authHeaderValue);
        String email = jwtTokenProvider.getSubject(authHeaderValue);

        request.setAttribute("loginMemberEmail", email);
        return true;
    }
}
