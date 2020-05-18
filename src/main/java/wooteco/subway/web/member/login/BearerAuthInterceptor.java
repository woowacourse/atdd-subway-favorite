package wooteco.subway.web.member.login;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

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
        // TODO: Authorization 헤더를 통해 Bearer 값을 추출 (authExtractor.extract() 메서드 활용)

        // TODO: 추출한 토큰값의 유효성 검사 (jwtTokenProvider.validateToken() 메서드 활용)

        // TODO: 추출한 토큰값에서 email 정보 추출 (jwtTokenProvider.getSubject() 메서드 활용)
        String email = Strings.EMPTY;
        request.setAttribute("loginMemberEmail", email);
        return true;
    }
}
