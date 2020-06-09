package wooteco.subway.web.member.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.web.member.AuthorizationExtractor;
import wooteco.subway.web.member.InvalidAuthenticationException;

@Component
public class BearerAuthInterceptor implements HandlerInterceptor {
    public static final String BEARER = "bearer";
    private final AuthorizationExtractor authExtractor;
    private final JwtTokenProvider jwtTokenProvider;

    public BearerAuthInterceptor(AuthorizationExtractor authExtractor,
        JwtTokenProvider jwtTokenProvider) {
        this.authExtractor = authExtractor;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) {
        String token = authExtractor.extract(request, BEARER);
        checkTokenNotEmpty(token);
        checkTokenValidity(token);

        String email = jwtTokenProvider.getSubject(token);
        request.setAttribute("loginMemberEmail", email);

        return true;
    }

    private void checkTokenValidity(final String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new InvalidAuthenticationException("유효하지 않은 토큰");
        }
    }

    private void checkTokenNotEmpty(final String token) {
        if (StringUtils.isEmpty(token)) {
            throw new InvalidAuthenticationException("존재하지 않는 토큰");
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
        ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
        Object handler, Exception ex) throws Exception {
    }
}
