package wooteco.subway.web.member;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidAuthenticationException extends RuntimeException{
    private static final String MESSAGE = "잘못된 토큰 값 입니다.";

    public InvalidAuthenticationException(String message) {
        super(message);
    }

    public InvalidAuthenticationException() {
        super(MESSAGE);
    }
}
