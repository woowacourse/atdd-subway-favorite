package wooteco.subway.web.member.interceptor;

import java.lang.annotation.Annotation;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import io.jsonwebtoken.JwtException;
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
        IsAuth annotation = getAnnotation((HandlerMethod)handler, IsAuth.class);
        if (annotation.isAuth() == Auth.NONE) {
            return true;
        }
        String token = authExtractor.extract(request, "bearer");

        if (jwtTokenProvider.nonValidToken(token)) {
            throw new JwtException("유효하지 않는 토큰입니다.");
        }

        String email = jwtTokenProvider.getSubject(token);
        request.setAttribute("email", email);
        return true;
    }

    private <A extends Annotation> A getAnnotation(HandlerMethod handlerMethod, Class<A> annotationType) {
        return Optional.ofNullable(handlerMethod.getMethodAnnotation(annotationType))
            .orElse(handlerMethod.getBeanType().getAnnotation(annotationType));
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
        Exception ex) throws Exception {

    }
}
