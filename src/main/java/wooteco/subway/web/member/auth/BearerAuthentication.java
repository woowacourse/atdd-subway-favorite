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
            return;
        }

        if (!jwtTokenProvider.validateToken(token)) {
            throw new InvalidAuthenticationException(ErrorCode.TONEN_NOT_AUTHORIZED);
        }

        String email = jwtTokenProvider.getSubject(token);
        request.setAttribute("loginMemberEmail", email);
    }

    @Override
    public <T> T getAuthentication(final NativeWebRequest request, final Class<T> tClass) {
        Object rawAuth = request.getAttribute("loginMemberEmail", SCOPE_REQUEST);
        T auth = tClass.cast(rawAuth);
        if (Objects.isNull(auth)) {
            throw new InvalidAuthenticationException(ErrorCode.TOKEN_NOT_FOUND);
        }
        return auth;
    }
}
