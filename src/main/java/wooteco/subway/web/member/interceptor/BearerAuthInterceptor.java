package wooteco.subway.web.member.interceptor;

import static wooteco.subway.exception.UnauthorizedException.REQUIRE_LOGIN_MESSAGE;
import static wooteco.subway.web.member.LoginMemberController.MEMBER_URI_WITH_AUTH;
import static wooteco.subway.web.member.MemberController.MEMBER_URI;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import wooteco.subway.exception.UnauthorizedException;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.web.member.AuthorizationExtractor;

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
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (isNoNeedVerifyToken(request)) {
            return true;
        }

        String credential = authExtractor.extract(request, BEARER);
        if (isInvalidToken(credential)) {
            throw new UnauthorizedException(REQUIRE_LOGIN_MESSAGE);
        }

        String email = jwtTokenProvider.getSubject(credential);
        request.setAttribute("loginMemberEmail", email);
        return true;
    }

    private boolean isNoNeedVerifyToken(HttpServletRequest request) {
        boolean isPostMethod = request.getMethod().equals(HttpMethod.POST.name());

        boolean isLoginRequest = isPostMethod && request.getRequestURI().equals(MEMBER_URI_WITH_AUTH);
        boolean isJoinRequest = isPostMethod && request.getRequestURI().equals(MEMBER_URI);

        return isLoginRequest || isJoinRequest;
    }

    private boolean isInvalidToken(String credential) {
        return !jwtTokenProvider.validateToken(credential);
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
