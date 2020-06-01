package wooteco.subway.exception;

public class IllegalLoginRequestException extends RuntimeException {
    public IllegalLoginRequestException(String message) {
        super(message);
    }
}
