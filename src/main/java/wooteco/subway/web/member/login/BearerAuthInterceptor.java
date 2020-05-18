package wooteco.subway.web.member.login;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import wooteco.subway.web.member.InvalidAuthenticationException;

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
        String token = authExtractor.extract(request, "Bearer");
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
}
