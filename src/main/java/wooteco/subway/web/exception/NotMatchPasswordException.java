package wooteco.subway.web.exception;

public class NotMatchPasswordException extends RuntimeException {
    public NotMatchPasswordException(String message) {
        super(message);
    }
}
