package wooteco.subway.web.exception.member;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidAuthenticationException extends MemberException {
    public InvalidAuthenticationException() {
        super("비정상적인 로그인입니다.");
    }
}
