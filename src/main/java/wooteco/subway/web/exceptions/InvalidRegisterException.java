package wooteco.subway.web.exceptions;

public class InvalidRegisterException extends RuntimeException {
    public InvalidRegisterException(final String message) {
        super(message);
    }
}
