package wooteco.subway.web.exception;

public class NotMatchPasswordException extends AuthenticationException {

    private static final String ERROR_MESSAGE = "패스워드가 일치하지 않습니다.";

    public NotMatchPasswordException() {
        super(ERROR_MESSAGE);
    }

    public NotMatchPasswordException(String message) {
        super(message);
    }
}
