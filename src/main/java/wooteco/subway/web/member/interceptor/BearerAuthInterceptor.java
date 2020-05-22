package wooteco.subway.web.member.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.web.member.AuthorizationExtractor;
import wooteco.subway.web.member.InvalidAuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.HttpMethod.POST;

@Component
public class BearerAuthInterceptor implements HandlerInterceptor {
    private AuthorizationExtractor authExtractor;
    private JwtTokenProvider jwtTokenProvider;

    public BearerAuthInterceptor(AuthorizationExtractor authExtractor, JwtTokenProvider jwtTokenProvider) {
        this.authExtractor = authExtractor;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) {
        if (isPost(request)) {
            return true;
        }

        String bearer = authExtractor.extract(request, "Bearer");
        validateToken(bearer);

        String email = jwtTokenProvider.getSubject(bearer);
        request.setAttribute("loginMemberEmail", email);
        return true;
    }

    private boolean isPost(HttpServletRequest request) {
        return POST.matches(request.getMethod());
    }

    private void validateToken(String bearer) {
        if (!jwtTokenProvider.validateToken(bearer)) {
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
