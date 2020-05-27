package wooteco.subway.web.member.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidPasswordException extends IllegalArgumentException{
    private static final String MESSAGE = "잘못된 패스워드";

    public InvalidPasswordException() {
        super(MESSAGE);
    }
}
