package wooteco.subway.web.member.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.web.member.AuthorizationExtractor;

@Component
public class MemberApiInterceptor extends BearerAuthInterceptor {
    public MemberApiInterceptor(AuthorizationExtractor authExtractor,
            JwtTokenProvider jwtTokenProvider) {
        super(authExtractor, jwtTokenProvider);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) {
        return request.getMethod().equals(HttpMethod.POST.name()) ||
                super.preHandle(request, response, handler);
    }
}
