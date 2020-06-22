package wooteco.subway.web.member;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidAuthorizationException extends RuntimeException{
    public InvalidAuthorizationException(String message) {
        super(message);
    }
}
