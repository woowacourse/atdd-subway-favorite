package wooteco.subway.exception;

public class LineStationNotFoundException extends RuntimeException {
    public LineStationNotFoundException(String message) {
        super(message);
    }
}
