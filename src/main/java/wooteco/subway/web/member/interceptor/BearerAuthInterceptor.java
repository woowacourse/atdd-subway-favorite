package wooteco.subway.web.member.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import wooteco.subway.config.Auth;
import wooteco.subway.config.IsAuth;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.web.member.AuthorizationExtractor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.Optional;

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
        IsAuth annotation = getAnnotation((HandlerMethod)handler, IsAuth.class);
        if(annotation.isAuth() == Auth.NONE) {
            return true;
        }
        String token = authExtractor.extract(request, "bearer");
        if (jwtTokenProvider.validateToken(token)) {
            String email = jwtTokenProvider.getSubject(token);
            request.setAttribute("loginMemberEmail", email);
            return true;
        }
        return false;
    }

    private <A extends Annotation> A getAnnotation(HandlerMethod handlerMethod, Class<A> annotationType) {
        return Optional.ofNullable(handlerMethod.getMethodAnnotation(annotationType))
                .orElse(handlerMethod.getBeanType().getAnnotation(annotationType));
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
