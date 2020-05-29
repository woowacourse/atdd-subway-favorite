package wooteco.subway.exception;

public class NoResourceExistException extends IllegalArgumentException {
    public NoResourceExistException(String message) {
        super(message);
    }
}
