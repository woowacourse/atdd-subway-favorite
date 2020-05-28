package wooteco.subway.exception;

public class InvalidStationIdException extends RuntimeException {
    public InvalidStationIdException(String message) {
        super(message);
    }
}
