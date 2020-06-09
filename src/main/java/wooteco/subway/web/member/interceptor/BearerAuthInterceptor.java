package wooteco.subway.web.member.interceptor;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.web.member.auth.AuthorizationExtractor;
import wooteco.subway.web.member.auth.RequireAuth;

@Component
public class BearerAuthInterceptor implements HandlerInterceptor {
    public static final String BEARER = "Bearer";
    private AuthorizationExtractor authExtractor;
    private JwtTokenProvider jwtTokenProvider;

    public BearerAuthInterceptor(AuthorizationExtractor authExtractor,
        JwtTokenProvider jwtTokenProvider) {
        this.authExtractor = authExtractor;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
        HttpServletResponse response, Object handler) {
        HandlerMethod handlerMethod = (HandlerMethod)handler;

        if (Objects.isNull(handlerMethod.getMethodAnnotation(RequireAuth.class))) {
            return true;
        }

        String extractedAuth = authExtractor.extract(request, BEARER);
        jwtTokenProvider.validateToken(extractedAuth);
        String email = jwtTokenProvider.getSubject(extractedAuth);
        request.setAttribute("loginMemberEmail", email);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
        HttpServletResponse response,
        Object handler,
        ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
        Object handler, Exception ex) throws Exception {

    }
}
