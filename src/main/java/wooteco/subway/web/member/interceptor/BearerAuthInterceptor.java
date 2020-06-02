package wooteco.subway.web.member.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import wooteco.subway.exception.authentication.InvalidTokenException;
import wooteco.subway.infra.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        InterceptorValidator interceptorValidator = new InterceptorValidator();
        if (!interceptorValidator.isValid(handler)) {
            return true;
        }

        String token = authExtractor.extract(request, "Bearer");
        if (StringUtils.isEmpty(token) || (!jwtTokenProvider.validateToken(token))) {
            throw new InvalidTokenException();
        }

        String email = jwtTokenProvider.getSubject(token);
        request.setAttribute("loginMemberEmailJwt", email);
        return true;
    }
}
