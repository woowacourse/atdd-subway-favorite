package wooteco.subway.web.member.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import wooteco.subway.web.auth.Auth;
import wooteco.subway.web.auth.RequiredAuth;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.web.member.AuthorizationExtractor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.Optional;

@Component
public class BearerAuthInterceptor implements HandlerInterceptor {
    public static final int ID_INDEX = 0;
    public static final int EMAIL_INDEX = 1;

    private AuthorizationExtractor authExtractor;
    private JwtTokenProvider jwtTokenProvider;

    public BearerAuthInterceptor(AuthorizationExtractor authExtractor, JwtTokenProvider jwtTokenProvider) {
        this.authExtractor = authExtractor;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) {
        RequiredAuth annotation = getAnnotation((HandlerMethod) handler, RequiredAuth.class);
        if (Objects.isNull(annotation) || annotation.type() == Auth.NONE) {
            return true;
        }
        String token = authExtractor.extract(request, "bearer");
        if (!jwtTokenProvider.validateToken(token)) {
            return false;
        }

        String tokenValue = jwtTokenProvider.getSubject(token);
        Long id = Long.parseLong(tokenValue.split(":")[ID_INDEX]);
        String email = tokenValue.split(":")[EMAIL_INDEX];

        request.setAttribute("loginMemberEmail", email);
        request.setAttribute("loginMemberId", id);

        if(annotation.type() == Auth.AUTH_BY_ID) {
            Long inputId = getId(request);
            return id.equals(inputId);
        }

        if(annotation.type() == Auth.AUTH_BY_EMAIL) {
            return email.equals(request.getParameter("email"));
        }

        return true;
    }

    private Long getId(HttpServletRequest request) {
        String uri = request.getRequestURI().split("/members/")[1];
        if (uri.contains("/")) {
            return Long.parseLong(uri.split("/")[0]);
        }
        return Long.parseLong(uri);
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
