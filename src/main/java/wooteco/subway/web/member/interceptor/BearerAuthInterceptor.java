package wooteco.subway.web.member.interceptor;

import static wooteco.subway.web.member.LoginMemberMethodArgumentResolver.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.web.member.AuthorizationExtractor;

@Component
public class BearerAuthInterceptor implements HandlerInterceptor {
    private final AuthorizationExtractor authExtractor;
    private final JwtTokenProvider jwtTokenProvider;

    public BearerAuthInterceptor(AuthorizationExtractor authExtractor,
        JwtTokenProvider jwtTokenProvider) {
        this.authExtractor = authExtractor;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
        HttpServletResponse response, Object handler) {
        final String accessToken = authExtractor.extract(request, "bearer");

        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다!");
        }

        final String loginId = jwtTokenProvider.getSubject(accessToken);

        request.setAttribute(LOGIN_MEMBER_ID, loginId);
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
