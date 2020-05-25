package wooteco.subway.web.member.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.web.member.AuthorizationExtractor;

@Component
public class BearerAuthInterceptor implements HandlerInterceptor {
    private static final String BEARER = "BEARER";

    private final AuthorizationExtractor authExtractor;
    private final JwtTokenProvider jwtTokenProvider;

    public BearerAuthInterceptor(AuthorizationExtractor authExtractor, JwtTokenProvider jwtTokenProvider) {
        this.authExtractor = authExtractor;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String authorizationHeaderValue = authExtractor.extract(request, BEARER);
        if (jwtTokenProvider.validateToken(authorizationHeaderValue)) {
            request.setAttribute("loginMemberEmail", jwtTokenProvider.getSubject(authorizationHeaderValue));
        }
        return true;

        // TODO: 2020/05/22 sendError와 controllerAdvice 선택해서 반영
        // TODO: 2020/05/22 사용자 파라미터나 쿼리에서 오는 다른 정보와 비교하여 인증하는 로직 여기로
    }
}
