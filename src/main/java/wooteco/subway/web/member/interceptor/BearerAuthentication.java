package wooteco.subway.web.member.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.web.member.AuthorizationExtractor;
import wooteco.subway.web.member.InvalidAuthenticationException;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

@Component
public class BearerAuthentication implements Authentication {

    private final AuthorizationExtractor authExtractor;
    private final JwtTokenProvider jwtTokenProvider;

    public BearerAuthentication(final AuthorizationExtractor authExtractor, final JwtTokenProvider jwtTokenProvider) {
        this.authExtractor = authExtractor;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void setAuthentication(final HttpServletRequest request) {
        String token = authExtractor.extract(request, "bearer");
        if (token.isEmpty()) {
            throw new InvalidAuthenticationException("토큰이 존재하지 않습니다.");
        }

        if (!jwtTokenProvider.validateToken(token)) {
            throw new InvalidAuthenticationException("토큰이 유효하지 않습니다.");
        }

        String email = jwtTokenProvider.getSubject(token);
        request.setAttribute("loginMemberEmail", email);
    }

    @Override
    public <T> T getAuthentication(final NativeWebRequest request, final Class<T> tClass) {
        Object rawAuth = request.getAttribute("loginMemberEmail", SCOPE_REQUEST);
        T auth = tClass.cast(rawAuth);
        if (Objects.isNull(auth)) {
            throw new InvalidAuthenticationException("인증 정보가 존재하지 않습니다.");
        }
        return auth;
    }
}
