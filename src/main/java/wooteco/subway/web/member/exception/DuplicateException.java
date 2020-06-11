package wooteco.subway.web.member.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DuplicateException extends IllegalArgumentException {
    private static final String MESSAGE = " : 중복된 값입니다.";

    public DuplicateException(String message) {
        super(message + MESSAGE);
    }
}
