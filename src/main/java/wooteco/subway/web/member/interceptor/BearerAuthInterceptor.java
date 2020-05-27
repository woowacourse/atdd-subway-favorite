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
        if (isCreateMemberMethod(request) || isLoginMethod(request)) {
            return true;
        }
        String token = authExtractor.extract(request, "bearer");
        if (StringUtils.isEmpty(token)) {
            throw new InvalidAuthenticationException("존재하지 않는 토큰");
        }
        if (!jwtTokenProvider.validateToken(token)) {
            throw new InvalidAuthenticationException("유효하지 않은 토큰");
        }

        String email = jwtTokenProvider.getSubject(token);
        request.setAttribute("loginMemberEmail", email);

        return true;
    }

    private boolean isLoginMethod(final HttpServletRequest request) {
        return request.getMethod().equals("POST") && request.getRequestURI().equals("/me/login");
    }

    private boolean isCreateMemberMethod(final HttpServletRequest request) {
        return request.getMethod().equals("POST") && request.getRequestURI().equals("/me");
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
