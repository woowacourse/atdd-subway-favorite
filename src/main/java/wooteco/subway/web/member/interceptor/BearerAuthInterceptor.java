package wooteco.subway.web.member.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import wooteco.subway.web.member.auth.Authentication;

@Component
public class BearerAuthInterceptor implements HandlerInterceptor {
    private final Authentication authentication;

    public BearerAuthInterceptor(final Authentication authentication) {
        this.authentication = authentication;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) {
        authentication.setAuthentication(request);
        return true;
    }
}
