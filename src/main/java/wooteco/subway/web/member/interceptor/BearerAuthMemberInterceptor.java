package wooteco.subway.web.member.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.web.member.exception.InvalidAuthenticationException;
import wooteco.subway.web.member.util.AuthorizationExtractor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class BearerAuthMemberInterceptor implements HandlerInterceptor {
    private AuthorizationExtractor authExtractor;
    private JwtTokenProvider jwtTokenProvider;

    public BearerAuthMemberInterceptor(AuthorizationExtractor authExtractor, JwtTokenProvider jwtTokenProvider) {
        this.authExtractor = authExtractor;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) {
        String token = authExtractor.extract(request, "Bearer");
        validateToken(token);
        String email = jwtTokenProvider.getSubject(token);
        request.setAttribute("requestMemberEmail", email);
        return true;
    }

    private void validateToken(String token) {
        if (token.isEmpty() || !jwtTokenProvider.validateToken(token)) {
            throw new InvalidAuthenticationException();
        }
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
