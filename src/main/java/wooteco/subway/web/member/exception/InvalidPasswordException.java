package wooteco.subway.web.member.exception;

import javax.security.sasl.AuthenticationException;

public class InvalidPasswordException extends AuthenticationException {
    private static final String MESSAGE = "잘못된 패스워드";

    public InvalidPasswordException() {
        super(MESSAGE);
    }
}
