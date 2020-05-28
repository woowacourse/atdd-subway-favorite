package wooteco.subway.web.exceptions;

public class InvalidLoginException extends RuntimeException {
    public InvalidLoginException(final String message) {
        super(message);
    }
}
