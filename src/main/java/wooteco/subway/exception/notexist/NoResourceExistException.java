package wooteco.subway.exception.notexist;

public class NoResourceExistException extends IllegalArgumentException {
    public NoResourceExistException(String message) {
        super(message);
    }
}
