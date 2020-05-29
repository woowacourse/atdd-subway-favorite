package wooteco.subway.web.member.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.web.exception.UnauthorizedException;
import wooteco.subway.web.member.AuthorizationExtractor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static wooteco.subway.web.exception.UnauthorizedException.REQUIRE_LOGIN_MESSAGE;

@Component
public class BearerAuthInterceptor implements HandlerInterceptor {
    public static final String BEARER = "bearer";

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
        // 회원가입 시 토큰 검증 불필요
        if (request.getMethod().equals("POST") && request.getRequestURI().equals("/members")) {
            return true;
        }
        String credential = authExtractor.extract(request, BEARER);
        if (!jwtTokenProvider.validateToken(credential)) {
            throw new UnauthorizedException(REQUIRE_LOGIN_MESSAGE);
        }

        String email = jwtTokenProvider.getSubject(credential);
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
