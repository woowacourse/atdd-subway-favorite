package wooteco.subway.web.member.auth;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.web.dto.ErrorCode;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

@Component
public class BearerAuthentication implements Authentication {

    private final HeaderExtractor headerExtractor;
    private final JwtTokenProvider jwtTokenProvider;

    public BearerAuthentication(final HeaderExtractor headerExtractor, final JwtTokenProvider jwtTokenProvider) {
        this.headerExtractor = headerExtractor;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void setAuthentication(final HttpServletRequest request) {
        String token = headerExtractor.extract(request, "bearer");
        if (token.isEmpty()) {
            return;
        }

        if (!jwtTokenProvider.validateToken(token)) {
            throw new InvalidAuthenticationException("토큰이 유효하지 않습니다.", ErrorCode.INVALID_AUTHENTICATION);
        }

        String email = jwtTokenProvider.getSubject(token);
        request.setAttribute("loginMemberEmail", email);
    }

    @Override
    public <T> T getAuthentication(final NativeWebRequest request, final Class<T> tClass) {
        Object rawAuth = request.getAttribute("loginMemberEmail", SCOPE_REQUEST);
        T auth = tClass.cast(rawAuth);
        if (Objects.isNull(auth)) {
            throw new InvalidAuthenticationException("인증 정보가 비어있습니다.", ErrorCode.INVALID_AUTHENTICATION);
        }
        return auth;
    }
}
