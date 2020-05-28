package wooteco.subway.web.member;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import wooteco.subway.web.BusinessException;
import wooteco.subway.web.UserUnauthorizedException;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidAuthenticationException extends UserUnauthorizedException {
    private static final String ERROR_CODE = "INVALID_AUTHENTICATION";

    public InvalidAuthenticationException() {
        super(ERROR_CODE, "유효하지 않은 토큰입니다.");
    }
}
